package com.vt.cms.order.application.service.impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.base.enums.MessageCode;
import com.vt.cms.campain.application.dto.response.CampaignProductResponse;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import com.vt.cms.campain.application.service.port.CampaignProductPort;
import com.vt.cms.campain.domain.enity.CampainProduct;
import com.vt.cms.campain.domain.repository.CampainProductRepository;
import com.vt.cms.order.application.dto.request.*;
import com.vt.cms.order.application.dto.response.*;
import com.vt.cms.order.application.service.BuyerOrderService;
import com.vt.cms.order.application.service.port.CampaignPort;
import com.vt.cms.order.domain.OrderStatus;
import com.vt.cms.order.domain.entity.Order;
import com.vt.cms.order.domain.entity.OrderItem;
import com.vt.cms.order.domain.repository.OrderItemRepository;
import com.vt.cms.order.domain.repository.OrderRepository;
import com.vt.cms.payment.domain.enums.PaymentMethod;
import com.vt.cms.product.application.dto.response.SkuResponse;
import com.vt.cms.product.domain.entity.Product_Sku;
import com.vt.cms.product.domain.repository.ProductRepository;
import com.vt.cms.product.domain.repository.ProductSkuRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BuyerOrderServiceImpl implements BuyerOrderService {

  private final CampaignProductPort campaignProductPort;
  private final CampaignPort campaignPort;
  private final ProductSkuRepository productSkuRepository;
  private final CampainProductRepository campainProductRepository;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;

  public BuyerOrderServiceImpl(
      CampaignProductPort campaignProductPort,
      CampaignPort campaignPort,
      ProductSkuRepository productSkuRepository,
      CampainProductRepository campainProductRepository,
      ProductRepository productRepository,
      OrderRepository orderRepository,
      OrderItemRepository orderItemRepository) {
    this.campaignProductPort = campaignProductPort;
    this.campaignPort = campaignPort;
    this.productSkuRepository = productSkuRepository;
    this.campainProductRepository = campainProductRepository;
    this.productRepository = productRepository;
    this.orderRepository = orderRepository;
    this.orderItemRepository = orderItemRepository;
  }

  @Override
  public CheckoutPreviewResponse checkoutPreview(CheckoutPreviewRequest request) {
    validateRequest(request);
    // 1. Lấy danh sách campainId duy nhất lấy campaign ID từ các item trong request và loại bỏ ID
    // trùng nhau.
    Set<Integer> uniqueCampaignIds = getuniqueCampaignIdsInCheckoutPreviewRequest(request);
    System.out.println("A" + uniqueCampaignIds);
    // Lấy thông tin campain
    List<CampainResponse> campainInfo = campaignPort.getCampaignInfoById(uniqueCampaignIds);
    System.out.println("B" + campainInfo);
    // 3. Lấy thông tin campain product
    List<CampaignProductResponse> campaignProductInfos =
        campaignPort.findCampaignProductByCampaignIdIn(uniqueCampaignIds);
    System.out.println("campaignProductInfos" + campaignProductInfos);
    // 4. Kiểm tra campain và stock: Sản phẩm trong campain có hợp lệ về thời gian không
    checkCampaignExpired(campainInfo, campaignProductInfos, request);
    // 5. Kiểm tra số lượng hàng tồn kho có đủ đáp ứng số lượng người dùng muốn mua không
    checkinventorystock(request, campaignProductInfos);
    List<ItemSelection> selections =
        request.getItem().stream()
            .map(item ->
                new ItemSelection(
                    item.getProduct_id(),
                    item.getSku_id(),
                    item.getCampaign_id(),
                    item.getQuantity()))
            .collect(Collectors.toList());
    // 7. Lấy thông tin SKU
    List<SkuResponse> skuInfos = loadSkuInfosForSelections(selections);
    // 8. Map Campaign Product
    Map<SkuCampaignKey, CampainProduct> campaignProductMap = loadCampaignProductMap(selections);
    // 9. Build SKU Preview
    List<SkuPreviewDto> skus = buildSkuResponses(selections, skuInfos, campaignProductMap);

    // 10. Build response

    return buildCheckoutReponse(skus);
  }

  public Set<Integer> getUniqueCampaignIds(List<OrderRequest> orderRequests) {
    return orderRequests.stream().map(OrderRequest::getCampaignId).collect(Collectors.toSet());
  }

  private Set<Integer> getUniqueSkuIds(List<OrderRequest> orders) {

    return orders.stream()
        .flatMap(order -> order.getItem().stream())
        .map(item -> item.getSkuId())
        .collect(Collectors.toSet());
  }

  private Map<Integer, SkuResponse> buildSkuResMap(
      Set<Integer> uniqueSkuIds, Set<Integer> uniqueCampaignIds) {
    List<SkuResponse> skuResList =
        productSkuRepository.findSkuWithCampaignStock(uniqueSkuIds, uniqueCampaignIds);
    return skuResList.stream().collect(Collectors.toMap(SkuResponse::getId, Function.identity()));
  }

  // TODO: Xử lý validate tạo đơn hàng
  //
  //  private void validateCreateOrder(
  //          Set<Integer> uniqueCampaignIds,
  //          Set<Integer> uniqueSkuIds,
  //          List<CampainResponse> campaignInfos,
  //          List<CampaignProductResponse> campaignProductInfos,
  //          CreateOrderRequest request) {
  //    Set<Integer> uniqueProductIds = getUniqueProductIds(request.getOrders());
  //
  //    checkExistsCampaign(uniqueCampaignIds, campaignInfos);
  //    checkExistsProduct(uniqueProductIds, campaignProductInfos);
  //    checkExistsSku(uniqueSkuIds, campaignProductInfos);
  //    checkCampaignExpired(campaignInfos, campaignProductInfos, null);
  //    checkInsufficientStock(request.getOrders(), null, campaignProductInfos);
  //    checkTotalAmount(request, campaignProductInfos);
  //  }

  private Map<Integer, CampaignProductResponse> buildSkuInCampaignMap(
      List<CampaignProductResponse> campaignProductInfos) {
    return campaignProductInfos.stream()
        .collect(Collectors.toMap(CampaignProductResponse::getSkuId, Function.identity()));
  }

  private Map<Integer, CampainResponse> buildCampaignMap(List<CampainResponse> campaignInfos) {
    return campaignInfos.stream()
        .collect(Collectors.toMap(CampainResponse::getId, Function.identity()));
  }

  private List<OrderItem> buildOrderItems(
      OrderRequest orderReq,
      Map<Integer, CampaignProductResponse> skuInfoMap,
      Map<Integer, SkuResponse> skuResMap) {
    List<OrderItem> items = new ArrayList<>();
    for (OrderItemRequest itemReq : orderReq.getItem()) {
      CampaignProductResponse skuInfo = skuInfoMap.get(itemReq.getSkuId());
      SkuResponse skuRes = skuResMap.get(itemReq.getSkuId());
      BigDecimal originalPrice = skuInfo.getPrice(); // gia goc
      BigDecimal unitPrice = skuInfo.getDisCountPrice(); // gia ban
      BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
      Integer totalWeight = itemReq.getQuantity() * skuRes.getWeightGram();

      OrderItem orderItem = new OrderItem();
      orderItem.setSku_id(itemReq.getSkuId());
      orderItem.setQuantity(itemReq.getQuantity());
      orderItem.setOriginalPrice(originalPrice);
      orderItem.setUnitPrice(unitPrice);
      orderItem.setTotal_price(totalPrice);
      orderItem.setTotalWeight(totalWeight);

      items.add(orderItem);
    }

    return items;
  }

  private void buildOrders(
      List<OrderRequest> orderRequests,
      Map<Integer, CampaignProductResponse> skuCampaignInfoMap,
      List<Order> orders,
      Map<Integer, List<OrderItem>> orderItemMap,
      Map<Integer, CampainResponse> campaignInfoMap,
      BigDecimal shippingFeeMap,
      Map<Integer, SkuResponse> skuResMap,
      Integer userId) {
    for (int i = 0; i < orderRequests.size(); i++) {
      // ToDO  BigDecimal shippingFee = shippingFeeMap.getOrDefault(i, BigDecimal.ZERO);
      OrderRequest orderReq = orderRequests.get(i);
      Order order = new Order();
      order.setOrderStatus(OrderStatus.WAIT_PAYMENT);
      order.setProductId(orderReq.getProductId());
      order.setCampaignId(orderReq.getCampaignId());
      order.setCreatedBy(userId.toString());
      order.setShipperCode(orderReq.getShipping_service_code());
      order.setShipperName(orderReq.getShipping_service_name());
      order.setCreatedAt(LocalDateTime.now());
      List<OrderItem> items = buildOrderItems(orderReq, skuCampaignInfoMap, skuResMap);
      System.out.println("items:" + items);
      Integer itemQuantity = items.stream().map(OrderItem::getQuantity).reduce(0, Integer::sum);
      BigDecimal itemTotal =
          items.stream().map(OrderItem::getTotal_price).reduce(BigDecimal.ZERO, BigDecimal::add);
      Integer totalWeight = items.stream().map(OrderItem::getTotalWeight).reduce(0, Integer::sum);
      order.setTotalPrice(itemTotal);
      order.setTotalWeight(totalWeight);
      order.setTotalQuantity(itemQuantity);
      order.setTotalPayment(itemTotal);
      order.setShippingFee(BigDecimal.valueOf(0)); // phi van chuyen: shippingFee
      orders.add(order);
      orderItemMap.put(i, items);
    }
  }

  // TODO  Tính phí vân chuyển đơn hàng

  //  private Map<Integer, BigDecimal> calculateShippingFees(
  //          Map<Integer, SkuResponse> skuResMap, CreateOrderRequest request) {
  //    Map<Integer, BigDecimal> shippingFeeMap = new HashMap<>();
  //    String requestId = RequestUtils.extractRequestId();
  //
  //    for (int i = 0; i < request.getOrders().size(); i++) {
  //      CreateOrderRequest.OrderRequest orderReq = request.getOrders().get(i);
  //
  //      BigDecimal totalItemPrice = BigDecimal.ZERO;
  //      int totalWeight = 0;
  //
  //      for (CreateOrderRequest.OrderItemRequest item : orderReq.getItems()) {
  //        BigDecimal price = skuResMap.get(item.getSkuId()).getPriceDiscount();
  //        totalItemPrice =
  // totalItemPrice.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
  //
  //        ProductSkuRes skuRes = skuResMap.get(item.getSkuId());
  //        if (skuRes != null) {
  //          totalWeight += (skuRes.getWeightGram() * item.getQuantity());
  //        }
  //      }
  //
  //      ShippingPerServiceRequest vtpReq =
  //              buildVtpRequest(orderReq, request.getShipping(), totalWeight, totalItemPrice);
  //      log.info("ShppingPersServiceRequest: {}", JsonUtils.toJson(vtpReq));
  //
  //      BigDecimal fee =
  //              shippingPartnerGateway.getShippingPerService(requestId, vtpReq).getMoneyTotal();
  //      shippingFeeMap.put(i, fee != null ? fee : BigDecimal.ZERO);
  //    }
  //    return shippingFeeMap;
  //  }
  @Override
  public OrderCreateResponse createOrder(CreateOrderRequest createorderRequest, Integer userId) {
    // Check valadate request
    if (createorderRequest == null
        || createorderRequest.getOrders() == null
        || createorderRequest.getOrders().isEmpty()) {
      throw new BusinessException(MessageCode.INVALID_ORDER);
    }
    List<OrderRequest> orderRequests = createorderRequest.getOrders();
    // ---------------------------------------------------------//
    // Lấy thông tin campain từ request
    Set<Integer> uniqueCampaignIds = getUniqueCampaignIds(orderRequests);
    // Lấy thông tin SKU từ request
    Set<Integer> uniqueSkuIds = getUniqueSkuIds(orderRequests);
    // Lấy thông tin Campaign
    List<CampainResponse> campaignInfos = campaignPort.getCampaignInfoById(uniqueCampaignIds);
    // Lấy sản phẩm thuộc Campaign
    List<CampaignProductResponse> campaignProductInfos =
        campaignPort.findCampaignProductByCampaignIdIn(uniqueCampaignIds);
    System.out.println("Check 123" + campaignProductInfos);
    // Build map SKU
    Map<Integer, SkuResponse> skuResMap = buildSkuResMap(uniqueSkuIds, uniqueCampaignIds);
    // TODO Tính phí vận chuyển
    // Map<Integer, BigDecimal> shippingFeeMap = calculateShippingFees(skuResMap, orderRequests);

    // TODO  Check validate
    // validateCreateOrder(uniqueCampaignIds, uniqueSkuIds, campaignInfos, campaignProductInfos,
    // orderRequests);

    // chuyển List thành Map để tra cứu nhanh hơn.
    Map<Integer, CampaignProductResponse> skuCampaignInfoMap =
        buildSkuInCampaignMap(campaignProductInfos);
    Map<Integer, CampainResponse> campaignInfoMap = buildCampaignMap(campaignInfos);
    BigDecimal shippingFeeMap = BigDecimal.valueOf(0); // TODO: Giả lập phí =0
    List<Order> orders = new ArrayList<>();
    Map<Integer, List<OrderItem>> orderItemMap = new HashMap<>();
    buildOrders(
        orderRequests,
        skuCampaignInfoMap,
        orders,
        orderItemMap,
        campaignInfoMap,
        shippingFeeMap,
        skuResMap,
        userId);
    int savedOrders = orderRepository.saveAll(orders);
    List<Integer> orderIds = orders.stream().map(Order::getId).toList();

    List<OrderItem> orderItems = buildOrderItem(orders, orderItemMap);
    // Cập nhật kho campain khi mua sản phẩm
    decreaseStock(orderRequests, orderItems, campaignProductInfos);
    orderItemRepository.saveAll(orderItems);
    OrderCreateResponse response = new OrderCreateResponse();
    response.setOrderId(orderIds);
    response.setTotal_payment(createorderRequest.getTotalAmount());
    response.setPayment_method(PaymentMethod.QR);
    return response;
  }

  // Trừ kho campain & campainproduct khi mua sản phẩm
  private void decreaseStock(
      List<OrderRequest> orderRequests,
      List<OrderItem> orderItems,
      List<CampaignProductResponse> campaignProductInfos) {

    Map<Integer, Integer> skuToCampaignProduct =
        campaignProductInfos.stream()
            .collect(
                Collectors.toMap(
                    CampaignProductResponse::getSkuId, CampaignProductResponse::getId));

    List<Integer> failedCampaignProducts = new ArrayList<>();
    for (OrderItem item : orderItems) {
      Integer campaignProductId = skuToCampaignProduct.get(item.getSku_id());
      int updated = campaignPort.decreaseStock(campaignProductId, item.getQuantity());
      if (updated == 0) {
        failedCampaignProducts.add(campaignProductId);
      }
    }
    if (!failedCampaignProducts.isEmpty()) {
      throw new BusinessException(MessageCode.INSUFFICIENT_STOCK, failedCampaignProducts);
    }
    // Cộng lại campain (đã fix)
    Map<Integer, Integer> campaignTotalQty =
        orderRequests.stream()
            .collect(
                Collectors.groupingBy(
                    OrderRequest::getCampaignId,
                    Collectors.summingInt(order ->
                        order.getItem().stream().mapToInt(OrderItemRequest::getQuantity).sum())));
    campaignTotalQty.forEach((campaignId, totalQuantity) -> {
      try {
        System.out.println("campainID" + campaignId);
        System.out.println("totalquantity" + totalQuantity);
        campaignPort.decreaseTotalStock(campaignId, totalQuantity);
      } catch (Exception e) {
        System.out.println("Loi" + e.getMessage());
        System.out.println("Loi" + e.getStackTrace());

        throw new BusinessException(MessageCode.INSUFFICIENT_STOCK);
      }
    });
  }

  private List<OrderItem> buildOrderItem(
      List<Order> orders, Map<Integer, List<OrderItem>> orderItemMap) {

    List<OrderItem> orderItems = new ArrayList<>();

    for (int i = 0; i < orders.size(); i++) {

      Integer orderId = orders.get(i).getId();
      List<OrderItem> items = orderItemMap.get(i);

      for (OrderItem item : items) {
        item.setOrder_id(orderId);
        orderItems.add(item);
      }
    }

    return orderItems;
  }

  // Tính tiền tổng đơn hàng
  private BigDecimal calculateTotalPrice(List<SkuPreviewDto> skus) {
    return skus.stream()
        .map(item ->
            item.getPrice() != null
                ? item.getPriceDiscount().multiply(BigDecimal.valueOf(item.getQuantity()))
                : BigDecimal.ZERO)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private CheckoutPreviewResponse buildCheckoutReponse(List<SkuPreviewDto> skus) {

    CheckoutPreviewResponse response = new CheckoutPreviewResponse();
    response.setPaymentMethod(PaymentMethod.QR);
    response.setSkus(skus);
    response.setTotalPrice(calculateTotalPrice(skus));
    return response;
  }

  private List<SkuPreviewDto> buildSkuResponses(
      List<ItemSelection> selections,
      List<SkuResponse> skuInfos,
      Map<SkuCampaignKey, CampainProduct> campaignProductMap) {

    // Tạo Map SKU để tìm kiếm nhanh
    Map<Integer, SkuResponse> skuMap =
        skuInfos.stream()
            .collect(
                Collectors.toMap(
                    SkuResponse::getId, Function.identity(), (existing, replacement) -> existing));

    return selections.stream()
        .map(selection -> {
          // Lấy thông tin SKU
          SkuResponse sku = skuMap.get(selection.skuId());
          if (sku == null) {
            throw new RuntimeException("SKU not found: " + selection.skuId());
          }
          // Lấy thông tin Campaign Product
          SkuCampaignKey key = new SkuCampaignKey(selection.skuId(), selection.campaignId());

          CampainProduct campaignProduct = campaignProductMap.get(key);

          // Build response
          SkuPreviewDto response = new SkuPreviewDto();

          response.setSkuId(sku.getId());
          response.setCampaign_id(sku.getCampaignId());
          response.setProduct_id(sku.getProductId());
          response.setProduct_name(sku.getProductName());
          response.setQuantity(selection.quantity());
          response.setSkuImageUrl(sku.getImageUrl());
          response.setTypeProduct(sku.getTypeProduct());
          response.setAttribute(sku.getAttribute());

          // Giá bán
          if (campaignProduct != null) {
            response.setPrice(campaignProduct.getPrice());
            response.setStockCampaign(campaignProduct.getStockCampaign());
            response.setPriceDiscount(campaignProduct.getPriceDiscount());

          } else {
            response.setPrice(sku.getPrice());
          }
          return response;
        })
        .collect(Collectors.toList());
  }

  private List<SkuResponse> loadSkuInfosForSelections(List<ItemSelection> selections) {
    Set<Integer> skuIds =
        selections.stream()
            .map(ItemSelection::skuId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    Set<Integer> campaignIds =
        selections.stream()
            .map(ItemSelection::campaignId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    return productSkuRepository.findSkuWithCampaignStock(skuIds, campaignIds);
  }

  private Map<SkuCampaignKey, CampainProduct> loadCampaignProductMap(
      List<ItemSelection> selections) {

    List<Integer> campaignIds =
        selections.stream().map(ItemSelection::campaignId).distinct().toList();

    Set<SkuCampaignKey> requestKeys =
        selections.stream()
            .map(s -> new SkuCampaignKey(s.skuId(), s.campaignId()))
            .collect(Collectors.toSet());

    return campainProductRepository.findAllByCampaignIds(campaignIds).stream()
        .filter(cp -> requestKeys.contains(new SkuCampaignKey(cp.getSkuId(), cp.getCampaignId())))
        .collect(
            Collectors.toMap(
                cp -> new SkuCampaignKey(cp.getSkuId(), cp.getCampaignId()), Function.identity()));
  }

  // Kiểm tra số lượng hàng tồn kho có đủ đáp ứng số lượng người dùng muốn mua không
  public void checkinventorystock(
      CheckoutPreviewRequest request, List<CampaignProductResponse> campainProductInfo) {
    // 1. Lấy số lượng sp đặt mua
    Map<Integer, Integer> quantitySkuMap;
    quantitySkuMap = getQuantitySkuMapFromRequest(request);
    //    if (request == null) {
    //           quantitySkuMap = getQuantitySkuMapFromOrders(orders);
    //    } else {
    //      quantitySkuMap = getQuantitySkuMapFromRequest(request);
    //    }
    // 2. Lấy số lượng tồn kho của từng SKU
    Map<Integer, Integer> stockSkuMap = getStockSkuMap(campainProductInfo);
    // 3. Kiểm tra SKu hết hàng
    List<Integer> outOfStockSku =
        quantitySkuMap.keySet().stream()
            .filter(skuId -> stockSkuMap.getOrDefault(skuId, 0) == 0)
            .toList();
    // 4. Xử lý trường hợp SKU hết hàng
    if (!outOfStockSku.isEmpty() && request == null)
      throw new BusinessException(
          MessageCode.SKU_OUT_OF_STOCK, outOfStockSku.stream().map(String::valueOf).toList());

    if (!outOfStockSku.isEmpty()) {
      List<ProductSkuDTO> outOfStockSkus =
          productSkuRepository.findSkuByIds(outOfStockSku).stream()
              .map(this::toProductSkuDTO)
              .toList();
      throw new BusinessException(MessageCode.SKU_OUT_OF_STOCK, outOfStockSkus);
    }
    // 5. Kiểm tra số lượng đặt mua vượt quá tồn kho
    Map<Integer, Integer> exceedStockSku = getExceedStockSku(quantitySkuMap, stockSkuMap);
    // 6. Xử lý lỗi vượt quá tồn kho khi request = null
    if (!exceedStockSku.isEmpty() && request == null)
      throw new BusinessException(MessageCode.INSUFFICIENT_STOCK, exceedStockSku);
    // 7. Xử lý lỗi vượt quá tồn kho khi request !=null
    if (!exceedStockSku.isEmpty()) {
      List<Product_Sku> skuList =
          productSkuRepository.findSkuByIds(new ArrayList<>(exceedStockSku.keySet()));
      List<ExceedStockSkuDTO> exceedStockSkuDTO =
          skuList.stream()
              .map(sku ->
                  new ExceedStockSkuDTO(toProductSkuDTO(sku), exceedStockSku.get(sku.getId())))
              .toList();
      throw new BusinessException(MessageCode.INSUFFICIENT_STOCK, exceedStockSkuDTO);
    }
  }

  private Map<Integer, Integer> getExceedStockSku(
      Map<Integer, Integer> quantitySkuMap, Map<Integer, Integer> stockSkuMap) {
    return quantitySkuMap.entrySet().stream()
        .filter(e -> e.getValue() > stockSkuMap.getOrDefault(e.getKey(), 0))
        .collect(Collectors.toMap(Map.Entry::getKey, e -> stockSkuMap.getOrDefault(e.getKey(), 0)));
  }

  private ProductSkuDTO toProductSkuDTO(Product_Sku sku) {
    ProductSkuDTO productSkuDTO = new ProductSkuDTO();
    productSkuDTO.setSkuCode(sku.getSku_code());
    productSkuDTO.setAttribute(sku.getAttribute());
    productSkuDTO.setPrice(sku.getPrice());
    productSkuDTO.setStock(sku.getStock());
    productSkuDTO.setStatus(sku.getStatus());
    productSkuDTO.setTypeProduct(sku.getType_product());
    productSkuDTO.setWeightGram(sku.getWeight_gram());
    productSkuDTO.setHeightCm(sku.getHeight_cm());
    productSkuDTO.setLengthCm(sku.getLength_cm());
    return productSkuDTO;
  }

  // Lấy số lượng đặt mua
  public Map<Integer, Integer> getQuantitySkuMapFromRequest(CheckoutPreviewRequest request) {
    return request.getItem().stream()
        .collect(
            Collectors.toMap(
                CheckoutPreviewItem::getSku_id, CheckoutPreviewItem::getQuantity, Integer::sum));
  }

  // Lấy sl tồn kho của ừng SKU
  public Map<Integer, Integer> getStockSkuMap(List<CampaignProductResponse> campainProductInfo) {
    return campainProductInfo.stream()
        .collect(
            Collectors.toMap(
                CampaignProductResponse::getSkuId, CampaignProductResponse::getStockCampaign));
  }

  public void checkCampaignExpired(
      List<CampainResponse> campainInfo,
      List<CampaignProductResponse> campaignProductInfos,
      CheckoutPreviewRequest request) {
    // Check time campain
    List<CampainResponse> expiredcampaign =
        campainInfo.stream()
            .filter(campaign -> campaignPort.isCampaignExpried(campaign.getEndTime()))
            .toList();
    if (!expiredcampaign.isEmpty()) {
      Set<Integer> expiredCampaignIds =
          expiredcampaign.stream().map(CampainResponse::getId).collect(Collectors.toSet());

      List<Integer> expriredSkuIds =
          campaignProductInfos.stream()
              .filter(productInfo -> expiredCampaignIds.contains(productInfo.getCampaignId()))
              .map(CampaignProductResponse::getSkuId)
              .distinct()
              .toList();

      if (!expiredCampaignIds.isEmpty() && request == null) {
        throw new BusinessException(MessageCode.CAMPAIGN_INACTIVE);
      }
    }
  }

  public Set<Integer> getuniqueCampaignIdsInCheckoutPreviewRequest(CheckoutPreviewRequest request) {
    return request.getItem().stream()
        .map(CheckoutPreviewItem::getCampaign_id)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  public void validateRequest(CheckoutPreviewRequest request) {
    // 1. request null hoặc rỗng -> tạo 1 hàm trả về gi trị rỗng
  }

  private record SkuCampaignKey(Integer skuId, Integer campaignId) {}

  // TODO Check lại thiếu cartID
  private record ItemSelection(
      Integer productId, Integer skuId, Integer campaignId, Integer quantity) {}
}
