# Sơ đồ refactor theo Feature Module

> Phạm vi: bản đồ đích cho các package/file Java hiện có, theo Clean Architecture kết hợp Layered Architecture. Đây là bước tổ chức và lộ trình di chuyển an toàn; không đổi URL API, schema, tên bảng hoặc SQL mapper trong đợt đầu.
>
> Nguyên tắc quan trọng: không di chuyển một lớp chỉ vì tên loại lớp (DTO/entity/repository). Xác định feature sở hữu nghiệp vụ trước. Các file dùng chung giữa nhiều feature chỉ đặt trong `shared` nếu thực sự không sở hữu nghiệp vụ; nếu không, giữ một contract trong feature sở hữu và gọi qua application port.

## 1. Cấu trúc đích

```text
com.vt.cms
├── CmsApplication.java
├── shared
│   ├── api                 # response envelope/error HTTP dùng chung
│   ├── application         # contract dùng chung thực sự
│   ├── config              # cấu hình Spring/MyBatis/security
│   ├── domain              # value objects, lỗi nghiệp vụ dùng chung
│   └── security            # JWT principal/filter
├── identity                # auth, user/account, buyer profile
├── catalog                 # product, SKU, review
├── campaign                # campaign và campaign-product
├── cart                    # cart, cart item
├── checkout                # preview/checkout orchestration
├── order                   # order, fulfillment, return, tracking, history, export
├── payment                 # payment/callback
├── shipping                # shipping, assignment, fee/provider
└── warehouse               # warehouse, inventory, movement
```

Các feature module dùng cùng một quy ước package:

```text
<feature>/
├── api/                              # Spring MVC controllers, transport mapping
├── application/
│   ├── dto/                          # command/query/result boundary DTO
│   ├── port/in/                      # use-case interfaces
│   ├── port/out/                     # persistence/integration interfaces
│   └── service/                      # use-case orchestration
├── domain/
│   ├── model/                        # domain model/entities/value objects
│   ├── repository/                   # domain-facing ports nếu phù hợp
│   └── service/                      # quy tắc domain thuần
└── infrastructure/
    ├── persistence/mybatis/          # Mapper/adapters, row mapping
    └── integration/                  # external providers/jobs
```

Luồng phụ thuộc: `api -> application -> domain`; `infrastructure -> application/domain ports`. Domain không phụ thuộc Spring, MyBatis, HTTP hoặc DTO API.

## 2. Bản đồ package/file hiện có -> đích

### Bootstrap và cross-cutting

| Hiện tại | Đích đề xuất | Ghi chú |
|---|---|---|
| `com.vt.cms.CmsApplication` | `com.vt.cms.CmsApplication` | Giữ root để component scan bao phủ các module. |
| `Exception/BusinessException.java` | `shared/domain/BusinessException.java` | Giữ constructor/hành vi hiện tại; cập nhật imports theo từng đợt. |
| `Exception/GlobalExceptionHandler.java` | `shared/api/GlobalExceptionHandler.java` | Chỉ HTTP exception translation thuộc API/shared. |
| `config/PasswordEncoderConfig.java` | `shared/config/PasswordEncoderConfig.java` | Cấu hình framework. |
| `config/SkuListTypeHandler.java` | `catalog/infrastructure/persistence/mybatis/SkuListTypeHandler.java` | Gắn với persistence catalog; kiểm tra MyBatis scan/type handler. |
| `security/JwtAuthenticationFilter.java` | `shared/security/JwtAuthenticationFilter.java` | Giữ nguyên attribute key để không ảnh hưởng controller. |
| `service/JwtService.java` | `shared/security/JwtService.java` hoặc `identity/application` | Chọn `identity` nếu JWT là use case đăng nhập; shared chỉ giữ kỹ thuật token. |
| `mapper/Modelmapper.java` | `shared/config` hoặc mapper riêng từng feature | Tránh một mapper toàn cục nếu mapping thuộc feature. |
| `service/mapper/CampaignEntityMapper.java` | `campaign/application/mapper/CampaignMapper.java` | Mapper nghiệp vụ campaign. |
| `service/mapper/ProductEntityMapper.java` | `catalog/application/mapper/ProductMapper.java` | Mapper nghiệp vụ product. |
| `service/validation/CampaignRequestValidator.java` | `campaign/application/validation/CampaignRequestValidator.java` | Request validation thuộc application/API boundary. |
| `service/validation/ProductRequestValidator.java` | `catalog/application/validation/ProductRequestValidator.java` | Request validation thuộc catalog. |

### API controllers

