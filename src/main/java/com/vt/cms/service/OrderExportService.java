package com.vt.cms.service;

import com.vt.cms.model.entity.OrderExportHistory;
import com.vt.cms.model.repository.OrderExportRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Xuất báo cáo Excel theo cấu trúc tab Orders và ghi nhận lịch sử xuất file. */
@Service
public class OrderExportService {
    private static final DateTimeFormatter FILE_DATE = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter CELL_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String[] HEADERS = {
        "Mã đơn hàng\n(1)", "Ngày đặt hàng\n(2)", "Trạng Thái Đơn Hàng\n(3)", "Lưu ý từ người mua\n(4)",
        "Mã vận đơn\n(5)", "Đơn Vị Vận Chuyển\n(6)", "Phương thức giao hàng\n(7)", "Ngày giao hàng dự kiến\n(8)",
        "Thời gian giao hàng\n(9)", "Tên sản phẩm\n(10)", "Cân nặng sản phẩm\n(11)", "Tổng cân nặng\n(12)",
        "SKU phân loại hàng\n(13)", "Tên phân loại hàng\n(14)", "Giá gốc\n(15)", "Tổng giá bán (sản phẩm)\n(16)",
        "Số lượng\n(17)", "Tổng giá trị đơn hàng (VND)\n(18)=(16)x(17)", "Phí chiết khấu sàn\n(19)",
        "Phí vận chuyển (dự kiến)\n(20)", "Phí vận chuyển mà người mua trả\n(21)",
        "Tổng số tiền người mua thanh toán\n(22)=(18)+(21)", "Thời gian hoàn thành đơn hàng\n(23)",
        "Thời gian đơn hàng được thanh toán\n(24)", "Phương thức thanh toán\n(25)",
        "Doanh thu dự kiến trên đơn\n(26)=(22)-VAT", "Nhà bán\n(27)", "Người Mua\n(28)",
        "Tên Người nhận\n(29)", "Số điện thoại\n(30)", "Quốc gia\n(31)", "Tỉnh/Thành phố\n(32)",
        "Phường/Xã\n(33)", "Địa chỉ nhận hàng\n(34)",
        "Ghi chú nhà bán (chuẩn bị đơn hàng bàn giao đơn vị vận chuyển)\n(35)"
    };

    private final OrderExportRepository repository;
    public OrderExportService(OrderExportRepository repository) { this.repository = repository; }

    @Transactional
    public ExportedOrderFile export(String tabName, LocalDate from, LocalDate to, String actor) {
        String normalizedTab = (tabName == null || tabName.isBlank()) ? "Orders" : tabName.trim();
        if (!"Orders".equalsIgnoreCase(normalizedTab))
            throw new IllegalArgumentException("Tab xuất hiện tại chỉ hỗ trợ: Orders");
        if ((from == null) != (to == null)) throw new IllegalArgumentException("Cần truyền cả fromDate và toDate");
        if (from != null && to.isBefore(from)) throw new IllegalArgumentException("toDate phải lớn hơn hoặc bằng fromDate");

        LocalDateTime fromTime = from == null ? null : from.atStartOfDay();
        LocalDateTime toExclusive = to == null ? null : to.plusDays(1).atStartOfDay();
        var rows = repository.findOrdersForExport(fromTime, toExclusive);
        String fileName = from == null ? normalizedTab + ".xlsx"
                : normalizedTab + " _" + from.format(FILE_DATE) + " - " + to.format(FILE_DATE) + ".xlsx";
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(normalizedTab);
            Row header = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont(); font.setBold(true); headerStyle.setFont(font);
            headerStyle.setWrapText(true); headerStyle.setAlignment(HorizontalAlignment.CENTER);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = header.createCell(i); cell.setCellValue(HEADERS[i]); cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, Math.min(HEADERS[i].length() * 230, 12000));
            }
            int rowIndex = 1;
            for (var item : rows) {
                Row row = sheet.createRow(rowIndex++);
                Object[] values = values(item);
                for (int c = 0; c < values.length; c++) {
                    Cell cell = row.createCell(c);
                    Object value = values[c];
                    if (value instanceof Number n) cell.setCellValue(n.doubleValue());
                    else if (value != null) cell.setCellValue(value.toString());
                }
            }
            sheet.createFreezePane(0, 1); sheet.setAutoFilter(new org.apache.poi.ss.util.CellRangeAddress(0, Math.max(0, rowIndex - 1), 0, HEADERS.length - 1));
            workbook.write(output);
            OrderExportHistory history = new OrderExportHistory();
            history.setExportedAt(LocalDateTime.now()); history.setTabName(normalizedTab); history.setFileName(fileName);
            history.setFromDate(fromTime); history.setToDate(to == null ? null : to.atTime(23,59,59));
            history.setExportedBy(actor); history.setRowCount(rows.size());
            repository.insertExportHistory(history);
            return new ExportedOrderFile(fileName, output.toByteArray(), rows.size());
        } catch (IOException e) { throw new IllegalStateException("Không thể tạo file Excel", e); }
    }

    private Object[] values(com.vt.cms.model.dto.OrderExportRow x) {
        return new Object[] {x.getOrderId(), fmt(x.getOrderDate()), x.getOrderStatus(), x.getBuyerNote(),
            x.getTrackingNumber(), x.getShippingProvider(), x.getDeliveryMethod(), fmt(x.getExpectedDelivery()),
            fmt(x.getShippedAt()), x.getProductName(), x.getProductWeight(), x.getTotalWeight(), x.getSkuCode(),
            x.getSkuName(), x.getOriginalPrice(), x.getSellingPrice(), x.getQuantity(), x.getOrderTotal(),
            x.getPlatformDiscount(), x.getEstimatedShippingFee(), x.getBuyerShippingFee(), x.getBuyerPaidTotal(),
            fmt(x.getCompletedAt()), fmt(x.getPaidAt()), x.getPaymentMethod(), x.getExpectedRevenue(),
            x.getSellerName(), x.getBuyerName(), x.getRecipientName(), x.getRecipientPhone(), x.getCountry(),
            x.getProvince(), x.getWard(), x.getDeliveryAddress(), x.getSellerNote()};
    }
    private String fmt(LocalDateTime value) { return value == null ? null : value.format(CELL_DATE); }

    public List<OrderExportHistory> history() { return repository.findExportHistory(100); }

    public record ExportedOrderFile(String fileName, byte[] content, int rowCount) {}
}