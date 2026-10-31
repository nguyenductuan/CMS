package com.vt.cms.model.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductReview {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer skuId;
    private String skuCode;
    private Integer userId;
    private String username;
    private Integer rating;
    private String content;
    private List<String> images;
    private List<String> videos;
    private LocalDateTime createdAt;
}
