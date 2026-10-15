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
import com.vt.cms.model.resp.CampainResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public CampainServiceImpl(
            CampainRepository campainRepository,
            CampainProductRepository campainProductRepository,
            ProductSkuRepository productSkuRepository, ScheduledTaskHolder scheduledTaskHolder) {

        this.campainRepository = campainRepository;
        this.campainProductRepository = campainProductRepository;
        this.productSkuRepository = productSkuRepository;
        this.scheduledTaskHolder = scheduledTaskHolder;
    }

    /**
     * Tạo campaign và lưu thông tin SKU tương ứng.
     *
     * Toàn bộ thao tác ghi dữ liệu phải rollback nếu một SKU không hợp lệ.
     */
    @Override
    @Transactional
    public void createdCampain(CreatedCampainRequest request) {
        log.info("Start creating campaign");

        // 1. Kiểm tra dữ liệu đầu vào trước khi ghi DB.
        validateRequest(request);

        // 2. Tạo entity campaign từ request.
        Campain campaign = buildCampaign(request);

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
     * Kiểm tra các dữ liệu bắt buộc trước khi bắt đầu ghi dữ liệu.
     */
    private void validateRequest(CreatedCampainRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is required");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Tên chiến dịch không được để trống");
        }

        if (request.getTotal_stock() == null || request.getTotal_stock() < 0) {
            throw new IllegalArgumentException("Tổng tồn kho chiến dịch phải >= 0");
        }

        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new IllegalArgumentException(
                    "Start time and end time are required"
            );
        }

        // Cho phép startTime bằng endTime theo quy tắc nghiệp vụ hiện tại.
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException(
                    "Start time must be before or equal to end time"
            );
        }

        validateDiscount(request.getDiscount());

        if (request.getProducts() == null || request.getProducts().isEmpty()) {
            throw new IllegalArgumentException(
                    "Campaign must contain at least one product"
            );
        }

        // Không cho phép cùng một SKU xuất hiện nhiều lần trong request.
        Set<Integer> requestedSkuIds = new HashSet<>();

        for (CampainProductRequest product : request.getProducts()) {
            if (product == null || product.getProductid() <= 0) {
                throw new IllegalArgumentException(
                        "Product ID must be valid"
                );
            }

            if (product.getSkus() == null || product.getSkus().isEmpty()) {
                throw new IllegalArgumentException(
                        "Each campaign product must contain at least one SKU"
                );
            }

            for (CampainProductSku sku : product.getSkus()) {
                if (sku == null || sku.getSkuId() == null) {
                    throw new IllegalArgumentException(
                            "SKU ID is required"
                    );
                }

                if (!requestedSkuIds.add(sku.getSkuId())) {
                    throw new IllegalArgumentException(
                            "Duplicate SKU ID: " + sku.getSkuId()
                    );
                }
            }
        }
    }

    /**
     * Discount phải là số nguyên và không vượt quá 100%.
     */
    private void validateDiscount(BigDecimal discount) {
        if (discount == null) {
            throw new IllegalArgumentException("Discount is required");
        }

        if (discount.stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException(
                    "Discount must be an integer"
            );
        }

        if (discount.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException(
                    "Discount must not exceed 100%"
            );
        }
    }

    /**
     * Chuyển dữ liệu request thành entity campaign.
     */
    private Campain buildCampaign(CreatedCampainRequest request) {
        Campain campaign = new Campain();
        campaign.setName(request.getName());
        campaign.setDescription(request.getDescription());
        campaign.setStartTime(request.getStartTime());
        campaign.setEndTime(request.getEndTime());
        campaign.setDiscount(request.getDiscount());
        campaign.setTotalStock(request.getTotal_stock());
        campaign.setStatus(CampainStatus.PENDING_APPROVAL);
        campaign.setCreatedAt(LocalDateTime.now());
        campaign.setCreatedBy(SYSTEM_USER);
        campaign.setIsDeleted(NOT_DELETED);
        return campaign;
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
    public void updatestatus(Integer campain_id, CampainStatus status) {
        if (campain_id == null || campain_id <= 0 || status == null) {
            throw new IllegalArgumentException("Campaign ID và trạng thái là bắt buộc");
        }
        Campain campaign = campainRepository.getCampainById(campain_id);
        if (campaign == null || "true".equalsIgnoreCase(campaign.getIsDeleted())) {
            throw new IllegalArgumentException("Không tìm thấy chiến dịch: " + campain_id);
        }
        if (campaign.getStatus() != CampainStatus.PENDING_APPROVAL
                || (status != CampainStatus.APPROVED && status != CampainStatus.REJECTED)) {
            throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
        }
        int rows = campainRepository.updatestatus(campain_id, status);
        if (rows != 1) throw new BusinessException(MessageCode.CAMPAIN_STATUS_INVALID);
    }
    @Override
    public List<CampainResponse> getlist() {
        return campainRepository.getlistcampain();
    }

    @Override
    public void deletecampain(Integer campainID) {
        if (campainID == null || campainID <= 0) throw new IllegalArgumentException("Campaign ID không hợp lệ");
        int rows = campainRepository.deletecampain(campainID);
        if (rows != 1) throw new IllegalStateException("Không thể xóa chiến dịch: chiến dịch không tồn tại hoặc trạng thái không cho phép");
    }
}