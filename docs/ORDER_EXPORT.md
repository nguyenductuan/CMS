# Xuất Excel và lịch sử xuất báo cáo đơn hàng

## API xuất Excel

`GET /order/export`

Query parameters:
- `tabName`: tên tab; mặc định `Orders`. Hỗ trợ các tab: `Orders`/`Tất cả`, `Chờ thanh toán`, `Đã thanh toán`/`Chờ xác nhận`, `Chờ giao hàng`, `Đang giao`, `Đã giao`, `Đã hủy`, `Trả hàng/Hoàn tiền`. Mỗi tab tự lọc theo trạng thái đơn tương ứng.
- `fromDate`: ngày bắt đầu, định dạng `yyyy-MM-dd`, không bắt buộc.
- `toDate`: ngày kết thúc, định dạng `yyyy-MM-dd`, không bắt buộc. Phải truyền cùng `fromDate`.

Ví dụ có lọc ngày:

`GET /order/export?tabName=Orders&fromDate=2026-10-01&toDate=2026-10-31`

Ví dụ không lọc ngày:

`GET /order/export?tabName=Orders`

Tên file:
- Có khoảng thời gian: `Orders _01-10-2026 - 31-10-2026.xlsx` (dùng dấu `-` thay cho `/` trong phần ngày)
- Không chọn thời gian: `Orders.xlsx`

Dấu gạch ngang được dùng thay cho dấu `/` trong tên file vì dấu gạch chéo là ký tự phân tách thư mục trên nhiều hệ điều hành. Ngày trong tên file vẫn theo thứ tự ngày-tháng-năm.

File có 35 cột theo mẫu Excel tab Orders đã cung cấp; mỗi dòng là một SKU thuộc đơn hàng. Một số cột không có dữ liệu tương ứng trong schema hiện tại (ví dụ chiết khấu sàn, phí vận chuyển tách riêng, thời gian thanh toán, phương thức thanh toán, tỉnh/phường) sẽ để trống thay vì tự suy diễn.

## API lịch sử xuất

`GET /order/export-history`

Trả về tối đa 100 lần xuất gần nhất, gồm:
- Thời gian xuất (`exportedAt`)
- Tab xuất (`tabName`)
- Tên file (`fileName`)
- Khoảng thời gian lọc (`fromDate`, `toDate`)
- Người xuất (`exportedBy`)
- Số dòng dữ liệu (`rowCount`)

Hai API yêu cầu JWT Bearer và quyền ADMIN.

## Database

Chạy migration `src/main/resources/db/migration/V20261105__order_export_history.sql` để tạo bảng `order_export_history`.
