package com.vt.cms.model.dto;

import java.util.List;

public class CreatedCampainRequest {
    private String campainname;
    private String startdate;
    private String enddate;
    private int discountpercent;
    private int disacountamount;
    private List<CampainProductRequest> products;

}
