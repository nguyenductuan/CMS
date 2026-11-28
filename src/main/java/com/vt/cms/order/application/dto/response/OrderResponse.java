package com.vt.cms.order.application.dto.response;

import com.vt.cms.order.domain.enums.OrderStatus;
import com.vt.cms.order.domain.enums.OrderSubStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderResponse {
  private Integer id;
  private String productId;
  private String productName;
  private String jsonMedias;
  private String jsonAttributes;
  private String jsonAttributesName;
  private String productImages;
  private String typeProducts;
  private String typeProductsName;
  private String buyerName;
  private String buyerAvatar;
  private String buyerUserId;
  private Long buyerId;
  private BigDecimal totalAmount;
  private OrderStatus status;
  private Boolean isCompleted;
  private String trackingNumber;
  private LocalDateTime orderDate;
  private String reasonDetail;
  private LocalDateTime cancelAt;
  private String cancelByName;
  private BigDecimal totalPayment;
  private List<OrderItemDto> orderitems;
  private OrderSubStatus subStatus;
  private LocalDateTime expectedDeliveryFrom;
  private LocalDateTime expectedDeliveryTo;
}
