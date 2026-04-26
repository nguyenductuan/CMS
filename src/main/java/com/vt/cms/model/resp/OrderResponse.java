package com.vt.cms.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vt.cms.model.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrderResponse {
    @Schema(description = "ID của đơn hàng")
    @JsonProperty("id")
    private Integer id;
    @Schema(description = "Tổng tiền")
    @JsonProperty("total")
    private Integer total;
    @Schema(description = "Trạng thái đơn hàng")
    @JsonProperty("status")
    private OrderStatus status;
}
