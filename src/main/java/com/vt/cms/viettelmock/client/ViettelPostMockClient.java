package com.vt.cms.viettelmock.client;

import com.vt.cms.viettelmock.config.ViettelMockProperties;
import com.vt.cms.viettelmock.dto.MockCreateShipmentRequest;
import com.vt.cms.viettelmock.dto.MockShipmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ViettelPostMockClient {
    private final ViettelMockProperties properties;

    public MockShipmentResponse createShipment(MockCreateShipmentRequest request) {
        return RestClient.create(properties.getBaseUrl())
                .post()
                .uri("/internal/mock/viettelpost/orders")
                .body(request)
                .retrieve()
                .body(MockShipmentResponse.class);
    }
}