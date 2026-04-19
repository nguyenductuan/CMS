package com.vt.cms.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserReq {

    @Schema(description = "Tên ngời dùng", example = "Nguyễn Văn A")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Tuổi người dùng", example ="10")
    @JsonProperty("age")
    private Integer age;
}