| File hiện tại | Package đích |
|---|---|
| `controller/AuthController.java` | `identity/api/AuthController.java` |
| `controller/UserAccountController.java` | `identity/api/UserAccountController.java` |
| `controller/buyer/BuyerProfileController.java` | `identity/api/BuyerProfileController.java` |
| `controller/ProductController.java` | `catalog/api/ProductController.java` |
| `controller/ProductReviewController.java` | `catalog/api/ProductReviewController.java` |
| `controller/CampainController.java` | `campaign/api/CampaignController.java` (giữ route, cân nhắc sửa chính tả class riêng biệt) |
| `controller/CartController.java` | `cart/api/CartController.java` |
| `controller/buyer/CheckoutController.java` | `checkout/api/CheckoutController.java` |
| `controller/buyer/OrderController.java` | `order/api/BuyerOrderController.java` |
| `controller/OrderExportController.java` | `order/api/OrderExportController.java` |
| `controller/OrderFulfillmentController.java` | `order/api/OrderFulfillmentController.java` |
| `controller/OrderReturnController.java` | `order/api/OrderReturnController.java` |
| `controller/PaymentController.java` | `payment/api/PaymentController.java` |
| `controller/WarehouseController.java` | `warehouse/api/WarehouseController.java` |
| `controller/buyer/ProductBuyerController.java` | `catalog/api/BuyerProductController.java` |
| `order/api/AdminOrderController.java` | Giữ trong `order/api` |
| `order/api/BuyerOrderController.java` | Giữ trong `order/api`; hiện file là class rỗng, không thay thế controller buyer đang hoạt động trước khi kiểm tra routes. |
| `order/api/SellerOrderController.java` | Giữ trong `order/api` |

Không đổi annotation mapping, HTTP method, request/response JSON hoặc security attributes trong bước chuyển package.

### Application services và jobs

| File hiện tại | Package đích |
|---|---|
| `service/AuthService.java` | `identity/application/port/in/AuthUseCase.java` (legacy adapter ở giai đoạn chuyển tiếp) |
| `service/UserAccountService.java` | `identity/application/port/in/UserAccountUseCase.java` |
| `service/Impl/ProductServiceImpl.java` | `catalog/application/service/ProductService.java` (implementation) |
| `service/ProductService.java` | `catalog/application/port/in/ProductUseCase.java` |
| `service/ProductReviewService.java` | `catalog/application/port/in/ProductReviewUseCase.java` |
| `service/Impl/CampainServiceImpl.java` | `campaign/application/service/CampaignService.java` |
| `service/CampainService.java` | `campaign/application/port/in/CampaignUseCase.java` |
| `service/CartService.java`, `service/Impl/CartServiceImpl.java` | `cart/application/port/in` và `cart/application/service` |
| `service/CheckoutService.java`, `service/Impl/CheckoutServiceImpl.java` | `checkout/application/port/in` và `checkout/application/service` |
| `service/OrderService.java`, `service/Impl/OrderServiceImpl.java` | `order/application/port/in` và `order/application/service` |
| `order/application/service/OrderService.java` | Chuẩn hóa thành application use-case contract; tránh hai interface cùng tên với `service/OrderService.java`. |
| `order/application/service/impl/OrderServiceImpl.java` | Giữ implementation tại `order/application/service`; hợp nhất/đổi tên chỉ sau khi đối chiếu callers và bean registrations. |
| `service/PaymentService.java`, `service/Impl/PaymentServiceImpl.java` | `payment/application/port/in` và `payment/application/service` |
| `service/ShippingService.java`, `service/Impl/ShippingServiceImpl.java` | `shipping/application/port/in` và `shipping/application/service` |
| `service/ShippingFeeService.java` | `shipping/application/port/in/ShippingFeeUseCase.java` |
| `service/MockShippingProvider.java` | `shipping/infrastructure/integration/MockShippingProvider.java` |
| `service/TrackingService.java`, `service/Impl/TrackingServiceImpl.java` | `shipping` hoặc `order` theo ownership của tracking state; hiện ưuut tiên `order` nếu tracking là order lifecycle. |
| `service/OrderFulfillmentService.java` | `order/application` |
| `service/OrderReturnService.java` | `order/application` |
| `service/OrderHistoryService.java` | `order/application` |
| `service/OrderExportService.java` | `order/application` |
| `service/OrderPricingService.java`, `service/PriceService.java`, `service/Impl/PriceServiceImpl.java` | Tách giá catalog/campaign và snapshot giá đặt hàng; không gom chung trước khi xác định nguồn giá từng use case. |
| `service/InventoryService.java`, `service/Impl/WarehouseServiceImpl.java`, `service/WarehouseService.java` | `warehouse/application` |
| `service/DeliveredOrderCompletionJob.java` | `order/infrastructure/integration` hoặc `order/application/job`; job gọi application use case, không chứa luật nghiệp vụ. |

### DTO, response và pagination

Các transport DTO cũ đang tập trung ở `model/dto`; chuyển theo feature, giữ nguyên field/property names để không đổi JSON:

