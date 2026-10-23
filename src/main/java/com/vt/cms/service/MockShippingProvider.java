package com.vt.cms.service;

import com.vt.cms.model.entity.ShippingAssignment;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adapter giả lập đơn vị vận chuyển thứ ba.
 * Production: thay bằng HTTP client có timeout, xác thực, retry giới hạn
 * và idempotency key; không tin callback nếu chưa xác thực chữ ký.
 */
@Component
public class MockShippingProvider {
    /**
     * Gán dữ liệu assign shipper. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public ShippingAssignment assignShipper(Integer orderId, String warehouseCode) {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        ShippingAssignment assignment = new ShippingAssignment();
        assignment.setProvider("MOCK-EXPRESS");
        assignment.setTrackingNumber("MOCK-" + orderId + "-" + suffix);
        assignment.setShipperCode("SHIPPER-" + suffix);
        assignment.setShipperName("Shipper Demo");
        assignment.setShipperPhone("0900000000");
        return assignment;
    }
}
