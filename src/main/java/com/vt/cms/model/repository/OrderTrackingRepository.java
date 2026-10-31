package com.vt.cms.model.repository;

import com.vt.cms.model.entity.OrderTracking;
import com.vt.cms.model.enums.OrderStatus;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderTrackingRepository {


    /**
     * Thêm bản ghi insertordertracking. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    void insertordertracking(OrderTracking tracking);

    /** Lấy lịch sử thao tác theo thời gian tăng dần. */
    java.util.List<OrderTracking> findByOrderId(@org.apache.ibatis.annotations.Param("orderId") Integer orderId);
}