- **identity:** `CreateUserRequest.java`, `LoginRequest.java`, `UpdateBuyerProfileRequest.java`.
- **catalog:** `CreateProductRequest.java`, `CreateReviewRequest.java`, `product/AttributeRequest.java`, `product/ImageRequest.java`, `product/MediaRequest.java`, `product/SkuRequest.java`, `product/TypeProductRequest.java`.
- **campaign:** `CampainProductRequest.java`, `CampainProductSku.java`, `CreatedCampainRequest.java`.
- **cart:** `AddCartRequest.java`, `CartItemDto.java`, `CartResponse.java` (đối chiếu response trùng tên trong `model/resp` trước khi chuyển), `DeleteRequest.java` nếu chỉ xóa cart item.
- **checkout:** `CheckoutPreviewRequest.java`, `CheckoutPreviewResponse.java`, `ShippingMethodDTO.java`, `ShippingRequest.java` (nếu chỉ dùng cho checkout, nếu không đặt ở shipping).
- **order:** `OrderRequest.java`, `OrderItemRequest.java`, `OrdersRequest.java` (dùng list/filter product hiện tại nên thay bằng `ProductQuery` sau migration), `SkuOrderRequest.java`, `OrderTrackingResponse.java`, `Trackingstep.java`, `CreateReturnRequest.java`, `ReturnReviewRequest.java`, `OrderExportRow.java`, `dto/order/Itemorderdetail.java`.
- **payment:** `PaymentRequest.java`, `PaymentCallback.java`, `Paymentcallback.java` (cùng blob hiện tại; kiểm tra usages rồi loại duplicate trong thay đổi riêng), `PaymentResponse`.
- **warehouse:** `WarehouseRequest.java`, `WarehouseSelectionRequest.java`, `WarehouseSellerinfo.java`, `WarehouseStatusRequest.java`, `WarehouseInventoryAdjustRequest.java`, `WarehouseInventoryMovementResponse.java`, `WarehouseInventoryResponse.java`.
- **shared/api:** `dto/page/PageInfo.java`, `dto/page/PagingResponse.java` chỉ khi nhiều feature thực sự dùng chung; nếu không thì từng feature định nghĩa result/page phù hợp.
- **shared/api:** `resp/APIRessponse.java`, `resp/BaseResponse.java`, `resp/BaseModel2.java` sau khi xác nhận contract JSON; giữ typo `APIRessponse` trong đợt đầu để tránh breaking change.

Response DTO cần được xem là API model, không phải domain model. `model/resp` được phân loại theo feature: `CampaignProductResponse`, `CampainResponse` -> campaign; `CartResponse` -> cart; `LoginResponse` -> identity; `OrderCreateResponse`, `OrderItemResponse`, `OrderResponse`, `OrderReturnResponse` -> order; `PaymentResponse` -> payment; `ProductDetailResponse`, `ProductResponse`, `ProductReviewResponse`, `SkuResponse` -> catalog; `WarehouseResponse` -> warehouse. Giữ nguyên response class/import compatibility cho đến khi callers được chuyển.

### Domain model, repositories và persistence

`model/entity` hiện gồm các lớp sau; phân nhóm đích theo ownership:

- `Product.java`, `ProductDetail.java`, `Product_Sku.java`, `ProductReview.java` -> `catalog/domain/model` (đổi `Product_Sku` thành `ProductSku` là cleanup sau, không làm chung với move).
- `Campain.java`, `CampainProduct.java`, `CampainproductSku.java` -> `campaign/domain/model`.
- `Cart.java`, `CartItem.java` -> `cart/domain/model`.
- `Order.java`, `OrderItem.java`, `OrderReturn.java`, `OrderShipping.java`, `OrderTracking.java`, `OrderExportHistory.java`, `JobConfig.java` -> `order/domain/model` (xác nhận `JobConfig` ownership bằng usages).
- `Payment.java` -> `payment/domain/model`.
- `Shipping.java`, `ShippingAssignment.java` -> `shipping/domain/model`.
- `User.java` -> `identity/domain/model`.
- `Warehouse.java` -> `warehouse/domain/model`.

Persistence interfaces/mapper hiện ở `model/repository` được chuyển cùng mapper XML theo feature:

