package com.vt.cms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Shipping {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("service_code")
    private String servicecode;
    @JsonProperty("service_name")
    private String servicename;
    @JsonProperty("free")
    private int fee;
}
