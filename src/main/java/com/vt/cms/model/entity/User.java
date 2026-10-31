package com.vt.cms.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("age")
    private Integer age;
    private String username;
    @JsonIgnore
    private String password;
    private String role;
    private String phone;
    private String email;
    private String address;
    private String bankName;
    private String bankAccountNumber;
    private String bankAccountHolder;
}
