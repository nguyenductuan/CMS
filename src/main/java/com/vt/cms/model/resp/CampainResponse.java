package com.vt.cms.model.resp;

import java.time.LocalDateTime;
import java.util.List;

public class CampainResponse {
    private Long id;
    private String name;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private List<CampaignProductResponse> products;
}
