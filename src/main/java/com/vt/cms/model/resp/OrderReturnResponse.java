package com.vt.cms.model.resp;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderReturnResponse {
    private Integer id;
    private Integer orderId;
    private String orderStatus;
    private java.math.BigDecimal orderTotal;
    private java.util.List<com.vt.cms.model.dto.ItemDto> orderItems;
    private String buyerUsername;
    private String buyerName;
    private String phone;
    private String email;
    private String address;
    private String bankName;
    private String bankAccountNumber;
    private String bankAccountHolder;
    private String reason;
    private List<String> images;
    private List<String> videos;
    private String status;
    private String returnMethod;
    private LocalDateTime methodSelectionDeadline;
    private LocalDateTime methodSelectedAt;
    private String sellerReviewer;
    private String adminReviewer;
    private String sellerNote;
    private String adminNote;
    private LocalDateTime createdAt;
    private LocalDateTime sellerReviewedAt;
    private LocalDateTime adminReviewedAt;
}
