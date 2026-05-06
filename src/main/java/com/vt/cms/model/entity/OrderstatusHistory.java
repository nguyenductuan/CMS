package com.vt.cms.model.entity;

import com.vt.cms.model.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderstatusHistory {
    private Integer orderid;
    private OrderStatus status;

}
