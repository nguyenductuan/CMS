package com.vt.cms.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProductResponse {
    @Schema(description = "Tên sản phẩm")
    @JsonProperty("name")
    private String name;
    @Schema(description = "Giá sản phẩm")
    @JsonProperty("price")
    private Integer price;
    @Schema(description = "Số lượng tồn kho")
    @JsonProperty("stock")
    private Integer stock;
}
