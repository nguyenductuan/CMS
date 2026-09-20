package com.vt.cms.service;

import com.vt.cms.model.dto.CreatedCampainRequest;
import org.springframework.stereotype.Service;


public interface CampainService {
    void createdCampain(CreatedCampainRequest request);
    void updatestatus(int campainID, String status);
     void getlist();
     void deletecampain(Integer campainID);
}