- **catalog:** `ProductRepository.java`, `ProductSkuRepository.java`, `ProductReviewRepository.java` + `ProductMapper.xml`, `ProductSkuMapper.xml`, `ProductReviewMapper.xml`.
- **campaign:** `CampainRepository.java`, `CampainProductRepository.java` + `CampainMapper.xml`, `CampainProductMapper.xml`.
- **cart:** `CartRepository.java`, `CartItemRepository.java` + `CartMapper.xml`, `CartItemMapper.xml`.
- **order:** `OrderRepository.java`, `OrderItemRepository.java`, `OrderReturnRepository.java`, `OrderTrackingRepository.java`, `OrderExportRepository.java` + các XML cùng tên.
- **payment:** `PaymentRepository.java`, `PaymentRepostitory.java` + `PaymentMapper.xml`; giữ tên cũ cho tới khi kiểm tra usages và namespace mapper.
- **shipping:** `ShippingRepository.java` + `ShippingMaper.xml` (giữ tên file typo tới khi cấu hình mapper được cập nhật/test).
- **identity:** `UserRepository.java` + `UserMapper.xml`.
- **warehouse:** `WarehouseRepository.java` + `WarehouseMapper.xml`.

Trong Clean Architecture hoàn chỉnh, interface persistence là port do application/domain sở hữu; MyBatis mapper và adapter triển khai đặt trong `infrastructure/persistence/mybatis`. Bước đầu có thể đặt mapper hiện tại ở infrastructure và bổ sung port/adapter sau, tránh đổi toàn bộ SQL interface và namespace trong một commit.

### Enums

`model/enums` chuyển theo feature: `CampainStatus` -> campaign; `Order`, `OrderStatus`, `OrderShippingStatus`, `OrderTrackingStatus`, `TrackingStatus` -> order/shipping theo nơi sử dụng; `PaymentStatus` -> payment; `RoleAction` -> identity/security; `MessageCode` -> `shared/domain` chỉ khi dùng xuyên feature, nếu không chia thành lỗi theo module. Không đặt enum nghiệp vụ vào shared chỉ để tiện import.

## 3. Tài nguyên và migration SQL

- Giữ `src/main/resources/application.yaml` và Flyway `src/main/resources/db/migration/**` ở vị trí hiện tại; đây là convention/runtime discovery ổn định.
- Di chuyển `src/main/resources/sql/*.xml` theo feature chỉ sau khi cập nhật cấu hình `mapper-locations` trong `application.yaml`, `<mapper namespace>`, tên interface MyBatis và test context cùng một commit.
- Giữ nguyên thứ tự/version và nội dung migration. `src/main/resources/sql/migration/**` là legacy SQL, không tự động gộp vào `db/migration`; cần xác nhận đang được công cụ nào sử dụng.
- `dockerfile`, `pom.xml`, Maven wrapper, `.mvn/**` không nằm trong feature module; không đổi trong package refactor.

## 4. Trình tự migration không phá vỡ hệ thống

1. Tạo package đích và contract/application ports cho **một feature** (khuyến nghị catalog hoặc order), không move code chưa có kiểm thử.
2. Chuyển service interface thành use-case contract; để interface package cũ kế thừa contract như compatibility façade.
3. Chuyển controller sang feature API hoặc giữ controller cũ ủy quyền; giữ endpoint, method, payload và security attribute không đổi.
4. Di chuyển implementation và cập nhật wiring/imports; không tạo hai `@Service` bean cho cùng use case.
5. Di chuyển MyBatis mapper cùng XML và cấu hình scan/namespace đồng bộ; kiểm chứng app context khởi động.
6. Chạy `./mvnw test` và kiểm thử integration/HTTP với DB test; rà soát serialized JSON, mapper IDs, migration discovery.
7. Chỉ xóa façade/package cũ trong PR sau, khi tìm kiếm toàn repo chứng minh không còn caller.
8. Lặp lại feature tiếp theo. Không đổi tên class/field typo đồng thời với package move.

## 5. Rủi ro cần kiểm tra trước khi thực hiện từng move

- Mapper XML có thể được tìm bằng glob `classpath*:/sql/*.xml`; chuyển folder cần sửa config.
- `@MapperScan`, `@ComponentScan`, `@SpringBootApplication` và type handlers có thể phụ thuộc package cũ.
- Các module hiện tại `order`, `payment`, `cart` mới là một phần; order có hai đường service và nhiều controller legacy. Không tạo duplicate Spring beans.
- Entity/DTO được nhiều feature dùng; cần code search toàn repo và test compile trước khi đổi package.
- Hai response `CartResponse`, typo `PaymentRepostitory`, `Paymentcallback`, `Campain*`, `Product_Sku`, `ShippingMaper.xml` có thể được dùng trực tiếp bởi mapper/client; không đổi tên trong bước move.
- Route public và JSON là compatibility boundary; package refactor không được làm thay đổi chúng.

## 6. Kiểm chứng tối thiểu cho mỗi PR

```bash
./mvnw -q test
./mvnw -q -DskipTests package
```

Thêm kiểm tra context khởi động, mapper binding cho mỗi XML đã di chuyển, và test contract API cho routes/JSON liên quan. Việc build thành công một mình không chứng minh SQL namespace hay contract JSON còn tương thích.
