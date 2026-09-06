package com.vt.cms.model.dto;

import com.vt.cms.model.entity.Order;
import lombok.Data;

import java.util.List;
@Data
public class OrderShippingRequest {
   private  List<Order> orders;
    private  String note;
   private List<WarehouseSellerinfo> sellerInfos;

}
