package com.vt.cms.order.domain.repository;

import com.vt.cms.order.domain.entity.OrderTracking;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderTrackingRepository {
  void saveAll(@Param("list") List<OrderTracking> orderTracking);

  int createtracking(@Param("orderIds") List<OrderTracking> orderTracking);
}
