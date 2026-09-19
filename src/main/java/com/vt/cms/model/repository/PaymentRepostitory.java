package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Payment;
import org.mapstruct.Mapper;


public interface PaymentRepostitory {

    int insertpayment(Payment payment);

    int updatepayment(String transactionCode);

    Payment paymentbytransaction(String transactionCode);
}
