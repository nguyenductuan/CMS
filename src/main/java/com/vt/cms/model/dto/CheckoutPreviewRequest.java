package com.vt.cms.model.dto;

import lombok.Data;

import java.util.List;
@Data
public class CheckoutPreviewRequest {
    private List<ItemRequest> items;
}
