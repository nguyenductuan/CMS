package com.vt.cms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty ("name")
    private String name;
    @JsonProperty("age")
    private Integer age;
}
