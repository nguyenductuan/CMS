package com.vt.cms.campain.application.service.Impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.base.enums.MessageCode;
import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.base.response.BaseResponse;
import com.vt.cms.campain.application.dto.request.CampainProductRequest;
import com.vt.cms.campain.application.dto.request.CampainProductSku;
import com.vt.cms.campain.application.dto.request.CreatedCampainRequest;
import com.vt.cms.campain.application.dto.response.CampaignProductGroupResponse;
import com.vt.cms.campain.application.dto.response.CampaignProductResponse;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import com.vt.cms.campain.application.service.CampainService;
import com.vt.cms.campain.application.service.mapper.CampaignEntityMapper;
import com.vt.cms.campain.application.service.port.CampaignProductPort;
import com.vt.cms.campain.application.service.validation.CampaignRequestValidator;
import com.vt.cms.campain.domain.enity.Campain;
import com.vt.cms.campain.domain.enity.CampainProduct;
import com.vt.cms.campain.domain.enums.CampaignAction;
import com.vt.cms.campain.domain.enums.CampainStatus;
import com.vt.cms.campain.domain.repository.CampainProductRepository;
import com.vt.cms.campain.domain.repository.CampainRepository;
import com.vt.cms.model.dto.page.PageInfo;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.product.domain.entity.Product_Sku;
import com.vt.cms.product.domain.repository.ProductSkuRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CampainServiceImpl implements CampainService {

  private static final String ACTIVE_STATUS = "ACTIVE";
  private static final String NOT_DELETED = "false";

  private final CampainRepository campainRepository;
  private final CampainProductRepository campainProductRepository;
  private final ProductSkuRepository productSkuRepository;
  private final CampaignRequestValidator campaignRequestValidator;
  private final CampaignEntityMapper campaignEntityMapper;
  private final CampaignProductPort campaignProductPort;

  public CampainServiceImpl(
      CampainRepository campainRepository,
      CampainProductRepository campainProductRepository,
      ProductSkuRepository productSkuRepository,
      CampaignRequestValidator campaignRequestValidator,
      CampaignEntityMapper campaignEntityMapper,
      CampaignProductPort campaignProductPort) {

    this.campainRepository = campainRepository;
    this.campainProductRepository = campainProductRepository;
    this.productSkuRepository = productSkuRepository;
    this.campaignRequestValidator = campaignRequestValidator;
    this.campaignEntityMapper = campaignEntityMapper;
    this.campaignProductPort = campaignProductPort;
  }

  @Override
  @Transactional
  /* Tạo mới chiến dịch  */
  public void createdCampain(CreatedCampainRequest request, Integer userId) {
    log.info("Start creating campaign");
    // 1. Kiểm tra dữ liệu đầu vào trước khi ghi DB.
    campaignRequestValidator.validate(request);
    // 2. Tạo entity campaign từ request.
    Campain campaign = campaignEntityMapper.toNewCampaign(request, userId);
    // 3. Lưu campaign trước để lấy ID cho bảng chi tiết.
    campainRepository.savecampain(campaign);
    Integer campaignId = campaign.getCampaignId();
    if (campaignId == null) {
      log.error("Failed to create campaign: generated campaign ID is null");
      throw new IllegalStateException("Campaign ID was not generated after saving");
    }
    log.info(
        "Campaign created. campaignId={}, productCount={}",
        campaignId,
        request.getProducts().size());
    // 4. Validate và lưu chi tiết từng SKU.
    int savedSkuCount = 0;
    for (CampainProductRequest productRequest : request.getProducts()) {
      savedSkuCount += saveProductSkus(campaignId, productRequest);
    }
    // 5. Lấy danh sách SKU vừa tạo
    List<CampaignProductResponse> campaignProducts =
        campainProductRepository.findCampainProductById(campaignId);
    // 6. Kiểm tra tồn kho và trừ kho
    updateStockSku(campaignProducts, true);
    // TODO: Check số lượng bán trong chiến diịch <= sl của từng sku trong kho
    log.info(
        "Campaign creation completed. campaignId={}, savedSkuCount={}", campaignId, savedSkuCount);
  }

  private void updateStockSku(List<CampaignProductResponse> campaignProducts, boolean isDecrease) {

    Map<Integer, Integer> skuDeltaMap = new HashMap<>();

    for (CampaignProductResponse cp : campaignProducts) {

      if (cp.getStatus() != null && cp.getStatus() == CampainStatus.ACTIVE) {

        Integer skuId = cp.getSkuId();
        int stock = cp.getStockCampaign() != null ? cp.getStockCampaign() : 0;

        if (skuId != null && stock > 0) {

          int delta = isDecrease ? -stock : stock;

          skuDeltaMap.merge(skuId, delta, Integer::sum);
        }
      }
    }
    System.out.println("A" + skuDeltaMap.keySet());
    System.out.println("B" + skuDeltaMap);

    if (!skuDeltaMap.isEmpty()) {
      campaignProductPort.updateStockSkus(skuDeltaMap.keySet(), skuDeltaMap);
    }
  }

  private int saveProductSkus(Integer campaignId, CampainProductRequest productRequest) {

    Integer productId = productRequest.getProductid();

    List<Product_Sku> productSkus = productSkuRepository.findByProductId(productId);

    if (productSkus == null || productSkus.isEmpty()) {
      log.warn("No SKU found for product. campaignId={}, productId={}", campaignId, productId);
      throw new BusinessException(MessageCode.PRODUCT_SKU_NOT_FOUND);
    }

    int savedCount = 0;

    for (CampainProductSku skuRequest : productRequest.getSkus()) {
      Product_Sku sku = findSku(productSkus, skuRequest.getSkuId());

      validateSkuStatus(sku, campaignId);
      validateCampaignStock(sku, skuRequest, campaignId);

      CampainProduct campaignProduct = buildCampaignProduct(campaignId, productId, sku, skuRequest);

      campainProductRepository.savecampainproduct(campaignProduct);
      savedCount++;

      log.debug(
          "Campaign SKU saved. campaignId={}, productId={}, skuId={}, stock={}",
          campaignId,
          productId,
          sku.getId(),
          skuRequest.getStock_campaign());
    }

    return savedCount;
  }

  /* Tìm SKU trong danh sách của product hiện tại */
  private Product_Sku findSku(List<Product_Sku> productSkus, Integer skuId) {

    return productSkus.stream()
        .filter(sku -> skuId.equals(sku.getId()))
        .findFirst()
        .orElseThrow(() -> new BusinessException(MessageCode.PRODUCT_SKU_NOT_FOUND));
  }

  private void validateSkuStatus(Product_Sku sku, Integer campaignId) {

    if (CampainStatus.ACTIVE != (sku.getStatus())) {
      log.warn(
          "Inactive SKU rejected. campaignId={}, skuId={}, status={}",
          campaignId,
          sku.getId(),
          sku.getStatus());

      throw new BusinessException(MessageCode.SKU_NOT_ACTIVE);
    }
  }

  private void validateCampaignStock(
      Product_Sku sku, CampainProductSku skuRequest, Integer campaignId) {

    Integer campaignStock = skuRequest.getStock_campaign();
    Integer availableStock = sku.getStock();

    if (campaignStock == null || campaignStock < 0) {
      log.warn(
          "Invalid campaign stock. campaignId={}, skuId={}, requestedStock={}",
          campaignId,
          sku.getId(),
          campaignStock);

      throw new BusinessException(MessageCode.STOCK_INVALID);
    }

    if (availableStock == null || campaignStock > availableStock) {
      log.warn(
          "Insufficient stock. campaignId={}, skuId={}, requestedStock={}, availableStock={}",
          campaignId,
          sku.getId(),
          campaignStock,
          availableStock);

      throw new BusinessException(MessageCode.STOCK_INSUFFICIENT);
    }
  }

  /** Lưu giá gốc và giá bán cuối cùng theo từng SKU từ request. */
  private CampainProduct buildCampaignProduct(
      Integer campaignId, Integer productId, Product_Sku sku, CampainProductSku skuRequest) {

    CampainProduct campaignProduct = new CampainProduct();
    campaignProduct.setCampaignId(campaignId);
    campaignProduct.setProductId(productId);
    campaignProduct.setSkuId(sku.getId());
    campaignProduct.setPrice(skuRequest.getPrice());
    campaignProduct.setPriceDiscount(skuRequest.getDiscountPrice());
    campaignProduct.setStockCampaign(skuRequest.getStock_campaign());
    campaignProduct.setStatus(skuRequest.getStatus());
    campaignProduct.setCreatedAt(LocalDateTime.now());
    //    campaignProduct.setCreatedBy(SYSTEM_USER);
    campaignProduct.setIsDeleted(NOT_DELETED);

    return campaignProduct;
  }

  @Override
  /** Cập nhật dữ liệu updatestatus */
  public void updatestatus(Integer campain_id, CampainStatus status, Integer userId) {
    // Kiểm tra dữ liệu đầu vào
    if (campain_id == null || campain_id <= 0 || status == null) {
      throw new BusinessException(MessageCode.CAMPAIGN_ID_VA_TRANG_THAI_LA_BAT_BUOC);
    }
    Campain campaign = campainRepository.getCampainById(campain_id);
    // Kiểm tra chiến dịch tồn tại và chưa bị xóa
    if (campaign == null || "true".equalsIgnoreCase(campaign.getIsDeleted())) {
      throw new BusinessException(MessageCode.CAMPAIGN_NOT_FOUND);
    }
    // Chỉ được duyệt hoặc từ chối đối với chiến dịch trạng tha Chờ duyệt
    // TODO: Cần xem xét lại logic này
    CampainStatus currentStatus = campaign.getStatus();
    boolean isValid =
        (currentStatus == CampainStatus.PENDING
            && (status == CampainStatus.ACTIVE || status == CampainStatus.REJECTED));

    if (!isValid) {
      throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
    }
    //    int rows = campainRepository.savecampain(campain_id, status, userId);
    // ToDO: Có thể dùng 1 hàm save campain để có thể chung sửa campain
    int rows = campainRepository.updatestatus(campain_id, status, userId);
    if (rows != 1) throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
  }

  @Override
  public CampainResponse deaitlCampain(Integer campain_id) {
    Campain campain = campainRepository.getCampainById(campain_id);

    List<CampaignProductResponse> campainProduct =
        campainProductRepository.findCampainProductById(campain_id);
    // Nhóm SKU theo productId
    Map<Integer, List<CampaignProductResponse>> groupedProducts =
        campainProduct.stream()
            .collect(
                Collectors.groupingBy(
                    CampaignProductResponse::getProductId,
                    LinkedHashMap::new,
                    Collectors.toList()));

    // Chuyển thành danh sách products
    List<CampaignProductGroupResponse> products =
        groupedProducts.entrySet().stream()
            .map(entry -> {
              CampaignProductGroupResponse product = new CampaignProductGroupResponse();
              CampaignProductResponse first = entry.getValue().get(0);

              product.setProductId(entry.getKey());
              product.setProductName(first.getProductName());
              product.setCampaignProduct(entry.getValue());
              return product;
            })
            .toList();

    CampainResponse campainResponse = new CampainResponse();
    campainResponse.setDescription(campain.getDescription());

    campainResponse.setName(campain.getName());
    campainResponse.setId(campain_id);
    campainResponse.setProduct(products);
    campainResponse.setStatus(campain.getStatus());
    campainResponse.setDiscount(campain.getDiscount());
    campainResponse.setStartTime(campain.getStartTime());
    campainResponse.setEndTime(campain.getEndTime());
    campainResponse.setAvailableActions(getAvailableActions(campain.getStatus()));

    return campainResponse;
  }

  public List<CampaignAction> getAvailableActions(CampainStatus status) {
    return switch (status) {
      case PENDING ->
          List.of(CampaignAction.CANCEL_APPROVAL, CampaignAction.EDIT, CampaignAction.VIEW_DETAIL);
      case ACTIVE -> List.of(CampaignAction.VIEW_DETAIL, CampaignAction.STOP);
      case REJECTED -> List.of(CampaignAction.VIEW_DETAIL);
      case FINISHED -> List.of(CampaignAction.REPOST, CampaignAction.VIEW_DETAIL);
      case CANCELLED -> List.of(CampaignAction.VIEW_DETAIL);
    };
  }

  @Override
  /** Lấy dữ liệu getlist */
  public BaseResponse<PagingResponse<List<CampainResponse>>> getlist(Pagerequest request) {
    if (request == null) throw new BusinessException(MessageCode.PAGE_NOT_FOUND);
    if (request.getPageNo() == null || request.getPageNo() < 0)
      throw new BusinessException(MessageCode.PAGE_NO_MUST_BE_GREATER_THAN_OR_EQUAL_TO_0);
    if (request.getPageSize() == null || request.getPageSize() < 1 || request.getPageSize() > 100)
      throw new BusinessException(MessageCode.PAGE_SIZE_MUST_BE_BETWEEN_1_AND_100);
    List<CampainResponse> campainResponses = campainRepository.getlistcampain(request);
    // Lấy thông tin action theo trạng thaái chiến dịch
    for (CampainResponse item : campainResponses) {
      item.setAvailableActions(getAvailableActions(item.getStatus()));
    }
    // Tinhs số lượng bản ghi
    long totalRecords = campainRepository.countCampains(request);
    long totalpage = (int) Math.ceil((double) totalRecords / request.getPageSize());

    // page info
    PageInfo pageInfo = new PageInfo();
    pageInfo.setPageNo(request.getPageNo());
    pageInfo.setPageSize(request.getPageSize());
    pageInfo.setTotalCount(totalRecords);
    pageInfo.setTotalPage(totalpage);
    // paging response
    PagingResponse<List<CampainResponse>> pagingResponse = new PagingResponse<>();
    pagingResponse.setPageInfo(pageInfo);
    pagingResponse.setData(campainResponses);
    // base response
    BaseResponse<PagingResponse<List<CampainResponse>>> response = new BaseResponse<>();
    response.setMessage("Successful!");
    response.setData(pagingResponse);
    return response;
  }

  @Override
  /* Xóa dữ liệu deletecampain */
  public void deletecampain(Integer campaignId, Integer userId) {
    if (campaignId == null || campaignId <= 0)
      throw new BusinessException(MessageCode.CAMPAIGN_ID_KHONG_HOP_LE);
    // Chỉ cho phép xóa chiến dịch đang chờ duyệt
    Campain campaign = campainRepository.getCampainById(campaignId);
    CampainStatus currentStatus = campaign.getStatus();
    if (currentStatus != CampainStatus.PENDING) {
      throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
    }
    int rows = campainRepository.deletecampain(campaignId, userId);
    if (rows != 1) throw new BusinessException(MessageCode.CAMPAIN_DELETE_FAILED);
    // Cộng lại kho khi xóa chiến dịch
    List<CampaignProductResponse> campaignProducts =
        campainProductRepository.findCampainProductById(campaignId);
    updateStockSku(campaignProducts, false);
  }

  public void finishCampain(Integer campaignId, Integer userId) {
    /**
     * Cập nhật trnajg thái campain thành FINISH Lấy danh sách đơn hàng c trạng thái Chờ thanh toán
     * Lấy danh sách đơn hàng Chờ giao, Đang giao, Đã giao, Dã hoàn thành Hủy đơn haàng đối với đơn
     * chờ thaành toán (tác nhân là Seller hoặc role người kết thuc chiến dịch)
     */
    // Kiểm tra dữ liệu đầu vào
    if (campaignId == null || campaignId <= 0) {
      throw new BusinessException(MessageCode.CAMPAIGN_ID_VA_TRANG_THAI_LA_BAT_BUOC);
    }
    Campain campaign = campainRepository.getCampainById(campaignId);
    // Kiểm tra chiến dịch tồn tại và chưa bị xóa
    if (campaign == null || "true".equalsIgnoreCase(campaign.getIsDeleted())) {
      throw new BusinessException(MessageCode.CAMPAIGN_NOT_FOUND);
    }
    CampainStatus currentStatus = campaign.getStatus();
    boolean isValid = (currentStatus == CampainStatus.ACTIVE);
    if (!isValid) {
      throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
    }
    campaign.setCampaignId(campaignId);
    campaign.setStatus(CampainStatus.FINISHED);
    campaign.setUpdatedBy(userId.toString());
    campainRepository.finishCampain(campaign);
    // Cộng lại kho khi kết thúc chiến dịch
    List<CampaignProductResponse> campaignProducts =
        campainProductRepository.findCampainProductById(campaignId);
    updateStockSku(campaignProducts, false);
    // TODO: Xử lý đơn hàng khi hủy chiến dịch
  }

  @Override
  public List<CampainResponse> getCampaignInfoById(Set<Integer> campainids) {
    return campainRepository.findAllCampainByIds(campainids).stream()
        .map(CampaignEntityMapper::toCampainInfo)
        .toList();
  }

  @Override
  public List<CampaignProductResponse> findCampaignProductByCampaignIdIn(Set<Integer> campainids) {
    return campainProductRepository.findCampaignProductByCampaignIdIn(campainids).stream()
        .map(CampaignEntityMapper::toCampainProductInfo)
        .toList();
  }

  @Override
  public boolean isCampaignExpried(LocalDateTime endtime) {
    return endtime.isBefore(LocalDateTime.now());
  }
}
