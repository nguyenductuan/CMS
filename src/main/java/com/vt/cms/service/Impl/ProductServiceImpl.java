package com.vt.cms.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vt.cms.mapper.Modelmapper;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.CreateProductRequest;
import com.vt.cms.model.dto.page.PageInfo;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.dto.product.*;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.entity.Product_Sku;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.model.repository.ProductSkuRepository;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.ProductDetailResponse;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final Modelmapper modelMapper = Mappers.getMapper(Modelmapper.class);
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final ProductSkuRepository productSkuRepository;

    @Override
    //Thêm mới sp
    public void addproduct(CreateProductRequest createProductRequest) {
        Product product = new Product();
        product.setName(createProductRequest.getName());
        product.setDescription(createProductRequest.getDescription());
        product.setStatus(createProductRequest.getStatus());
        product.setIs_deleted("false");
        product.setCreated_at(LocalDateTime.now());
        product.setCreated_by("SYSTEMS");
        try{
            product.setType_products(objectMapper.writeValueAsString(createProductRequest.getType_products()));
            product.setJson_attributes(objectMapper.writeValueAsString(createProductRequest.getAttributes()));
            product.setJson_medias(objectMapper.writeValueAsString(createProductRequest.getMedias()));
            product.setJson_images(objectMapper.writeValueAsString(createProductRequest.getImages()));
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot convert to JSON", e);
        }

        product.setJson_attributes_name(createProductRequest.getAttributes_name());
        product.setType_products_name(createProductRequest.getType_products_name());
        productRepository.insertproduct(product);

        Product_Sku productSku = new Product_Sku();
        for (SkuRequest skuRequest : createProductRequest.getSkus()){
            productSku.setProduct_id(product.getId());
            productSku.setHeight_cm(skuRequest.getHeight_cm());
            productSku.setWidth_cm(skuRequest.getWidth_cm());
            productSku.setLength_cm(skuRequest.getLength_cm());
            productSku.setWeight_gram(skuRequest.getWeight_gram());
            productSku.setType_product(skuRequest.getType_product());
            productSku.setStock(skuRequest.getStock());
            productSku.setStatus(skuRequest.getStatus());
            productSku.setSku_code(skuRequest.getSku_code());
            productSku.setPrice(skuRequest.getPrice());
            productSku.setImage_url(skuRequest.getImage_url());
            productSku.setAttribute(skuRequest.getAttribute());
            productSku.setCreated_by("SYSTEM");
            productSku.setIs_deleted("false");

            productSku.setCreated_at(LocalDateTime.now());
            productSkuRepository.inserproductsku(productSku);
        }
    }
    @Override
    public ProductResponse detail(Integer product_id) {

        Product product = productRepository.detailProduct(product_id);

        ProductResponse response =
                new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setStatus(product.getStatus());
        response.setCreated_at(product.getCreated_at());
        response.setIs_deleted(product.getIs_deleted());
        response.setCreated_by(product.getCreated_by());
        System.out.println("product = " + product);
        System.out.println("json_attributes = " + product.getJson_attributes());
        // =========================
        // ATTRIBUTES
        // =========================

        response.setAttributes(
                parseJson(
                        product.getJson_attributes(),
                        new TypeReference<List<AttributeRequest>>() {}
                )
        );

        response.setAttributes_name(
                product.getJson_attributes_name()
        );

        // =========================
        // TYPE PRODUCTS
        // =========================

        response.setType_products(
                parseJson(
                        product.getType_products(),
                        new TypeReference<List<TypeProductRequest>>() {}
                )
        );

        response.setType_products_name(
                product.getType_products_name()
        );

        // =========================
        // IMAGES
        // =========================

        response.setImages(
                parseJson(
                        product.getJson_images(),
                        new TypeReference<List<ImageRequest>>() {}
                )
        );

        // =========================
        // MEDIAS
        // =========================

        response.setMedias(
                parseJson(
                        product.getJson_medias(),
                        new TypeReference<List<MediaRequest>>() {}
                )
        );
        // =========================
        // SKU
        // =========================

        List<Product_Sku> productSkus =
                productSkuRepository.findByProductId(product_id);

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
                    "JSON sản phẩm không hợp lệ: " + json,
                    e
            );
        }
    }
    @Override
    public void editproduct(Integer id, CreateProductRequest createProductRequest) {
//        ProductResponse product = productRepository.detailProduct(id);
//        if (product == null) {
//            throw new RuntimeException("Không tìm thâý sản phẩm"); // cần response trả về 200
//        }
//        Product product1 = new Product();
//        product1.setName(productRequest.getProductName());
//        product1.setStock(productRequest.getStock());
//        product1.setDescription(productRequest.getProductDescription());
//        product1.setPrice_display(productRequest.getProductPrice());
//        product1.setUpdatedAt(LocalDateTime.now());
//        product1.setImage(productRequest.getProductimage());
//        productRepository.upload(product1, id);
    }

    @Override
    //Danh sách sản phẩm
    public BaseResponse<PagingResponse<List<ProductResponse>>> listproduct(OrdersRequest request) {
        List<ProductResponse> product = productRepository.listproduct(request);
        long totalCount = productRepository.countproduct();
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
        var updated_at = LocalDateTime.now();
        String status = "APPROVAL";
        String approve_by = "SYSTEM";
        var approved_time = LocalDateTime.now();
        productRepository.editstatusproduct(product_id, status, updated_at, approve_by, approved_time);
    }
    @Override
    //Xóa sp
    public void deleteproduct(Integer product_id) {
        productRepository.deleteproduct(product_id);
    }
    @Override
    public ProductDetailResponse product_detail(Integer productId, Integer campainId) {
      return  productRepository.getproduct(productId,campainId);
    }
}
