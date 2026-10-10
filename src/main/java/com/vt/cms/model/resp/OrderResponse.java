package com.vt.cms.model.resp;


import com.vt.cms.model.dto.ItemDto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class OrderResponse {
private Integer orderId;

    private String orderStatus;
    private BigDecimal total;
    private LocalDateTime orderDate;


private LocalDateTime delivery_at;
private LocalDateTime receive_at;
private LocalDateTime confirm_dealine_at;
private LocalDateTime expected_delivery;
private String createdBy;
private List<ItemDto> orderItems;

}
