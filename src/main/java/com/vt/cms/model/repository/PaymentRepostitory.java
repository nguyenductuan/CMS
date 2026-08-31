package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Payment;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentRepostitory {

    void insertpayment(Payment payment);
}
