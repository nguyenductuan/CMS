package com.vt.cms.service;

import com.vt.cms.model.dto.CreatedCampainRequest;
import com.vt.cms.model.enums.CampainStatus;


public interface CampainService {
    void createdCampain(CreatedCampainRequest request);
    void updatestatus(Integer campain_id, CampainStatus status);
     void getlist();
     void deletecampain(Integer campainID);

}
