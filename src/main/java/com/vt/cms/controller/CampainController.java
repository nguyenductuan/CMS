package com.vt.cms.controller;

import com.vt.cms.model.dto.CreatedCampainRequest;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.service.CampainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController

//Thêm mới campain (1 campain nhiều sản phẩm)
public class CampainController {
    private CampainService campainService;
    @PostMapping("/campain")
    public ResponseEntity<APIRessponse> createCampain(@RequestBody CreatedCampainRequest request) {
        campainService.createdCampain(request);
        return ResponseEntity.ok(new
                APIRessponse(200, "Tạo chiến dịch thành công"));
    }
    // Lấy danh sách campain
    @GetMapping("/campain")
    public String getCampain() {
        return "Get campain successfully";
    }
    @PutMapping("/campain")
    public String updateCampain() {
        return "Campain updated successfully";
    }
    @DeleteMapping("/campain")
    public String deleteCampain() {
        return "Campain deleted successfully";
    }
}

