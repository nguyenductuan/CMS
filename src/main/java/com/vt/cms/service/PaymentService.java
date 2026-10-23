package com.vt.cms.service;

import com.vt.cms.model.dto.PaymentRequest;
import com.vt.cms.model.dto.Paymentcallback;
import com.vt.cms.model.repository.PaymentRepostitory;
import com.vt.cms.model.resp.PaymentResponse;

public interface PaymentService {

    /**
     * Xử lý thanh toán paymentcallback. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    void paymentcallback(Paymentcallback paymentcallback);
    /**
     * Xử lý thanh toán payment. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    PaymentResponse payment(PaymentRequest paymentRequest);

}
