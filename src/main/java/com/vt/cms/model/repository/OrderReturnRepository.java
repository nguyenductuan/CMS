package com.vt.cms.model.repository;

import com.vt.cms.model.entity.OrderReturn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;

@Mapper
public interface OrderReturnRepository {
    int insert(OrderReturn request);
    OrderReturn findById(@Param("returnId") Integer returnId);
    int countByOrderId(@Param("orderId") Integer orderId);
    java.util.List<OrderReturn> findPending(@Param("status") String status);
    int sellerReview(@Param("returnId") Integer returnId, @Param("approved") boolean approved,
                     @Param("reviewer") String reviewer, @Param("note") String note,
                     @Param("reviewedAt") LocalDateTime reviewedAt);
    int adminReview(@Param("returnId") Integer returnId, @Param("approved") boolean approved,
                    @Param("reviewer") String reviewer, @Param("note") String note,
                    @Param("reviewedAt") LocalDateTime reviewedAt);
    int escalateExpiredSellerReviews(@Param("cutoff") LocalDateTime cutoff);
}
