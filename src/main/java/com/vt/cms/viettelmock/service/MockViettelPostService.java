package com.vt.cms.viettelmock.service;

import com.vt.cms.viettelmock.config.ViettelMockProperties;
import com.vt.cms.viettelmock.dto.MockCreateShipmentRequest;
import com.vt.cms.viettelmock.dto.MockShipmentResponse;
import com.vt.cms.viettelmock.dto.MockUpdateStatusRequest;
import com.vt.cms.viettelmock.model.MockShipment;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@ConditionalOnProperty(prefix = "viettel.mock", name = "enabled", havingValue = "true")
public class MockViettelPostService {
    private final Map<String, MockShipment> shipments = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);
    private final RestClient restClient = RestClient.create();
    private final ViettelMockProperties properties;

    public MockViettelPostService(ViettelMockProperties properties) {
        this.properties = properties;
    }

    public MockShipmentResponse create(MockCreateShipmentRequest request) {
        String code = "VT-MOCK-" + String.format("%06d", sequence.incrementAndGet());
        MockShipment shipment = new MockShipment();
        shipment.setOrderNumber(code);
        shipment.setOrderReference(request.orderReference());
        shipment.setReceiverName(request.receiverName());
        shipment.setReceiverPhone(request.receiverPhone());
        shipment.setReceiverAddress(request.receiverAddress());
        shipment.setCodAmount(request.codAmount());
        shipment.setWeight(request.weight());
        shipment.setStatus(100);
        shipment.setStatusName("Đã tạo vận đơn (MOCK)");
        shipment.setUpdatedAt(LocalDateTime.now());
        shipments.put(code, shipment);
        return toResponse(shipment);
    }

    public MockShipmentResponse get(String orderNumber) {
        return toResponse(find(orderNumber));
    }

    public MockShipmentResponse updateStatus(String orderNumber, MockUpdateStatusRequest request) {
        MockShipment shipment = find(orderNumber);
        shipment.setStatus(request.status());
        shipment.setStatusName(request.statusName() == null ? "Mock status " + request.status() : request.statusName());
        shipment.setNote(request.note());
        shipment.setLocationCurrently(request.locationCurrently());
        shipment.setUpdatedAt(LocalDateTime.now());
        sendWebhook(shipment);
        return toResponse(shipment);
    }

    private void sendWebhook(MockShipment shipment) {
        Map<String, Object> payload = Map.of(
                "token", properties.getWebhookToken(),
                "orderReference", shipment.getOrderReference(),
                "orderNumber", shipment.getOrderNumber(),
                "orderStatus", shipment.getStatus(),
                "statusName", shipment.getStatusName(),
                "note", shipment.getNote() == null ? "" : shipment.getNote(),
                "locationCurrently", shipment.getLocationCurrently() == null ? "" : shipment.getLocationCurrently(),
                "orderStatusDate", shipment.getUpdatedAt().toString()
        );
        try {
            restClient.post()
                    .uri(properties.getWebhookUrl())
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Không gửi được Webhook tới CMS: " + ex.getMessage(), ex);
        }
    }

    private MockShipment find(String orderNumber) {
        MockShipment shipment = shipments.get(orderNumber);
        if (shipment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy vận đơn MOCK: " + orderNumber);
        }
        return shipment;
    }

    private MockShipmentResponse toResponse(MockShipment s) {
        return new MockShipmentResponse(s.getOrderNumber(), s.getOrderReference(), s.getStatus(),
                s.getStatusName(), s.getReceiverName(), s.getReceiverPhone(), s.getReceiverAddress(),
                s.getCodAmount(), s.getWeight(), s.getUpdatedAt());
    }
}