package com.vt.cms.order.api;

import com.vt.cms.model.dto.BulkOrderPreparationRequest;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.service.OrderFulfillmentService;
import com.vt.cms.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/s/v1/order")
@RequiredArgsConstructor
@Slf4j
public class SellerOrderController {
    private final OrderFulfillmentService fulfillmentService;

    /**
     * Chuẩn bị nhiều đơn cùng một kho và tạo vận đơn Viettel Post MOCK cho từng đơn.
     */
    @PutMapping("/prepared")
    public ResponseEntity<APIRessponse> preparedOrder(
            @Valid @RequestBody BulkOrderPreparationRequest request,
            HttpServletRequest http) {
        String actor = (String) http.getAttribute(JwtAuthenticationFilter.USERNAME_ATTRIBUTE);
        String actorRole = (String) http.getAttribute(JwtAuthenticationFilter.ROLE_ATTRIBUTE);
        var results = fulfillmentService.prepareMany(request, actor, actorRole);
        long success = results.stream().filter(item -> Boolean.TRUE.equals(item.get("success"))).count();
        return ResponseEntity.ok(new APIRessponse(200,
                "Đã xử lý " + results.size() + " đơn; tạo vận đơn thành công " + success + " đơn",
                results));
    }
}