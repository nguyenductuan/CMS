package com.vt.cms.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WarehouseResponse {
    @Schema(description = "Id kho")
    @JsonProperty("id")
    private Integer id;
    @Schema(description = "Mã kho")
    @JsonProperty("warehouse_code")
    private Integer warehouse_code;
    @Schema(description = "Tên kho")
    @JsonProperty("warehouse_name")
    private String warehouse_name;
    @Schema(description = "Giá trị mặc định")
    @JsonProperty("is_address_default")
    private Boolean address_default;


}
