package com.vt.cms.viettelmock.dto;

import java.time.LocalDateTime;

public record MockShipmentResponse(
        String orderNumber,
        String orderReference,
        Integer status,
        String statusName,
        String receiverName,
        String receiverPhone,
        String receiverAddress,
        Long codAmount,
        Integer weight,
        LocalDateTime updatedAt
) {}