package com.vt.cms.payment.domain.repository;

import com.vt.cms.payment.domain.entity.Paymentorder;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentOrderRepostitory {
  int savepaymentorder(@Param("paymentorder") List<Paymentorder> paymentorder);

  List<Paymentorder> getOrderByPayment(@Param("paymentId") Integer paymentId);
}
