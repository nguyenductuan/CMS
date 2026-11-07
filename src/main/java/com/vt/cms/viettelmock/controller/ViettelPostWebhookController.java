package com.vt.cms.viettelmock.controller;

import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.service.OrderFulfillmentService;
import com.vt.cms.viettelmock.config.ViettelMockProperties;
import com.vt.cms.viettelmock.dto.ViettelPostWebhookRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/internal/webhooks/viettelpost")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "viettel.mock", name = "enabled", havingValue = "true")
public class ViettelPostWebhookController {
    private final OrderFulfillmentService fulfillmentService;
    private final ViettelMockProperties properties;

    /**
     * Nhận callback trạng thái từ Viettel Post (hoặc mock server).
     * orderReference phải là ID đơn hàng nội bộ của CMS.
     */
    @PostMapping
    public APIRessponse receive(@Valid @RequestBody ViettelPostWebhookRequest request) {
        String configuredToken = properties.getWebhookToken();
        if (configuredToken == null || configuredToken.isBlank()
                || !MessageDigest.isEqual(
                        configuredToken.getBytes(StandardCharsets.UTF_8),
                        request.token().getBytes(StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Webhook token không hợp lệ");
        }

        final Integer orderId;
        try {
            orderId = Integer.valueOf(request.orderReference().trim());
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "orderReference phải là ID đơn hàng CMS", ex);
        }

        String code = String.valueOf(request.orderStatus());
        String description = String.join(" - ",
                request.statusName() == null ? "" : request.statusName(),
                request.note() == null ? "" : request.note(),
                request.locationCurrently() == null ? "" : request.locationCurrently()
        ).replaceAll("^( - )+|( - )+$", "").trim();

        fulfillmentService.recordCarrierTrackingEvent(orderId, code, description);
        return new APIRessponse(200, "Đã nhận callback Viettel Post cho đơn " + orderId);
    }
}
