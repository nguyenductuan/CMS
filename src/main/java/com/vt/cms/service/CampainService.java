package com.vt.cms.service;

import com.vt.cms.model.dto.CreatedCampainRequest;
import org.springframework.stereotype.Service;

@Service
public interface CampainService {
    void createdCampain(CreatedCampainRequest request);
    void updatestatus(int campainID, String status);
}
