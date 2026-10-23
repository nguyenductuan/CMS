package com.vt.cms.service;

import com.vt.cms.model.dto.CreatedCampainRequest;
import com.vt.cms.model.enums.CampainStatus;
import com.vt.cms.model.resp.CampainResponse;
import java.util.List;


public interface CampainService {
    /**
     * Tạo mới created campain. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    void createdCampain(CreatedCampainRequest request);
    /**
     * Cập nhật updatestatus. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    void updatestatus(Integer campain_id, CampainStatus status);
     /**
      * Lấy dữ liệu getlist. Tham số và kết quả tuân theo chữ ký phương thức.
      */
     List<CampainResponse> getlist();
     /**
      * Xóa deletecampain. Tham số và kết quả tuân theo chữ ký phương thức.
      */
     void deletecampain(Integer campainID);

}
