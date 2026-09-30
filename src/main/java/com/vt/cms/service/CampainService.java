package com.vt.cms.service;

import com.vt.cms.model.dto.CreatedCampainRequest;



public interface CampainService {
    void createdCampain(CreatedCampainRequest request);
    void updatestatus(Integer campain_id, String status);
     void getlist();
     void deletecampain(Integer campainID);
}
