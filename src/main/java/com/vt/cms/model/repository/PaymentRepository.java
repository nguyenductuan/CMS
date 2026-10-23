package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentRepository {
    int save(Payment payment);
    Payment findByTransactionCodeForUpdate(@Param("transactionCode") String transactionCode);
    int markSuccessIfPending(@Param("transactionCode") String transactionCode);
}
