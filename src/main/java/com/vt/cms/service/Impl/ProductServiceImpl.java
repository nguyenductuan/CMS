package com.vt.cms.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.CreateProductRequest;
import com.vt.cms.model.dto.page.PageInfo;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.dto.product.*;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.entity.ProductDetail;
import com.vt.cms.model.entity.Product_Sku;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.model.repository.CampainProductRepository;
import com.vt.cms.model.repository.ProductSkuRepository;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.ProductDetailResponse;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.model.resp.SkuResponse;
import com.vt.cms.service.ProductService;
import com.vt.cms.service.validation.ProductRequestValidator;
import com.vt.cms.service.mapper.ProductEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.HashMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CampainProductRepository campainProductRepository;
    private final ObjectMapper objectMapper;
    private final ProductSkuRepository productSkuRepository;
    private final ProductRequestValidator productRequestValidator;
    private final ProductEntityMapper productEntityMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addproduct(CreateProductRequest request) {
        productRequestValidator.validate(request);
        log.info("Create product started. name={}, skuCount={}", request.getName(), request.getSkus().size());
        Product product = buildProduct(request);
        int rows = productRepository.insertproduct(product);
        if (rows != 1 || product.getId() == null) {
            log.error("Insert product failed. name={}, rows={}, generatedId={}", request.getName(), rows, product.getId());
            throw new IllegalStateException("Không thể tạo sản phẩm");
        }
        saveSkus(product.getId(), request.getSkus());
        log.info("Create product completed. productId={}, skuCount={}", product.getId(), request.getSkus().size());
    }

    private void mergeSkus(Integer productId, List<SkuRequest> requests) {
        Map<String, Product_Sku> existingByCode = new HashMap<>();
        for (Product_Sku existingSku : productSkuRepository.findByProductId(productId)) {
            if (existingSku.getSku_code() != null) existingByCode.put(existingSku.getSku_code(), existingSku);
        }
        for (SkuRequest request : requests) {
            Product_Sku existingSku = existingByCode.get(request.getSku_code().trim());
            if (existingSku == null) {
                saveSkus(productId, List.of(request));
                continue;
            }
            Product_Sku sku = mapSkuFields(productId, request);
            sku.setUpdated_at(LocalDateTime.now());
            sku.setUpdated_by("SYSTEM");
            int rows = productSkuRepository.updateByProductIdAndSkuCode(sku);
            if (rows != 1) {
                log.error("Update SKU failed. productId={}, skuCode={}, rows={}", productId, sku.getSku_code(), rows);
                throw new IllegalStateException("Không thể cập nhật SKU: " + sku.getSku_code());
            }
        }
    }

    /**
     * Maps fields shared by SKU creation and update. Lifecycle/audit fields remain
     * owned by the respective operation so updates do not reset creation metadata.
     */
    private Product_Sku mapSkuFields(Integer productId, SkuRequest request) {
        Product_Sku sku = new Product_Sku();
        sku.setProduct_id(productId);
        sku.setSku_code(request.getSku_code().trim());
        sku.setHeight_cm(request.getHeight_cm());
        sku.setWidth_cm(request.getWidth_cm());
        sku.setLength_cm(request.getLength_cm());
        sku.setWeight_gram(request.getWeight_gram());
        sku.setType_product(request.getType_product());
        sku.setStock(request.getStock());
        sku.setStatus(request.getStatus());
        sku.setPrice(request.getPrice());
        sku.setImage_url(request.getImage_url());
        sku.setAttribute(request.getAttribute());
        return sku;
    }

    private void saveSkus(Integer productId, List<SkuRequest> requests) {
        for (SkuRequest request : requests) {
            Product_Sku sku = mapSkuFields(productId, request);
            sku.setCreated_by("SYSTEM");
            sku.setIs_deleted("false");
            sku.setCreated_at(LocalDateTime.now());
            int rows = productSkuRepository.inserproductsku(sku);
            if (rows != 1) {
                log.error("Insert SKU failed. productId={}, skuCode={}, rows={}", productId, sku.getSku_code(), rows);
                throw new IllegalStateException("Không thể lưu SKU: " + sku.getSku_code());
            }
        }
    }
    @Override
    public ProductResponse detail(Integer product_id) {
        Product product = productRepository.detailProduct(product_id);
        if (product == null || "true".equalsIgnoreCase(product.getIs_deleted())) {
            log.warn("Product detail not found. productId={}", product_id);
            throw new IllegalArgumentException("Không tìm thấy sản phẩm: " + product_id);
        }
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setStatus(product.getStatus());
        response.setCreated_at(product.getCreated_at());
        response.setUpdated_at(product.getUpdated_at());
        response.setIs_deleted(product.getIs_deleted());
        response.setCreated_by(product.getCreated_by());
        log.debug("Loaded product detail. productId={}", product_id);
        // ========================= ATTRIBUTES =========================
        response.setAttributes(parseJson(product.getJson_attributes(), new TypeReference<List<AttributeRequest>>() {}));
        response.setAttributes_name(product.getJson_attributes_name());

        // =========================TYPE PRODUCTS=========================
        response.setType_products(parseJson(product.getType_products(),new TypeReference<List<TypeProductRequest>>() {}));
        response.setType_products_name(product.getType_products_name());
        // ======================== IMAGES =========================
        response.setImages(parseJson(product.getJson_images(),new TypeReference<List<ImageRequest>>() {}));
        // ========================= MEDIAS =========================
        response.setMedias(parseJson(product.getJson_medias(),new TypeReference<List<MediaRequest>>() {}));
        // ========================= SKU =========================
        List<Product_Sku> productSkus = productSkuRepository.findByProductId(product_id);
        List<SkuRequest> skus = productSkus.stream()
                .map(sku -> {
                    SkuRequest request = new SkuRequest();
                    request.setSku_code(sku.getSku_code());
                    request.setAttribute(sku.getAttribute());
                    request.setType_product(sku.getType_product());
                    request.setImage_url(sku.getImage_url());
                    request.setWeight_gram(sku.getWeight_gram());
                    request.setWidth_cm(sku.getWidth_cm());
                    request.setHeight_cm(sku.getHeight_cm());
                    request.setLength_cm(sku.getLength_cm());
                    request.setPrice(sku.getPrice());
                    request.setStock(sku.getStock());
                    request.setStatus(sku.getStatus());
                    request.setAttribute(sku.getAttribute());
                    request.setAttrIndex(sku.getAttrIndex());
                    return request;
                })
                .toList();
        response.setSkus(skus);
        return response;
    }
    private <T> List<T> parseJson(String json, TypeReference<List<T>> typeReference) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(json,typeReference);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    "JSON sản phẩm không hợp lệ",
                    e
            );
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editproduct(Integer id, CreateProductRequest request) {
        if (id == null || id <= 0) throw new IllegalArgumentException("ID sản phẩm không hợp lệ");
        productRequestValidator.validate(request);
        Product existing = productRepository.getProductById(id);
        if (existing == null || "true".equalsIgnoreCase(existing.getIs_deleted())) {
            log.warn("Update product rejected: not found. productId={}", id);
            throw new IllegalArgumentException("Không tìm thấy sản phẩm: " + id);
        }
        if (!"PENDING_APPROVAL".equalsIgnoreCase(existing.getStatus())
                && !"REJECTED".equalsIgnoreCase(existing.getStatus())) {
            throw new IllegalStateException("Chỉ được sửa sản phẩm đang chờ duyệt hoặc đã bị từ chối");
        }
        Product update = productEntityMapper.toNewProduct(request);
        update.setStatus("PENDING_APPROVAL");
        update.setUpdated_at(LocalDateTime.now());
        update.setUpdated_by("SYSTEM");
        int rows = productRepository.updateProduct(update, id);
        if (rows != 1) {
            log.error("Update product failed. productId={}, rows={}", id, rows);
            throw new IllegalStateException("Không thể cập nhật sản phẩm");
        }
        // Merge SKU theo sku_code để không vô tình xóa SKU đang được campaign tham chiếu.
        mergeSkus(id, request.getSkus());
        log.info("Update product completed. productId={}, requestedSkuCount={}", id, request.getSkus().size());
    }
    @Override
    //Danh sách sản phẩm
    public BaseResponse<PagingResponse<List<ProductResponse>>> listproduct(OrdersRequest request) {
        if (request == null) throw new IllegalArgumentException("Thông tin phân trang không được để trống");
        if (request.getPageNo() == null || request.getPageNo() < 0) throw new IllegalArgumentException("pageNo phải >= 0");
        if (request.getPageSize() == null || request.getPageSize() < 1 || request.getPageSize() > 100) throw new IllegalArgumentException("pageSize phải nằm trong khoảng 1..100");
        List<ProductResponse> product = productRepository.listproduct(request);
        long totalCount = productRepository.countproduct(request);
        long totalpage = (long) Math.ceil((double) totalCount / request.getPageSize());
        // page info
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNo(request.getPageNo());
        pageInfo.setPageSize(request.getPageSize());
        pageInfo.setTotalCount(totalCount);
        pageInfo.setTotalPage(totalpage);
        // paging response
        PagingResponse<List<ProductResponse>> pagingResponse = new PagingResponse<>();
        pagingResponse.setPageInfo(pageInfo);
        pagingResponse.setData(product);
        // base response
        BaseResponse<PagingResponse<List<ProductResponse>>> response = new BaseResponse<>();
        response.setMessage("Successful!");
        response.setData(pagingResponse);
        return response;
    }
    @Override
    //Cập nhật trạng thái sp
    public void editstatusproduct(Integer product_id) {
        if (product_id == null || product_id <= 0) throw new IllegalArgumentException("ID sản phẩm không hợp lệ");
        Product existing = productRepository.getProductById(product_id);
        if (existing == null || "true".equalsIgnoreCase(existing.getIs_deleted())) throw new IllegalArgumentException("Không tìm thấy sản phẩm: " + product_id);
        if (!"PENDING_APPROVAL".equalsIgnoreCase(existing.getStatus())) {
            throw new IllegalStateException("Chỉ sản phẩm đang chờ duyệt mới được duyệt");
        }
        LocalDateTime now = LocalDateTime.now();
        int rows = productRepository.editstatusproduct(product_id, "APPROVED", now, "SYSTEM", now);
        if (rows != 1) throw new IllegalStateException("Không thể duyệt sản phẩm; trạng thái có thể đã thay đổi");
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectProduct(Integer product_id) {
        if (product_id == null || product_id <= 0) throw new IllegalArgumentException("ID sản phẩm không hợp lệ");
        Product existing = productRepository.getProductById(product_id);
        if (existing == null || "true".equalsIgnoreCase(existing.getIs_deleted())) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm: " + product_id);
        }
        if (!"PENDING_APPROVAL".equalsIgnoreCase(existing.getStatus())) {
            throw new IllegalStateException("Chỉ sản phẩm đang chờ duyệt mới được từ chối");
        }
        LocalDateTime now = LocalDateTime.now();
        int rows = productRepository.editstatusproduct(product_id, "REJECTED", now, "SYSTEM", now);
        if (rows != 1) throw new IllegalStateException("Không thể từ chối sản phẩm; trạng thái có thể đã thay đổi");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteproduct(Integer product_id) {
        if (product_id == null || product_id <= 0) throw new IllegalArgumentException("ID sản phẩm không hợp lệ");
        Product existing = productRepository.getProductById(product_id);
        if (existing == null || "true".equalsIgnoreCase(existing.getIs_deleted())) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm: " + product_id);
        }
        if (!"PENDING_APPROVAL".equalsIgnoreCase(existing.getStatus())
                && !"REJECTED".equalsIgnoreCase(existing.getStatus())) {
            throw new IllegalStateException("Chỉ được xóa sản phẩm đang chờ duyệt hoặc đã bị từ chối");
        }
        if (campainProductRepository.existsAssociationByProductId(product_id)) {
            throw new IllegalStateException("Không thể xóa sản phẩm đã được thêm vào chiến dịch");
        }
        int rows = productRepository.deleteproduct(product_id);
        if (rows != 1) {
            log.warn("Delete product rejected: not found/already deleted. productId={}, rows={}", product_id, rows);
            throw new IllegalArgumentException("Không tìm thấy sản phẩm hoặc sản phẩm đã bị xóa: " + product_id);
        }
        int skuRows = productSkuRepository.softDeleteByProductId(product_id);
        log.info("Delete product completed. productId={}, deletedSkuCount={}", product_id, skuRows);
    }
    @Override
    public ProductDetailResponse product_detail(Integer campaign_id, Integer product_id) {
        ProductDetail product = productRepository.getproduct1(product_id, campaign_id);

        if (product == null) {
            return null;
        }
        ProductDetailResponse response = new ProductDetailResponse();
        response.setName(product.getName());
        response.setId(product.getId());
        response.setCampaignEndAt(product.getCampaignEndAt());
        response.setDescription(product.getDescription());
        response.setCampaignStartAt(product.getCampaignStartAt());
        response.setCampaignId(product.getCampaignId());
        response.setCampaignCode(product.getCampaignId() == null ? null : "CMP-" + product.getCampaignId());
        response.setCampaignName(product.getCampaignName());
        response.setCampaignDescription(product.getCampaignDescription());
        response.setCampaignDiscount(product.getCampaignDiscount());
        response.setPriceCampaign(product.getPriceCampaign());

        response.setStatus(product.getStatus());
        response.setTypeProductsName(product.getTypeProductsName());
        response.setAttributesName(product.getAttributesName());
        response.setSkus(product.getSkus());

        response.setCampaignStatus(product.getCampaignStatus());
        response.setCreateAt(product.getCreateAt());

        /* * Tìm SKU có giá sau giảm thấp nhất */
        if (product.getSkus() != null && !product.getSkus().isEmpty())
        { SkuResponse minPriceSku = product.getSkus() .stream() .filter(sku -> sku.getPriceDiscountCampaign() != null)
                .min(Comparator.comparing( SkuResponse::getPriceDiscountCampaign )).orElse(null);
            if (minPriceSku != null) {
                    // Giá gốc của chính SKU có giá sau giảm thấp nhất
            response.setPriceCampaign( minPriceSku.getPriceCampaign() );
                    // Giá sau giảm thấp nhất
            response.setPriceDiscountCampaign( minPriceSku.getPriceDiscountCampaign() );
            }
        }

        return response;
    }
}
