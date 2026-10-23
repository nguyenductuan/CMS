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
    /**
     * Thêm dữ liệu addproduct. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void addproduct(CreateProductRequest request) {
        productRequestValidator.validate(request);
        log.info("Create product started. name={}, skuCount={}", request.getName(), request.getSkus().size());
        Product product = productEntityMapper.toNewProduct(request);
        int rows = productRepository.insertproduct(product);
        if (rows != 1 || product.getId() == null) {
            log.error("Insert product failed. name={}, rows={}, generatedId={}", request.getName(), rows, product.getId());
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Không thể tạo sản phẩm");
        }
        saveSkus(product.getId(), request.getSkus());
        log.info("Create product completed. productId={}, skuCount={}", product.getId(), request.getSkus().size());
    }

    /**
     * Thực hiện xử lý nghiệp vụ merge skus. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
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
                /**
                 * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
                 */
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

    /**
     * Lưu dữ liệu save skus. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    private void saveSkus(Integer productId, List<SkuRequest> requests) {
        for (SkuRequest request : requests) {
            Product_Sku sku = mapSkuFields(productId, request);
            sku.setCreated_by("SYSTEM");
            sku.setIs_deleted("false");
            sku.setCreated_at(LocalDateTime.now());
            int rows = productSkuRepository.inserproductsku(sku);
            if (rows != 1) {
                log.error("Insert SKU failed. productId={}, skuCode={}, rows={}", productId, sku.getSku_code(), rows);
                /**
                 * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
                 */
                throw new IllegalStateException("Không thể lưu SKU: " + sku.getSku_code());
            }
        }
    }
    @Override
    /**
     * Thực hiện xử lý nghiệp vụ detail. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public ProductResponse detail(Integer product_id) {
        ProductResponse product = productRepository.detailProduct(product_id);
        if (product == null || "true".equalsIgnoreCase(product.getIsDeleted())) {
            log.warn("Product detail not found. productId={}", product_id);
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException("Không tìm thấy sản phẩm: " + product_id);
        }
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setStatus(product.getStatus());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        response.setIsDeleted(product.getIsDeleted());
        response.setCreatedBy(product.getCreatedBy());
        response.setJsonImages(product.getJsonImages());
        response.setJsonMedias(product.getJsonMedias());
        response.setUpdatedAt(product.getUpdatedAt());

        response.setJsonAttributesName(product.getJsonAttributesName());
        response.setTypeProductsName(product.getTypeProductsName());

           response.setJsonAttributes(
                    parseJson(
                            product.getJsonAttributesRaw(),
                            new TypeReference<List<AttributeRequest>>() {}
                    )
            );
            response.setTypeProducts(
                    parseJson(
                            product.getTypeProductsRaw(),
                            new TypeReference<List<TypeProductRequest>>() {}
                    )
            );

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
    /**
     * Thực hiện xử lý nghiệp vụ parse json. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    private <T> List<T> parseJson(String json, TypeReference<List<T>> typeReference) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(json,typeReference);

        } catch (JsonProcessingException e) {
            /**
             * Thực hiện xử lý nghiệp vụ runtime exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new RuntimeException(
                    "JSON sản phẩm không hợp lệ",
                    e
            );
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    /**
     * Chỉnh sửa dữ liệu editproduct. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void editproduct(Integer id, CreateProductRequest request) {
        if (id == null || id <= 0) throw new IllegalArgumentException("ID sản phẩm không hợp lệ");
        productRequestValidator.validate(request);
        Product existing = productRepository.getProductById(id);
        if (existing == null || "true".equalsIgnoreCase(existing.getIsDeleted())) {
            log.warn("Update product rejected: not found. productId={}", id);
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException("Không tìm thấy sản phẩm: " + id);
        }
        if (!"PENDING_APPROVAL".equalsIgnoreCase(existing.getStatus())
                && !"REJECTED".equalsIgnoreCase(existing.getStatus())) {
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Chỉ được sửa sản phẩm đang chờ duyệt hoặc đã bị từ chối");
        }
        Product update = productEntityMapper.toNewProduct(request);
        update.setStatus("PENDING_APPROVAL");
        update.setUpdatedAt(LocalDateTime.now());
        update.setUpdatedBy("SYSTEM");
        int rows = productRepository.updateProduct(update, id);
        if (rows != 1) {
            log.error("Update product failed. productId={}, rows={}", id, rows);
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Không thể cập nhật sản phẩm");
        }
        // Merge SKU theo sku_code để không vô tình xóa SKU đang được campaign tham chiếu.
        mergeSkus(id, request.getSkus());
        log.info("Update product completed. productId={}, requestedSkuCount={}", id, request.getSkus().size());
    }
    @Override
    //Danh sách sản phẩm
    /**
     * Lấy danh sách listproduct. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public BaseResponse<PagingResponse<List<ProductResponse>>> listproduct(OrdersRequest request) {
        if (request == null) throw new IllegalArgumentException("Thông tin phân trang không được để trống");
        if (request.getPageNo() == null || request.getPageNo() < 0) throw new IllegalArgumentException("pageNo phải >= 0");
        if (request.getPageSize() == null || request.getPageSize() < 1 || request.getPageSize() > 100) throw new IllegalArgumentException("pageSize phải nằm trong khoảng 1..100");
        List<ProductResponse> product = productRepository.listproduct(request);

        for (ProductResponse item : product) {
            item.setJsonAttributes(
                    parseJson(
                            item.getJsonAttributesRaw(),
                            new TypeReference<List<AttributeRequest>>() {}
                    )
            );

            item.setTypeProducts(
                    parseJson(
                            item.getTypeProductsRaw(),
                            new TypeReference<List<TypeProductRequest>>() {}
                    )
            );
        }


       // Lấy danh sách SKU cho từng sản phẩm
        for (ProductResponse item : product) {
            List<Product_Sku> productSkus =
                    productSkuRepository.findByProductId(item.getId());

            List<SkuRequest> skus = productSkus.stream()
                    .map(sku -> {
                        SkuRequest skuRequest = new SkuRequest();
                        skuRequest.setSku_code(sku.getSku_code());
                        skuRequest.setAttribute(sku.getAttribute());
                        skuRequest.setType_product(sku.getType_product());
                        skuRequest.setImage_url(sku.getImage_url());
                        skuRequest.setPrice(sku.getPrice());
                        skuRequest.setStock(sku.getStock());
                        skuRequest.setWeight_gram(sku.getWeight_gram());
                        skuRequest.setLength_cm(sku.getLength_cm());
                        skuRequest.setWidth_cm(sku.getWidth_cm());
                        skuRequest.setHeight_cm(sku.getHeight_cm());
                        skuRequest.setStatus(sku.getStatus());

                        return skuRequest;
                    })
                    .toList();

            item.setSkus(skus);
        }
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
    /**
     * Chỉnh sửa dữ liệu editstatusproduct. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void editstatusproduct(Integer product_id) {
        if (product_id == null || product_id <= 0)
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException("ID sản phẩm không hợp lệ");
        Product existing = productRepository.getProductById(product_id);
        if (existing == null || "true".equalsIgnoreCase(existing.getIsDeleted()))
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException("Không tìm thấy sản phẩm: " + product_id);
        if (!"PENDING_APPROVAL".equalsIgnoreCase(existing.getStatus())) {
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Chỉ sản phẩm đang chờ duyệt mới được duyệt");
        }
        LocalDateTime now = LocalDateTime.now();
        int rows = productRepository.editstatusproduct
                (product_id, "APPROVED", now, "SYSTEM", now);
        if (rows != 1) throw new IllegalStateException
                ("Không thể duyệt sản phẩm; trạng thái có thể đã thay đổi");
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    /**
     * Thực hiện xử lý nghiệp vụ reject product. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void rejectProduct(Integer product_id) {
        if (product_id == null || product_id <= 0) throw new IllegalArgumentException("ID sản phẩm không hợp lệ");
        Product existing = productRepository.getProductById(product_id);
        if (existing == null || "true".equalsIgnoreCase(existing.getIsDeleted())) {
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException("Không tìm thấy sản phẩm: " + product_id);
        }
        if (!"PENDING_APPROVAL".equalsIgnoreCase(existing.getStatus())) {
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Chỉ sản phẩm đang chờ duyệt mới được từ chối");
        }
        LocalDateTime now = LocalDateTime.now();
        int rows = productRepository.editstatusproduct(product_id, "REJECTED", now, "SYSTEM", now);
        if (rows != 1) throw new IllegalStateException("Không thể từ chối sản phẩm; trạng thái có thể đã thay đổi");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    /**
     * Xóa dữ liệu deleteproduct. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void deleteproduct(Integer product_id) {
        if (product_id == null || product_id <= 0)
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException("ID sản phẩm không hợp lệ");
        Product existing = productRepository.getProductById(product_id);
        if (existing == null || "true".equalsIgnoreCase(existing.getIsDeleted())) {
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException("Không tìm thấy sản phẩm: " + product_id);
        }
        if (!"PENDING_APPROVAL".equalsIgnoreCase(existing.getStatus())
                && !"REJECTED".equalsIgnoreCase(existing.getStatus())) {
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Chỉ được xóa sản phẩm đang chờ duyệt hoặc đã bị từ chối");
        }
        if (campainProductRepository.existsAssociationByProductId(product_id)) {
            /**
             * Thực hiện xử lý nghiệp vụ illegal state exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalStateException("Không thể xóa sản phẩm đã được thêm vào chiến dịch");
        }
        int rows = productRepository.deleteproduct(product_id);
        if (rows != 1) {
            log.warn("Delete product rejected: not found/already deleted. productId={}, rows={}", product_id, rows);
            /**
             * Thực hiện xử lý nghiệp vụ illegal argument exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException("Không tìm thấy sản phẩm hoặc sản phẩm đã bị xóa: " + product_id);
        }
        int skuRows = productSkuRepository.softDeleteByProductId(product_id);
        log.info("Delete product completed. productId={}, deletedSkuCount={}", product_id, skuRows);
    }
    @Override
    /**
     * Thực hiện xử lý nghiệp vụ product detail. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
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
