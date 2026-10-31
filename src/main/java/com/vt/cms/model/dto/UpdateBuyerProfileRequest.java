package com.vt.cms.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateBuyerProfileRequest {
    @Size(max = 150)
    private String name;
    private Integer age;
    @Size(max = 30)
    private String phone;
    @Email @Size(max = 150)
    private String email;
    @Size(max = 500)
    private String address;
    @Size(max = 120)
    private String bankName;
    @Size(max = 50)
    private String bankAccountNumber;
    @Size(max = 150)
    private String bankAccountHolder;
}
