# Lịch sử tác động đơn hàng

## Mục đích

Lưu vết các hành động làm thay đổi trạng thái đơn hàng. Mỗi sự kiện gồm:
- `createdAt`: thời gian phát sinh, API trả về theo định dạng `dd/MM/yyyy HH:mm`.
- `actor`: tài khoản/tác nhân thực hiện.
- `actorRole`: `BUYER`, `SELLER`, `ADMIN`, `SYSTEM`, `CARRIER`.
- `eventCode`: mã nghiệp vụ hoặc mã tracking.
- `statusCode`: trạng thái đơn hàng sau tác động.
- `title`: mô tả ngắn.
- `description`: mô tả chi tiết, bao gồm lý do khi có.

Các sự kiện được sắp xếp tăng dần theo `created_at`, sau đó theo `id`.

## API

### Lấy lịch sử đơn hàng

`GET /order/{id}/history`

Trả về danh sách sự kiện theo thứ tự thời gian tăng dần.

### Chi tiết đơn hàng

`GET /order/{id}`

Response được bổ sung trường `orderHistory` với cùng cấu trúc danh sách lịch sử.

### Callback trạng thái vận chuyển

`POST /api/orders/{orderId}/carrier/tracking-event`

Request:

```json
{
  "code": "103",
  "description": "Đơn vị vận chuyển đã lấy hàng tại kho"
}
```

Các mã được hỗ trợ: `101`, `103`, `104`, `320`, `410`, `400`, `500`, `506`, `507`, `508`, `509`, `505`.

> Endpoint callback hiện phục vụ tích hợp/demo. Khi triển khai thật cần xác thực chữ ký webhook và đối chiếu mã vận đơn từ đơn vị vận chuyển.

## Các sự kiện đang được ghi nhận

| Actor | Mã sự kiện | Hoạt động |
|---|---:|---|
| Buyer | 001 | Đơn hàng đã được đặt |
| System | 005 | Đơn hàng thanh toán thành công, Nhà bán chuẩn bị hàng |
| Seller/System | 006 | Đơn hàng bị hủy; phần mô tả lưu lý do hủy |
| Seller | 008 | Seller xác nhận đã chuẩn bị hàng |
| Carrier | 101 | Lấy hàng thất bại |
| Carrier | 103, 104 | Lấy hàng thành công/đã nhận hàng |
| Carrier | 320, 410, 400, 500, 506–509 | Đang vận chuyển/đang giao |
| Carrier | 505 | Giao hàng không thành công |
| Carrier | CARRIER_DELIVERY_SUCCESS | Giao hàng thành công theo callback giao hàng hiện có |
| Buyer | BUYER_RECEIVED | Buyer nhấn Đã nhận hàng |
| Buyer | BUYER_RETURN_REQUESTED | Buyer tạo yêu cầu Trả hàng/Hoàn tiền |
| Buyer | BUYER_RETURN_REQUEST_CANCELLED | Buyer hủy yêu cầu trả hàng |
| Seller | SELLER_RETURN_APPROVED / SELLER_RETURN_REJECTED | Seller phê duyệt/từ chối yêu cầu |
| Admin | ADMIN_RETURN_APPROVED / ADMIN_RETURN_REJECTED | Admin phê duyệt/từ chối yêu cầu |
| Seller | SELLER_RETURN_METHOD_SELECTED | Seller chọn phương thức trả hàng/hoàn tiền |
| System | SYSTEM_ESCALATED_TO_ADMIN | Quá 24 giờ Seller chưa duyệt, hệ thống chuyển lên Admin |
| System | SYSTEM_AUTO_RETURN_METHOD | Quá 24 giờ chưa chọn phương thức, hệ thống tự chọn Trả hàng & Hoàn tiền |

## Cấu trúc lưu trữ

Bảng `order_history` được mở rộng thêm `description`, `actor`, `actor_role`, `event_code`, `created_at` và index theo `order_id, created_at, id`.

Chạy migration `src/main/resources/db/migration/V20261103__order_history_actor_audit.sql` trước khi triển khai phiên bản này.

## Ghi chú phạm vi

- Sự kiện được ghi cùng transaction với thao tác nghiệp vụ trong các luồng đã tích hợp, tránh trạng thái đổi nhưng lịch sử không được lưu.
- Mã hủy tự động đơn chưa thanh toán `003` chỉ ghi nhận khi có job tự động hủy được bật trong ứng dụng. Repository hiện tại chưa tìm thấy job hết hạn thanh toán riêng để gắn lịch sử.
- Các mã vận chuyển được lưu theo callback tracking. Callback giao hàng thành công hiện có cũng có event riêng.
