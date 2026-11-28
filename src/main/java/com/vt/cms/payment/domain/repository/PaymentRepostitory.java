package com.vt.cms.payment.domain.repository;

import com.vt.cms.payment.domain.entity.Payment;
import com.vt.cms.payment.domain.enums.PaymentStatus;
import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentRepostitory {
  int savepayment(Payment payment);

  int updatepayment(
      @Param("paymentId") Integer paymentId,
      @Param("timeTransaction") LocalDateTime timeTransaction,
      @Param("paymentStatus") PaymentStatus paymentStatus);

  Payment getPaymentById(@Param("transaction_code") String transaction_code);
}
