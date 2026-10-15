package com.vt.cms.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAssignment {
    private String provider;
    private String trackingNumber;
    private String shipperCode;
    private String shipperName;
    private String shipperPhone;
}
