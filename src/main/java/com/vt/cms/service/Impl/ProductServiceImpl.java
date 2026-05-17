package com.vt.cms.service.Impl;

import com.vt.cms.mapper.Modelmapper;
import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.ProductRequest;
import com.vt.cms.model.dto.page.PageInfo;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.entity.Product;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.model.resp.BaseResponse;
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


    @Override
    public void addproduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getProductName());
        product.setStock(productRequest.getStock());
        product.setDescription(productRequest.getProductDescription());
        product.setStatus("WAITING APPROVED");
        product.setPrice(productRequest.getProductPrice());
        product.setCreatedAt(LocalDateTime.now());
        product.setImage("https://down-vn.img.susercontent.com/file/sg-11134201-822zi-mibaop7aot8g88.webp");
        productRepository.insertproduct(product);
    }

    @Override
    public ProductResponse detail(Integer id) {
        ProductResponse product = productRepository.detailProduct(id);
        return product;

    }

    @Override
    public void editproduct(Integer id, ProductRequest productRequest) {
        Product product1 = new Product();
        product1.setName(productRequest.getProductName());
        product1.setStock(productRequest.getStock());
        product1.setDescription(productRequest.getProductDescription());
        product1.setStatus("WAITING APPROVED");
        product1.setPrice(productRequest.getProductPrice());
        product1.setCreatedAt(LocalDateTime.now());
        product1.setImage("https://down-vn.img.susercontent.com/file/sg-11134201-822zi-mibaop7aot8g88.webp");

        productRepository.upload(product1, id);

    }

    @Override
    public BaseResponse<PagingResponse<List<ProductResponse>>> listproduct(OrdersRequest request) {
        List<ProductResponse> product = productRepository.listproduct(request);
        long totalCount = productRepository.countproduct();
        long totalpage = totalCount / (request.getPageSize());


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

}
