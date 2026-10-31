package com.vt.cms.service.Impl;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.dto.CampainProductRequest;
import com.vt.cms.model.dto.CampainProductSku;
import com.vt.cms.model.dto.CreatedCampainRequest;
import com.vt.cms.model.entity.Campain;
import com.vt.cms.model.entity.CampainProduct;
import com.vt.cms.model.entity.Product_Sku;
import com.vt.cms.model.enums.CampainStatus;
import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.repository.CampainProductRepository;
import com.vt.cms.model.repository.CampainRepository;
import com.vt.cms.model.repository.ProductSkuRepository;
import com.vt.cms.service.CampainService;
import com.vt.cms.service.validation.CampaignRequestValidator;
import com.vt.cms.service.mapper.CampaignEntityMapper;
import com.vt.cms.model.resp.CampainResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CampainServiceImpl implements CampainService {

    private static final String SYSTEM_USER = "SYSTEM";
    private static final String ACTIVE_STATUS = "ACTIVE";
    private static final String NOT_DELETED = "false";

    private final CampainRepository campainRepository;
    private final CampainProductRepository campainProductRepository;
    private final ProductSkuRepository productSkuRepository;
    private final ScheduledTaskHolder scheduledTaskHolder;
    private final CampaignRequestValidator campaignRequestValidator;
    private final CampaignEntityMapper campaignEntityMapper;

    public CampainServiceImpl(
            CampainRepository campainRepository,
            CampainProductRepository campainProductRepository,
            ProductSkuRepository productSkuRepository, ScheduledTaskHolder scheduledTaskHolder,
            CampaignRequestValidator campaignRequestValidator,
            CampaignEntityMapper campaignEntityMapper) {

        this.campainRepository = campainRepository;
        this.campainProductRepository = campainProductRepository;
        this.productSkuRepository = productSkuRepository;
        this.scheduledTaskHolder = scheduledTaskHolder;
        this.campaignRequestValidator = campaignRequestValidator;
        this.campaignEntityMapper = campaignEntityMapper;
    }

    /**
     * Tạo campaign và lưu thông tin SKU tương ứng.
     *
     * Toàn bộ thao tác ghi dữ liệu phải rollback nếu một SKU không hợp lệ.
     */
    @Override
    @Transactional
    /**
     * Tạo mới dữ liệu created campain. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void createdCampain(CreatedCampainRequest request) {
        log.info("Start creating campaign");

        // 1. Kiểm tra dữ liệu đầu vào trước khi ghi DB.
        campaignRequestValidator.validate(request);

        // 2. Tạo entity campaign từ request.
        Campain campaign = campaignEntityMapper.toNewCampaign(request);

        // 3. Lưu campaign trước để lấy ID cho bảng chi tiết.
        campainRepository.savecampain(campaign);

        Integer campaignId = campaign.getCampainid();
        if (campaignId == null) {
            log.error(
                    "Failed to create campaign: generated campaign ID is null"
            );
            throw new IllegalStateException(
                    "Campaign ID was not generated after saving"
            );
        }

        log.info(
                "Campaign created. campaignId={}, productCount={}",
                campaignId,
                request.getProducts().size()
        );

        // 4. Validate và lưu chi tiết từng SKU.
        int savedSkuCount = 0;

        for (CampainProductRequest productRequest : request.getProducts()) {
            savedSkuCount += saveProductSkus(campaignId, productRequest);
        }

        log.info(
                "Campaign creation completed. campaignId={}, savedSkuCount={}",
                campaignId,
                savedSkuCount
        );
    }

    /**
     * Kiểm tra SKU thuộc đúng product, đang ACTIVE và có đủ tồn kho.
     * Mỗi SKU được lưu thành một entity CampainProduct riêng biệt.
     */
    private int saveProductSkus(
            Integer campaignId,
            CampainProductRequest productRequest) {

        Integer productId = productRequest.getProductid();

        List<Product_Sku> productSkus =
                productSkuRepository.findByProductId(productId);

        if (productSkus == null || productSkus.isEmpty()) {
            log.warn(
                    "No SKU found for product. campaignId={}, productId={}",
                    campaignId,
                    productId
            );
            throw new BusinessException(MessageCode.PRODUCT_SKU_NOT_FOUND);
        }

        int savedCount = 0;

        for (CampainProductSku skuRequest : productRequest.getSkus()) {
            Product_Sku sku = findSku(productSkus, skuRequest.getSkuId());

            validateSkuStatus(sku, campaignId);
            validateCampaignStock(sku, skuRequest, campaignId);

            CampainProduct campaignProduct =
                    buildCampaignProduct(
                            campaignId,
                            productId,
                            sku,
                            skuRequest
                    );

            campainProductRepository.savecampainproduct(campaignProduct);
            savedCount++;

            log.debug(
                    "Campaign SKU saved. campaignId={}, productId={}, skuId={}, stock={}",
                    campaignId,
                    productId,
                    sku.getId(),
                    skuRequest.getStock_campaign()
            );
        }

        return savedCount;
    }

    /**
     * Tìm SKU trong danh sách của product hiện tại.
     * Không chỉ kiểm tra SKU tồn tại, mà còn đảm bảo SKU thuộc đúng product.
     */
    private Product_Sku findSku(
            List<Product_Sku> productSkus,
            Integer skuId) {

        return productSkus.stream()
                .filter(sku -> skuId.equals(sku.getId()))
                .findFirst()
                .orElseThrow(() ->
                        new BusinessException(
                                MessageCode.PRODUCT_SKU_NOT_FOUND
                        )
                );
    }

    /**
     * Kiểm tra tính hợp lệ validate sku status. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    private void validateSkuStatus(
            Product_Sku sku,
            Integer campaignId) {

        if (!ACTIVE_STATUS.equalsIgnoreCase(sku.getStatus())) {
            log.warn(
                    "Inactive SKU rejected. campaignId={}, skuId={}, status={}",
                    campaignId,
                    sku.getId(),
                    sku.getStatus()
            );

            throw new BusinessException(MessageCode.SKU_NOT_ACTIVE);
        }
    }

    /**
     * Kiểm tra tính hợp lệ validate campaign stock. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    private void validateCampaignStock(
            Product_Sku sku,
            CampainProductSku skuRequest,
            Integer campaignId) {

        Integer campaignStock = skuRequest.getStock_campaign();
        Integer availableStock = sku.getStock();

        if (campaignStock == null || campaignStock < 0) {
            log.warn(
                    "Invalid campaign stock. campaignId={}, skuId={}, requestedStock={}",
                    campaignId,
                    sku.getId(),
                    campaignStock
            );

            throw new BusinessException(MessageCode.STOCK_INVALID);
        }

        if (availableStock == null || campaignStock > availableStock) {
            log.warn(
                    "Insufficient stock. campaignId={}, skuId={}, requestedStock={}, availableStock={}",
                    campaignId,
                    sku.getId(),
                    campaignStock,
                    availableStock
            );

            throw new BusinessException(MessageCode.STOCK_INSUFFICIENT);
        }
    }

    /**
     * Lưu giá gốc và giá bán cuối cùng theo từng SKU từ request.
     */
    private CampainProduct buildCampaignProduct(
            Integer campaignId,
            Integer productId,
            Product_Sku sku,
            CampainProductSku skuRequest) {

        CampainProduct campaignProduct = new CampainProduct();
        campaignProduct.setCampaignId(campaignId);
        campaignProduct.setProductId(productId);
        campaignProduct.setSkuId(sku.getId());
        campaignProduct.setPrice(skuRequest.getPrice());
        campaignProduct.setPrice_discount(skuRequest.getDiscountPrice());
        campaignProduct.setStockCampaign(skuRequest.getStock_campaign());
        campaignProduct.setStatus(skuRequest.getStatus());
        campaignProduct.setCreatedAt(LocalDateTime.now());
        campaignProduct.setCreatedBy(SYSTEM_USER);
        campaignProduct.setIsDeleted(NOT_DELETED);

        return campaignProduct;
    }
    @Override
    /**
     * Cập nhật dữ liệu updatestatus. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void updatestatus(Integer campain_id, CampainStatus status) {
        if (campain_id == null || campain_id <= 0 || status == null) {
            throw new BusinessException(MessageCode.CAMPAIGN_ID_VA_TRANG_THAI_LA_BAT_BUOC);
        }
        Campain campaign = campainRepository.getCampainById(campain_id);
        if (campaign == null || "true".equalsIgnoreCase(campaign.getIsDeleted())) {
            throw new BusinessException(MessageCode.CAMPAIGN_NOT_FOUND);
        }
        if (campaign.getStatus() != CampainStatus.PENDING_APPROVAL
                || (status != CampainStatus.APPROVED && status != CampainStatus.REJECTED)) {
            throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
        }
        int rows = campainRepository.updatestatus(campain_id, status);
        if (rows != 1) throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
    }
    @Override
    /**
     * Lấy dữ liệu getlist. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public List<CampainResponse> getlist() {
        return campainRepository.getlistcampain();
    }

    @Override
    /**
     * Xóa dữ liệu deletecampain. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void deletecampain(Integer campainID) {
        if (campainID == null || campainID <= 0) throw new BusinessException(MessageCode.CAMPAIGN_ID_KHONG_HOP_LE);
        int rows = campainRepository.deletecampain(campainID);
        if (rows != 1) throw new IllegalStateException("Không thể xóa chiến dịch: chiến dịch không tồn tại hoặc trạng thái không cho phép");
    }
}