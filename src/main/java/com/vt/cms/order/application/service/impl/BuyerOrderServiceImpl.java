package com.vt.cms.order.application.service.impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.base.enums.MessageCode;
import com.vt.cms.campain.application.dto.response.CampaignProductResponse;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import com.vt.cms.campain.application.service.port.CampaignProductPort;
import com.vt.cms.campain.domain.enity.CampainProduct;
import com.vt.cms.campain.domain.repository.CampainProductRepository;
import com.vt.cms.model.dto.ItemRequest;
import com.vt.cms.order.application.dto.request.CheckoutPreviewItem;
import com.vt.cms.order.application.dto.request.CheckoutPreviewRequest;
import com.vt.cms.order.application.dto.response.CheckoutPreviewResponse;
import com.vt.cms.order.application.dto.response.ExceedStockSkuDTO;
import com.vt.cms.order.application.dto.response.ProductSkuDTO;
import com.vt.cms.order.application.dto.response.SkuPreviewDto;
import com.vt.cms.order.application.service.BuyerOrderService;
import com.vt.cms.order.application.service.port.CampaignPort;
import com.vt.cms.payment.domain.enums.PaymentMethod;
import com.vt.cms.product.application.dto.response.SkuResponse;
import com.vt.cms.product.domain.entity.Product_Sku;
import com.vt.cms.product.domain.repository.ProductSkuRepository;
import java.math.BigDecimal;
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

  public BuyerOrderServiceImpl(
      CampaignProductPort campaignProductPort,
      CampaignPort campaignPort,
      ProductSkuRepository productSkuRepository,
      CampainProductRepository campainProductRepository) {
    this.campaignProductPort = campaignProductPort;
    this.campaignPort = campaignPort;
    this.productSkuRepository = productSkuRepository;
    this.campainProductRepository = campainProductRepository;
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
    System.out.println("C" + campaignProductInfos);
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
    System.out.println("-----------------------------------------------");
    List<SkuResponse> skuInfos = loadSkuInfosForSelections(selections);
    System.out.println("P" + skuInfos);
    System.out.println("-----------------------------------------------");
    System.out.println(skuInfos.stream().map(SkuResponse::getId).toList());
    System.out.println("-----------------------------------------------");
    // 8. Map Campaign Product
    Map<SkuCampaignKey, CampainProduct> campaignProductMap = loadCampaignProductMap(selections);
    // 9. Xử lý thông tin SKU để build response

    // 9. Build SKU Preview
    List<SkuPreviewDto> skus = buildSkuResponses(selections, skuInfos, campaignProductMap);
    // 10. Build response
    CheckoutPreviewResponse response = new CheckoutPreviewResponse();
    response.setPaymentMethod(PaymentMethod.QR);
    response.setSkus(skus);
    response.setTotalPrice(BigDecimal.valueOf(100));
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
          response.setProduct_id(sku.getProductId());
          response.setProduct_name(sku.getProductName());

          // Giá bán
          if (campaignProduct != null) {
            response.setPrice(campaignProduct.getPrice());
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

  public void processItem(ItemRequest item) {}

  private record SkuCampaignKey(Integer skuId, Integer campaignId) {}

  // TODO Check lại thiếu cartID
  private record ItemSelection(
      Integer productId, Integer skuId, Integer campaignId, Integer quantity) {}
}
