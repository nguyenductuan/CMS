package com.vt.cms.model.dto;

import lombok.Data;

@Data
public class ShippingRequest {
    private String address_type;
    private String address_detail;
    private String receiver_name;
    private String receiver_phone;
}
