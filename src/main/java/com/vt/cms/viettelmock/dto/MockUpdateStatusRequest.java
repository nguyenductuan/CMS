package com.vt.cms.viettelmock.dto;

import jakarta.validation.constraints.NotNull;

public record MockUpdateStatusRequest(
        @NotNull Integer status,
        String statusName,
        String note,
        String locationCurrently
) {}