package com.vt.cms.checkout.application.dto.request;

import com.vt.cms.model.dto.ItemRequest;
import java.util.List;
import lombok.Data;

@Data
public class CheckoutPreviewRequest {
  private List<ItemRequest> items;
  private String shippingServiceCode;
  private String recipientName;
  private String recipientPhone;
  private String deliveryAddress;
}
