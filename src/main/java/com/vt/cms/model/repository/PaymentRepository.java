package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentRepository {
    int save(Payment payment);
    Payment findByTransactionCode(@Param("transactionCode") String transactionCode);
    Payment findByTransactionCodeForUpdate(@Param("transactionCode") String transactionCode);
    int countPendingByOrderId(@Param("orderId") Integer orderId);
    int markSuccessIfPending(@Param("transactionCode") String transactionCode);
    int markRefundPendingByOrderId(@Param("orderId") Integer orderId);
}
