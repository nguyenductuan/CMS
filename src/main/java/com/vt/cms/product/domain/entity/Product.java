package com.vt.cms.product.domain.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Product {
  private Integer id;
  private String name;
  private LocalDateTime createdAt;
  private String createdBy;
  private String isDeleted;
  private LocalDateTime updatedAt;
  private String updatedBy;
  private String description;
  private String jsonImages;
  private String jsonAttributes; // VD: [{"name":"Xanh than"},{"name":"Retro bụi"}]
  private String jsonMedias;
  private String status;
  private String jsonAttributesName; // thuộc tính thứ nhất của sp VD: Màu
  private String typeProductsName; // thuộc tính thứ 2 của sản phẩm. VD: Kích thươc
  private String typeProducts; // VD: [{"name":"M"},{"name":"L"}]
  private String cancelAt;
  private String cancelBy;
  private String approveBy;
  private String approveByRole;
  private LocalDateTime approvedTime;
}
