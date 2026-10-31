package com.vt.cms.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

/** Lịch sử tạo file báo cáo đơn hàng. */
@Data
public class OrderExportHistory {
    private Long id;
    private LocalDateTime exportedAt;
    private String tabName;
    private String fileName;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String exportedBy;
    private Integer rowCount;
}