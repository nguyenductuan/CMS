package com.vt.cms.product.application.service;

import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.base.response.BaseResponse;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.product.application.dto.req.CreateProductRequest;
import com.vt.cms.product.application.dto.response.ProductDetailResponse;
import com.vt.cms.product.application.dto.response.ProductResponse;
import java.util.List;

public interface ProductService {
  /** Thêm addproduct. Tham số và kết quả tuân theo chữ ký phương thức. */
  void addProduct(CreateProductRequest createProductRequest, Integer userId);

  /** Thực hiện detail. Tham số và kết quả tuân theo chữ ký phương thức. */
  ProductResponse detail(Integer product_id);

  /** Chỉnh sửa editproduct. Tham số và kết quả tuân theo chữ ký phương thức. */
  void editproduct(Integer id, CreateProductRequest createProductRequest, Integer userId);

  /** Lấy danh sách listproduct. Tham số và kết quả tuân theo chữ ký phương thức. */
  BaseResponse<PagingResponse<List<ProductResponse>>> listproduct(Pagerequest request);

  /* Duyệt sản phẩm */
  void approvedProduct(Integer product_id, Integer userId);

  /** Thực hiện reject product. Tham số và kết quả tuân theo chữ ký phương thức. */
  void rejectProduct(Integer product_id, Integer userId, String reason);

  /** Xóa deleteproduct. Tham số và kết quả tuân theo chữ ký phương thức. */
  void deleteProduct(Integer product_id);

  /** Thực hiện product_detail. Tham số và kết quả tuân theo chữ ký phương thức. */
  ProductDetailResponse product_detail(Integer campaign_id, Integer product_id);
}
