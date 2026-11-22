package com.vt.cms.order.application.dto.response;

import com.vt.cms.Shipping.application.ShippingMethodDTO;
import com.vt.cms.payment.domain.enums.PaymentMethod;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CheckoutPreviewResponse {
  private List<SkuPreviewDto> skus;
  private PaymentMethod paymentMethod;
  private BigDecimal totalPrice;
  private ShippingMethodDTO shippingMethod;
  //  private BigDecimal shipping_fee;
}
