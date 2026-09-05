package com.vt.cms.controller;

import org.springframework.web.bind.annotation.*;

@RequestMapping
@RestController
//Thêm mới campain (1 campain nhiều sản phẩm)
public class CampainController {
    @PostMapping("/campain")
    public String createCampain() {
        return "Campain created successfully";
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

