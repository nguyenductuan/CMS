package com.vt.cms.model.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderReturn {
    private Integer id;
    private Integer orderId;
    private Integer buyerId;
    private String buyerUsername;
    private String buyerName;
    private String phone;
    private String email;
    private String address;
    private String bankName;
    private String bankAccountNumber;
    private String bankAccountHolder;
    private String reason;
    private String imagesJson;
    private String videosJson;
    private String status;
    private String sellerReviewer;
    private String adminReviewer;
    private String sellerNote;
    private String adminNote;
    private LocalDateTime createdAt;
    private LocalDateTime sellerReviewedAt;
    private LocalDateTime adminReviewedAt;
}
