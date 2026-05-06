package com.vt.cms.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Trackingstep {

    private String status;
    private String description;
    private LocalDateTime time;
}
