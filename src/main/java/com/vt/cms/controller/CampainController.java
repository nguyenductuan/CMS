package com.vt.cms.controller;

import com.vt.cms.model.dto.CreatedCampainRequest;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.service.CampainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController

//Thêm mới chiến dịch (1 campain nhiều sản phẩm)
public class CampainController {
    private CampainService campainService;
    @PostMapping("/campain")
    public ResponseEntity<APIRessponse> createCampain(@RequestBody CreatedCampainRequest request) {
        campainService.createdCampain(request);
        return ResponseEntity.ok(new
                APIRessponse(200, "Tạo chiến dịch thành công"));
    }
    // Lấy danh sách chiến dịch: cờ xử lý, Đang diễn ra, Săp diễn ra, Đã kết thúc
    @GetMapping("/campain")
    public String getCampain() {
        return "Get campain successfully";
    }
    //Thay đổi trạng thái chiến dịch(Duyệt, kết thúc chiến dịch)
    @PostMapping("/campainstatus")
    public String updateCampain( @RequestBody int campainId, String status) {
        campainService.updatestatus(campainId, status);
        return "Campain updated successfully";
    }
    // Xóa chiến dịch
    @DeleteMapping("/campain")
    public String deleteCampain() {
        return "Campain deleted successfully";
    }
}

