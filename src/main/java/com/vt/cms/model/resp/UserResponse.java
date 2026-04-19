package com.vt.cms.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserResponse{
    @Schema(description ="Tên")
    @JsonProperty("name")
      private String name;
    @Schema(description ="Tuổi")
    @JsonProperty("age")
    private String age;
}
