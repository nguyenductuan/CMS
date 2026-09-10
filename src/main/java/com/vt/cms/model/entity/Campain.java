package com.vt.cms.model.entity;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Campain {
    private int campainid;
    private String campainname;
    private LocalDateTime startdate;
    private LocalDateTime enddate;
    private int discountpercent;
    private int disacountamount;
}
