package com.vt.cms.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ShippingResponse {
    @Schema(description = "Id")
    @JsonProperty("id")
    private int id;
    @Schema(description = "Mã shipping")
    @JsonProperty("service_code")
    private String serviceCode;
    @Schema(description = "Tên shipping")
    @JsonProperty("service_name")
    private String serviceName;
    @Schema(description = "Gia vận chuyển")
    @JsonProperty("fee")
    private Integer fee;
}
