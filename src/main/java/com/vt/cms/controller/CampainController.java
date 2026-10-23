package com.vt.cms.controller;

import com.vt.cms.model.dto.CreatedCampainRequest;
import com.vt.cms.model.dto.page.PagingResponse;
import com.vt.cms.model.enums.CampainStatus;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.model.resp.BaseResponse;
import com.vt.cms.model.resp.CampainResponse;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.CampainService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j

@RestController
@RequestMapping("v1/campain")
//Thêm mới chiến dịch (1 campain nhiều sản phẩm)
public class CampainController {
    private final CampainService campainService;
    public CampainController(CampainService campainService) {
        this.campainService = campainService ;
    }
    @PostMapping("/add")
    /**
     * Tạo mới dữ liệu create campain; nhận các tham số khai báo trong chữ ký hàm và trả về kết quả theo kiểu dữ liệu đã định nghĩa.
     */
    public ResponseEntity<APIRessponse> createCampain(@RequestBody CreatedCampainRequest request) {
        campainService.createdCampain(request);
        return ResponseEntity.ok(new
                APIRessponse(200, "Tạo chiến dịch thành công"));
    }
    // Lấy danh sách chiến dịch
    @GetMapping("/listcampain")
    /**
     * Lấy và trả về dữ liệu get campain; nhận các tham số khai báo trong chữ ký hàm và trả về kết quả theo kiểu dữ liệu đã định nghĩa.
     */
    public ResponseEntity<APIRessponse> getCampain() {
        return ResponseEntity.ok(new APIRessponse(200, "Lấy danh sách chiến dịch thành công",
                campainService.getlist()));
    }
    //Thay đổi trạng thái chiến dịch(Duyệt, kết thúc chiến dịch)
    @PutMapping("/status/{id}/{status}")
    /**
     * Cập nhật dữ liệu update campain; nhận các tham số khai báo trong chữ ký hàm và trả về kết quả theo kiểu dữ liệu đã định nghĩa.
     */
    public ResponseEntity<APIRessponse> updateCampain(   @PathVariable("id") Integer campain_id,
                                                         @PathVariable("status") CampainStatus status) {
        campainService.updatestatus(campain_id, status);
        return  ResponseEntity.ok(new APIRessponse(200,"Cập nhật trạng thái thành công"));
    }
    // Xóa chiến dịch
    @DeleteMapping("/deletecampain/{id}")
    /**
     * Xóa dữ liệu delete campain; nhận các tham số khai báo trong chữ ký hàm và trả về kết quả theo kiểu dữ liệu đã định nghĩa.
     */
    public ResponseEntity<APIRessponse> deleteCampain( @PathVariable("id") Integer campainId) {
      campainService.deletecampain(campainId);
      return ResponseEntity.ok(new APIRessponse(200,"Xóa chến dịch thành công"));

    }
}

