package com.vt.cms.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Shipment {
    private Integer id;// id của shipment để tracking
    private Integer orderId;// mã đơn
    private String shipmentName;// tên shipper
    private String warehouseName;// ten kho
    private String shippingProvider; // GHN, GHTK
    private String trackingCode; // mã vận đơn
    private LocalDateTime createdAt;
    private Integer shipperId;
    private String title;
    private LocalDateTime estimatedDeliveryTime; //thời gian giao dự kiến trả từ API vận chuyển
    private LocalDateTime shippedAt;//Thời điểm đơn hàng bắt đầu được giao (Kho → Shipper nhận hàng → bắt đầu đi giao)
    private LocalDateTime deliveredAt;//Thời điểm đơn hàng giao thành công (user nhận hàng) (Shipper giao → User bấm "Đã nhận hàng")
    private String status; // CREATED, READY, DELIVERING, DELIVERED
}
