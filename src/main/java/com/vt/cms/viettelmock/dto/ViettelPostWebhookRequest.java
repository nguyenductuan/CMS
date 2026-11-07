package com.vt.cms.viettelmock.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ViettelPostWebhookRequest(
        @NotBlank String token,
        @NotBlank String orderReference,
        String orderNumber,
        @NotNull Integer orderStatus,
        String statusName,
        String note,
        String locationCurrently,
        String orderStatusDate
) {}
