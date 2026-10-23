package com.vt.cms.service;

import com.vt.cms.model.dto.OrdersRequest;
import com.vt.cms.model.dto.CreateProductRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.ProductDetailResponse;
import com.vt.cms.model.resp.ProductResponse;

import java.util.List;

public interface ProductService {
    /**
     * Thêm addproduct. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    void addproduct(CreateProductRequest createProductRequest);

    /**
     * Thực hiện detail. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    ProductResponse detail(Integer product_id);

    /**
     * Chỉnh sửa editproduct. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    void editproduct(Integer id, CreateProductRequest createProductRequest);

    /**
     * Lấy danh sách listproduct. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    BaseResponse<PagingResponse<List<ProductResponse>>> listproduct(OrdersRequest request);

    /**
     * Chỉnh sửa editstatusproduct. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    void editstatusproduct(Integer product_id);

    /**
     * Thực hiện reject product. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    void rejectProduct(Integer product_id);

    /**
     * Xóa deleteproduct. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    void deleteproduct(Integer product_id);

    /**
     * Thực hiện product_detail. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    ProductDetailResponse product_detail(Integer campaign_id, Integer product_id);


}
