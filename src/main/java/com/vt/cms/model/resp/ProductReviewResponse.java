package com.vt.cms.model.resp;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductReviewResponse {
    private Integer id;
    private Integer orderId;
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
