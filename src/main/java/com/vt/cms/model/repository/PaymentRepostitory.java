package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Payment;
import com.vt.cms.model.resp.PaymentResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentRepostitory {
//
//    int insertpayment(Payment payment);
//
   /**
    * Cập nhật updatepayment. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
    */
   int updatepayment(String transactionCode);
    /**
     * Lưu save. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    void save(Payment payment);
    /**
     * Xử lý thanh toán paymentbytransaction. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    Payment paymentbytransaction(String transactionCode);
}
