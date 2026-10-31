package com.vt.cms.model.repository;

import com.vt.cms.model.dto.OrderExportRow;
import com.vt.cms.model.entity.OrderExportHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderExportRepository {
    List<OrderExportRow> findOrdersForExport(@Param("fromDate") LocalDateTime fromDate,
                                              @Param("toDateExclusive") LocalDateTime toDateExclusive);
    int insertExportHistory(OrderExportHistory history);
    List<OrderExportHistory> findExportHistory(@Param("limit") Integer limit);
}