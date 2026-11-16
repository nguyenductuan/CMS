package com.vt.cms.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Một dòng dữ liệu xuất Excel; mỗi dòng tương ứng một SKU trong đơn hàng. */
@Data
public class OrderExportRow {
    private Long orderId;
    private LocalDateTime orderDate;
    private String orderStatus;
    private String buyerNote;
    private String trackingNumber;
    private String shippingProvider;
    private String deliveryMethod;
    private LocalDateTime expectedDelivery;
    private LocalDateTime shippedAt;
    private String productName;
    private BigDecimal productWeight;
    private BigDecimal totalWeight;
    private String skuCode;
    private String skuName;
    private BigDecimal originalPrice;
    private BigDecimal sellingPrice;
    private Integer quantity;
    private BigDecimal orderTotal;
    private BigDecimal platformDiscount;
    private BigDecimal estimatedShippingFee;
    private BigDecimal buyerShippingFee;
    private BigDecimal buyerPaidTotal;
    private LocalDateTime completedAt;
    private LocalDateTime paidAt;
    private String paymentMethod;
    private BigDecimal expectedRevenue;
    private String sellerName;
    private String buyerName;
    private String recipientName;
    private String recipientPhone;
    private String country;
    private String province;
    private String ward;
    private String deliveryAddress;
    private String sellerNote;
}