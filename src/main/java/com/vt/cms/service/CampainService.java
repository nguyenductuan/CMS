package com.vt.cms.service;

import com.vt.cms.model.dto.CreatedCampainRequest;
import com.vt.cms.model.enums.CampainStatus;
import com.vt.cms.model.resp.CampainResponse;
import java.util.List;


public interface CampainService {
    void createdCampain(CreatedCampainRequest request);
    void updatestatus(Integer campain_id, CampainStatus status);
     List<CampainResponse> getlist();
     void deletecampain(Integer campainID);

}
