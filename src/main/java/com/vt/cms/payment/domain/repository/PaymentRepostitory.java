//package com.vt.cms.payment.domain.repository;
//
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//
//@Mapper
//public interface PaymentRepostitory {
////
////    int insertpayment(Payment payment);
////
//   /**
//    * Cập nhật updatepayment. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
//    */
//   int updatepayment(String transactionCode);
//    /**
//     * Lưu save. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
//     */
//    void save(Payment payment);
//    /**
//     * Xử lý thanh toán paymentbytransaction. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
//     */
//    Payment paymentbytransaction(String transactionCode);
//
//    int save(Payment payment);
//    Payment findByTransactionCode(@Param("transactionCode") String transactionCode);
//    Payment findByTransactionCodeForUpdate(@Param("transactionCode") String transactionCode);
//    int countPendingByOrderId(@Param("orderId") Integer orderId);
//    int markSuccessIfPending(@Param("transactionCode") String transactionCode);
//    int markRefundPendingByOrderId(@Param("orderId") Integer orderId);
//}
