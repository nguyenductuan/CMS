package com.vt.cms.controller;

import com.vt.cms.security.JwtAuthenticationFilter;
import com.vt.cms.service.OrderExportService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class OrderExportController {
    private final OrderExportService service;
    public OrderExportController(OrderExportService service) { this.service = service; }

    /** Tải báo cáo Excel tab Orders; fromDate/toDate có định dạng yyyy-MM-dd và phải truyền cùng nhau. */
    @GetMapping("/order/export")
    public ResponseEntity<byte[]> exportOrders(
            @RequestParam(defaultValue = "Orders") String tabName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            HttpServletRequest request) {
        String actor = (String) request.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE);
        var file = service.export(tabName, fromDate, toDate, actor);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + java.net.URLEncoder.encode(file.fileName(), java.nio.charset.StandardCharsets.UTF_8).replace("+", "%20"))
                .header("X-Exported-Rows", Integer.toString(file.rowCount()))
                .body(file.content());
    }

    /** Lấy 100 lần xuất báo cáo gần nhất. */
    @GetMapping("/order/export-history")
    public ResponseEntity<?> exportHistory() { return ResponseEntity.ok(service.history()); }
}