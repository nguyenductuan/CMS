package com.vt.cms.campain.application.service;

import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.base.response.BaseResponse;
import com.vt.cms.campain.application.dto.request.CreatedCampainRequest;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import com.vt.cms.campain.domain.enity.Campain;
import com.vt.cms.campain.domain.enums.CampainStatus;
import com.vt.cms.model.dto.page.PagingResponse;
import java.util.List;

public interface CampainService {
  /** Tạo mới created campain. Tham số và kết quả tuân theo chữ ký phương thức. */
  void createdCampain(CreatedCampainRequest request, Integer userId);

  /** Cập nhật updatestatus. Tham số và kết quả tuân theo chữ ký phương thức. */
  void updatestatus(Integer campain_id, CampainStatus status, Integer userId);

  Campain deaitlCampain(Integer campainId);

  /** Lấy dữ liệu getlist. Tham số và kết quả tuân theo chữ ký phương thức. */
  BaseResponse<PagingResponse<List<CampainResponse>>> getlist(Pagerequest request);

  /** Xóa deletecampain. Tham số và kết quả tuân theo chữ ký phương thức. */
  void deletecampain(Integer campainId, Integer userId);

  void finishCampain(Integer campainId, Integer userId);
}
