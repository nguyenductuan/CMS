package com.vt.cms.campain.api;

import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.base.response.APIRessponse;
import com.vt.cms.base.response.BaseResponse;
import com.vt.cms.campain.application.dto.request.CreatedCampainRequest;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import com.vt.cms.campain.application.service.CampainService;
import com.vt.cms.campain.domain.enums.CampainStatus;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("v1/campain")
@SecurityRequirement(name = "bearerAuth")
// Thêm mới chiến dịch (1 campain nhiều sản phẩm)
public class CampainController {

  private final CampainService campainService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public CampainController(
      CampainService campainService, JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.campainService = campainService;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @PostMapping("/add")
  public ResponseEntity<APIRessponse> createCampain(
      @RequestBody CreatedCampainRequest request, HttpServletRequest httpRequest) {
    Integer userId = (Integer) httpRequest.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);
    campainService.createdCampain(request, userId);
    return ResponseEntity.ok(new APIRessponse(200, "Tạo chiến dịch thành công"));
  }

  // Lấy danh sách chiến dịch
  @GetMapping("/listcampain")
  public BaseResponse<PagingResponse<List<CampainResponse>>> getCampain(
      @ModelAttribute Pagerequest request) {
    return campainService.getlist(request);
  }

  // Thay đổi trạng thái chiến dịch
  @PutMapping("/status/{id}/{status}")
  public ResponseEntity<APIRessponse> updateCampain(
      @PathVariable("id") Integer campain_id,
      @PathVariable("status") CampainStatus status,
      HttpServletRequest httpRequest) {
    log.info("Request update status campain");
    Integer userId = (Integer) httpRequest.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);
    campainService.updatestatus(campain_id, status, userId);
    return ResponseEntity.ok(new APIRessponse(200, "Cập nhật trạng thái thành công"));
  }

  // Kết thúc chiến dịch
  @PutMapping("/finish_campain/{id}")
  public ResponseEntity<APIRessponse> finishCampain(
      @PathVariable("id") Integer campaignId, HttpServletRequest request) {
    log.info("Start finish Campain");
    Integer userID = (Integer) request.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);
    campainService.finishCampain(campaignId, userID);
    return ResponseEntity.ok(new APIRessponse(200, "Kết thúc chiến dịch thành công"));
  }

  // Chi tiết chiến dịch

  /* TODO
   * 1. Bổ sung APi chi tiết sp khi chiến dịch hết hạn gồm thông tin các sku của product /product/get-product-not-campaign
   *  2. Bổ sung APi chi tiueets sp khi chiến dịch đang dienx ra gồm thông tin các sku trong product campaign/get-campaign-by-id
   *  3. Đối với Chi tiết chiến dịch khi chưa hết hạn thì cho phép dừng chiến dịch. Nếu Chi tiết chiến dịch hết hạn cho phép đăng lại
   *  4. Lấy danh sách sản phẩm đã bán trong chi tiết chiến dịch gồm các thông tin (API campaign/get-buyer-order-by-id):
   * Danh sách nguwoif mua(số người mua)
   * Bảng danh sách gồm các thông tin: Ngưới mua, Mã đơn, Tổng SL,Tổng tiền, thời gian, Trạng thái
   */
  @GetMapping("/deaitlCampain/{id}")
  public ResponseEntity<APIRessponse> deaitlCampain(@PathVariable("id") Integer campain_id) {
    CampainResponse response = campainService.deaitlCampain(campain_id);
    return ResponseEntity.ok(new APIRessponse(200, "Thành công", response));
  }

  // Xóa chiến dịch
  @DeleteMapping("/deletecampain/{id}")
  public ResponseEntity<APIRessponse> deleteCampain(
      @PathVariable("id") Integer campainId, HttpServletRequest httpRequest) {
    log.info("Request delete campain");
    Integer userId = (Integer) httpRequest.getAttribute(JwtAuthenticationFilter.USER_ID_ATTRIBUTE);
    campainService.deletecampain(campainId, userId);

    return ResponseEntity.ok(new APIRessponse(200, "Xóa chến dịch thành công"));
  }
}
