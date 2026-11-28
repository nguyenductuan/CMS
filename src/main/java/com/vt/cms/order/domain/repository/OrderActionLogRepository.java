package com.vt.cms.order.domain.repository;

import com.vt.cms.order.domain.entity.OrderActionLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderActionLogRepository {

  void saveorder(OrderActionLog orderActionLog);
}
