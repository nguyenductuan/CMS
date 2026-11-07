package com.vt.cms.viettelmock.service;

import com.vt.cms.viettelmock.dto.MockCreateShipmentRequest;
import com.vt.cms.viettelmock.dto.MockShipmentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MockViettelPostService {
    private final Map<String, MockShipmentResponse> shipments = new ConcurrentHashMap<>();
    private final Map<String, String> orderNumbersByReference = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public MockShipmentResponse create(MockCreateShipmentRequest request) {
        String existingNumber = orderNumbersByReference.get(request.orderReference());
        if (existingNumber != null) {
            return shipments.get(existingNumber);
        }

        String number = "VT-MOCK-" + String.format("%06d", sequence.incrementAndGet());
        MockShipmentResponse response = new MockShipmentResponse(
                number, request.orderReference(), 100, "Đã tạo vận đơn (MOCK)",
                request.receiverName(), request.receiverPhone(), request.receiverAddress(),
                request.codAmount(), request.weight(), LocalDateTime.now());
        shipments.put(number, response);
        orderNumbersByReference.put(request.orderReference(), number);
        return response;
    }

    public MockShipmentResponse get(String orderNumber) {
        MockShipmentResponse response = shipments.get(orderNumber);
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy vận đơn MOCK");
        }
        return response;
    }
}