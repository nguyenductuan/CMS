package com.vt.cms.campain.application.service;

import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.base.response.BaseResponse;
import com.vt.cms.campain.application.dto.request.CreatedCampainRequest;
import com.vt.cms.campain.application.dto.response.CampaignProductResponse;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import com.vt.cms.campain.domain.enums.CampainStatus;
import com.vt.cms.model.dto.page.PagingResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface CampainService {
  /** Tạo mới created campain. Tham số và kết quả tuân theo chữ ký phương thức. */
  void createdCampain(CreatedCampainRequest request, Integer userId);

  /** Cập nhật updatestatus. Tham số và kết quả tuân theo chữ ký phương thức. */
  void updatestatus(Integer campain_id, CampainStatus status, Integer userId);

  CampainResponse deaitlCampain(Integer campain_id);

  /** Lấy dữ liệu getlist. Tham số và kết quả tuân theo chữ ký phương thức. */
  BaseResponse<PagingResponse<List<CampainResponse>>> getlist(Pagerequest request);

  /** Xóa deletecampain. Tham số và kết quả tuân theo chữ ký phương thức. */
  void deletecampain(Integer campainId, Integer userId);

  void finishCampain(Integer campaignId, Integer userId);

  List<CampainResponse> getCampaignInfoById(Set<Integer> campainids);

  List<CampaignProductResponse> findCampaignProductByCampaignIdIn(Set<Integer> campainids);

  boolean isCampaignExpried(LocalDateTime endtime);
}
