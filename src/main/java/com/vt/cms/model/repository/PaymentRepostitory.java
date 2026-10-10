package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Payment;
import com.vt.cms.model.resp.PaymentResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentRepostitory {
//
//    int insertpayment(Payment payment);
//
   int updatepayment(String transactionCode);
    void save(Payment payment);
    Payment paymentbytransaction(String transactionCode);
}
