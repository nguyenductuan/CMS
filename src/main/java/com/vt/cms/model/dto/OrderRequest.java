package com.vt.cms.model.dto;


import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<OrderItemRequest> order;
    private List<ShippingRequest> shipping;
    private Double totalamount;
}

