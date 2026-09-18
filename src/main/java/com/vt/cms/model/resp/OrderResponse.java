package com.vt.cms.model.resp;


import com.vt.cms.model.dto.ItemDto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class OrderResponse {
private Integer orderId;
private String status;
private Integer campainId;
private LocalDateTime order_date;
private String order_status;
private LocalDateTime delivery_at;
private LocalDateTime receive_at;
private LocalDateTime confirm_dealine_at;
private LocalDateTime expected_delivery_from;
private  LocalDateTime expected_delivery_to;
private List<ItemDto> orderItems;

}
