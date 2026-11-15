package com.vt.cms.campain.api;

import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.base.response.APIRessponse;
import com.vt.cms.base.response.BaseResponse;
import com.vt.cms.campain.application.dto.request.CreatedCampainRequest;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import com.vt.cms.campain.application.service.CampainService;
import com.vt.cms.campain.domain.enums.CampainStatus;
import com.vt.cms.model.dto.page.PagingResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("v1/campain")
// Thêm mới chiến dịch (1 campain nhiều sản phẩm)
public class CampainController {

  private final CampainService campainService;

  public CampainController(CampainService campainService) {
    this.campainService = campainService;
  }

  @PostMapping("/add")
  public ResponseEntity<APIRessponse> createCampain(@RequestBody CreatedCampainRequest request) {
    campainService.createdCampain(request);
    return ResponseEntity.ok(new APIRessponse(200, "Tạo chiến dịch thành công"));
  }

  // Lấy danh sách chiến dịch
  @GetMapping("/listcampain")
  public BaseResponse<PagingResponse<List<CampainResponse>>> getCampain(
      @ModelAttribute Pagerequest request) {
    return campainService.getlist(request);
  }

  // Thay đổi trạng thái chiến dịch(Duyệt, kết thúc chiến dịch)
  @PutMapping("/status/{id}/{status}")
  public ResponseEntity<APIRessponse> updateCampain(
      @PathVariable("id") Integer campain_id, @PathVariable("status") CampainStatus status) {
    campainService.updatestatus(campain_id, status);
    return ResponseEntity.ok(new APIRessponse(200, "Cập nhật trạng thái thành công"));
  }

  // Xóa chiến dịch
  @DeleteMapping("/deletecampain/{id}")
  public ResponseEntity<APIRessponse> deleteCampain(@PathVariable("id") Integer campainId) {
    campainService.deletecampain(campainId);
    return ResponseEntity.ok(new APIRessponse(200, "Xóa chến dịch thành công"));
  }
}
