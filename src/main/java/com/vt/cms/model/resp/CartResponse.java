package com.vt.cms.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CartResponse {
    @Schema(description = "ID người dùng")
    @JsonProperty("user_id")
    private Integer userId;
    @Schema(description = "Id cart")
    @JsonProperty("id")
    private Integer id;
}
