package com.vt.cms.viettelmock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MockCreateShipmentRequest(
        @NotBlank String orderReference,
        @NotBlank String receiverName,
        @NotBlank String receiverPhone,
        @NotBlank String receiverAddress,
        @NotNull Long codAmount,
        Integer weight
) {}