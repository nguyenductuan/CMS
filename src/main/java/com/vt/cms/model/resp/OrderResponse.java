package com.vt.cms.model.resp;


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
}
