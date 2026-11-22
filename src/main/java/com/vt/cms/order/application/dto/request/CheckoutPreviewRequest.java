package com.vt.cms.order.application.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class CheckoutPreviewRequest {
  List<CheckoutPreviewItem> item;
}
