package com.vt.cms.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vt.cms.mapper.Modelmapper;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.ProductRequest;
import com.vt.cms.model.dto.page.PageInfo;
import com.vt.cms.model.dto.page.PagingResponse;
//import com.vt.cms.model.dto.product.SkuRequest;
import com.vt.cms.model.dto.product.SkuRequest;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.entity.Product_Sku;
import com.vt.cms.model.repository.ProductRepository;
//import com.vt.cms.model.repository.ProductSkuRepository;
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
    public void addproduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        //product.setStock(productRequest.getStock());
        product.setDescription(productRequest.getDescription());
        product.setStatus(productRequest.getStatus());
        product.setIs_deleted("false");

       // product.setPrice_display(productRequest.getProductPrice());
        product.setCreated_at(LocalDateTime.now());
        product.setCreated_by("SYSTEMS");
        try{
            product.setType_products(objectMapper.writeValueAsString(productRequest.getType_products()));
            product.setJson_attributes(objectMapper.writeValueAsString(productRequest.getAttributes()));
            product.setJson_medias(objectMapper.writeValueAsString(productRequest.getMedias()));
            product.setJson_images(objectMapper.writeValueAsString(productRequest.getImages()));
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot convert to JSON", e);
        }

        product.setJson_attributes_name(productRequest.getAttributes_name());
        product.setType_products_name(productRequest.getType_products_name());
        productRepository.insertproduct(product);

        Product_Sku productSku = new Product_Sku();
        for (SkuRequest skuRequest : productRequest.getSkus()){
            productSku.setProduct_id(product.getId());
            productSku.setHeight_cm(skuRequest.getHeight_cm());
            productSku.setWidth_cm(skuRequest.getWidth_cm());
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
    public ProductResponse detail(Integer id) {
        return productRepository.detailProduct(id);
    }

    @Override
    public void editproduct(Integer id, ProductRequest productRequest) {

    }

//    @Override
//    public void editproduct(Integer id, ProductRequest productRequest) {
//        ProductResponse product = productRepository.detailProduct(id);
//        if (product == null) {
//            throw new RuntimeException("Không tìm thâý sản phẩm"); // cần response trả về 200
//        }
//
//        Product product1 = new Product();
//        product1.setName(productRequest.getProductName());
//        product1.setStock(productRequest.getStock());
//        product1.setDescription(productRequest.getProductDescription());
//        product1.setPrice_display(productRequest.getProductPrice());
//        product1.setUpdatedAt(LocalDateTime.now());
//        product1.setImage(productRequest.getProductimage());
//        productRepository.upload(product1, id);
//    }

    @Override
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
    public void editstatusproduct(Integer product_id) {
        var updated_at = LocalDateTime.now();
        String status = "APPROVAL";
        productRepository.editstatusproduct(product_id, status, updated_at);
    }

    @Override
    public void deleteproduct(Integer id) {
        productRepository.deleteproduct(id);
    }

    @Override
    public ProductDetailResponse product_detail(Integer productId, Integer campainId) {
      return  productRepository.getproduct(productId,campainId);
    }
}
