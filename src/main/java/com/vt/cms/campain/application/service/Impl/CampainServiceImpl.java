package com.vt.cms.campain.application.service.Impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.base.enums.MessageCode;
import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.base.response.BaseResponse;
import com.vt.cms.campain.application.dto.request.CampainProductRequest;
import com.vt.cms.campain.application.dto.request.CampainProductSku;
import com.vt.cms.campain.application.dto.request.CreatedCampainRequest;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import com.vt.cms.campain.application.service.CampainService;
import com.vt.cms.campain.application.service.mapper.CampaignEntityMapper;
import com.vt.cms.campain.application.service.validation.CampaignRequestValidator;
import com.vt.cms.campain.domain.enity.Campain;
import com.vt.cms.campain.domain.enity.CampainProduct;
import com.vt.cms.campain.domain.enums.CampainStatus;
import com.vt.cms.campain.domain.repository.CampainProductRepository;
import com.vt.cms.campain.domain.repository.CampainRepository;
import com.vt.cms.model.dto.page.PageInfo;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.product.domain.entity.Product_Sku;
import com.vt.cms.product.domain.repository.ProductSkuRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
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

  public CampainServiceImpl(
      CampainRepository campainRepository,
      CampainProductRepository campainProductRepository,
      ProductSkuRepository productSkuRepository,
      CampaignRequestValidator campaignRequestValidator,
      CampaignEntityMapper campaignEntityMapper) {

    this.campainRepository = campainRepository;
    this.campainProductRepository = campainProductRepository;
    this.productSkuRepository = productSkuRepository;
    this.campaignRequestValidator = campaignRequestValidator;
    this.campaignEntityMapper = campaignEntityMapper;
  }

  /**
   * Tạo campaign và lưu thông tin SKU tương ứng.
   *
   * <p>Toàn bộ thao tác ghi dữ liệu phải rollback nếu một SKU không hợp lệ.
   */
  @Override
  @Transactional
  /** Tạo mới dữ liệu created campain. Các tham số và kết quả tuân theo chữ ký phương thức. */
  public void createdCampain(CreatedCampainRequest request, Integer userId) {
    log.info("Start creating campaign");

    // 1. Kiểm tra dữ liệu đầu vào trước khi ghi DB.
    campaignRequestValidator.validate(request);

    // 2. Tạo entity campaign từ request.
    Campain campaign = campaignEntityMapper.toNewCampaign(request, userId);

    // 3. Lưu campaign trước để lấy ID cho bảng chi tiết.
    campainRepository.savecampain(campaign);

    Integer campaignId = campaign.getCampainId();
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
    // TODO: Check số lượng bán trong chiến diịch <= sl của từng sku trong kho
    log.info(
        "Campaign creation completed. campaignId={}, savedSkuCount={}", campaignId, savedSkuCount);
  }

  /**
   * Kiểm tra SKU thuộc đúng product, đang ACTIVE và có đủ tồn kho. Mỗi SKU được lưu thành một
   * entity CampainProduct riêng biệt.
   */
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

  /**
   * Tìm SKU trong danh sách của product hiện tại. Không chỉ kiểm tra SKU tồn tại, mà còn đảm bảo
   * SKU thuộc đúng product.
   */
  private Product_Sku findSku(List<Product_Sku> productSkus, Integer skuId) {

    return productSkus.stream()
        .filter(sku -> skuId.equals(sku.getId()))
        .findFirst()
        .orElseThrow(() -> new BusinessException(MessageCode.PRODUCT_SKU_NOT_FOUND));
  }

  /**
   * Kiểm tra tính hợp lệ validate sku status. Các tham số và kết quả tuân theo chữ ký phương thức.
   */
  private void validateSkuStatus(Product_Sku sku, Integer campaignId) {

    if (!ACTIVE_STATUS.equalsIgnoreCase(sku.getStatus())) {
      log.warn(
          "Inactive SKU rejected. campaignId={}, skuId={}, status={}",
          campaignId,
          sku.getId(),
          sku.getStatus());

      throw new BusinessException(MessageCode.SKU_NOT_ACTIVE);
    }
  }

  /**
   * Kiểm tra tính hợp lệ validate campaign stock. Các tham số và kết quả tuân theo chữ ký phương
   * thức.
   */
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
    campaignProduct.setPrice_discount(skuRequest.getDiscountPrice());
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
            && (status == CampainStatus.APPROVED || status == CampainStatus.REJECTED));

    if (!isValid) {
      throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
    }
    //    int rows = campainRepository.savecampain(campain_id, status, userId);
    // ToDO: Có thể dùng 1 hàm save campain để có thể chung sửa campain
    int rows = campainRepository.updatestatus(campain_id, status, userId);
    if (rows != 1) throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
  }

  @Override
  public Campain deaitlCampain(Integer campainId) {
    Campain campain = campainRepository.getCampainById(campainId);
    campain.setCampainId(campain.getCampainId());
    return campain;
  }

  @Override
  /** Lấy dữ liệu getlist */
  public BaseResponse<PagingResponse<List<CampainResponse>>> getlist(Pagerequest request) {
    if (request == null) throw new BusinessException(MessageCode.PAGE_NOT_FOUND);
    if (request.getPageNo() == null || request.getPageNo() < 0)
      throw new BusinessException(MessageCode.PAGE_NO_MUST_BE_GREATER_THAN_OR_EQUAL_TO_0);
    if (request.getPageSize() == null || request.getPageSize() < 1 || request.getPageSize() > 100)
      throw new BusinessException(MessageCode.PAGE_SIZE_MUST_BE_BETWEEN_1_AND_100);
    // Lấy danh sách chiên dịch
    List<CampainResponse> campainResponses = campainRepository.getlistcampain(request);
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
  /** Xóa dữ liệu deletecampain */
  public void deletecampain(Integer campainId, Integer userId) {
    if (campainId == null || campainId <= 0)
      throw new BusinessException(MessageCode.CAMPAIGN_ID_KHONG_HOP_LE);
    // Chỉ cho phép xóa chiến dịch đang chờ duyệt
    // TODO: Cần xem xét lại logic này
    Campain campaign = campainRepository.getCampainById(campainId);
    CampainStatus currentStatus = campaign.getStatus();
    if (currentStatus != CampainStatus.PENDING) {
      throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
    }
    int rows = campainRepository.deletecampain(campainId, userId);
    if (rows != 1) throw new BusinessException(MessageCode.CAMPAIN_DELETE_FAILED);
  }

  public void finishCampain(Integer campainId, Integer userId) {
    /**
     * Cập nhật trnajg thái campain thành FINISH Lấy danh sách đơn hàng c trạng thái Chờ thanh toán
     * Lấy danh sách đơn hàng Chờ giao, Đang giao, Đã giao, Dã hoàn thành Hủy đơn haàng đối với đơn
     * chờ thaành toán (tác nhân là Seller hoặc role người kết thuc chiến dịch)
     */
    // Kiểm tra dữ liệu đầu vào
    if (campainId == null || campainId <= 0) {
      throw new BusinessException(MessageCode.CAMPAIGN_ID_VA_TRANG_THAI_LA_BAT_BUOC);
    }
    Campain campaign = campainRepository.getCampainById(campainId);
    // Kiểm tra chiến dịch tồn tại và chưa bị xóa
    if (campaign == null || "true".equalsIgnoreCase(campaign.getIsDeleted())) {
      throw new BusinessException(MessageCode.CAMPAIGN_NOT_FOUND);
    }
    CampainStatus currentStatus = campaign.getStatus();
    boolean isValid = (currentStatus == CampainStatus.APPROVED);
    if (!isValid) {
      throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
    }
    campaign.setCampainId(campainId);
    campaign.setStatus(CampainStatus.FINISHED);
    campaign.setUpdatedBy(userId.toString());
    campainRepository.finishCampain(campaign);
    // TODO: Xử lý đơn hàng khi hủy chiến dịch
  }
}
