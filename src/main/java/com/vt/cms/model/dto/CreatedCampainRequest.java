package com.vt.cms.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class CreatedCampainRequest {
    private String campainname;
    private LocalDateTime startdate;
    private LocalDateTime enddate;
    private int discountpercent;
    private List<CampainProductRequest> products;
}
