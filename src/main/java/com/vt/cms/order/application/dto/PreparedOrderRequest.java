package com.vt.cms.order.application.dto;
import com.vt.cms.model.dto.OrderRequest;
/** Authenticated order command at the application boundary. */
public record PreparedOrderRequest(OrderRequest orderRequest,String buyerUsername) {}