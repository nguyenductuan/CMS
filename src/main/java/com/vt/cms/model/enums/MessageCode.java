package com.vt.cms.model.enums;

public enum MessageCode {
    SHIPPING_NOT_FOUND(
            "SHIPPING_NOT_FOUND",
            "Phương thức vận chuyển không tồn tại"
    ),
    INVALID_SHIPPING_FEE(
            "INVALID_SHIPPING_FEE",
            "Phí vận chuyển không hợp lệ"
    ),
    INVALID_ORDER(
            "INVALID_ORDER",
            "Thông tin đơn hàng không hợp lệ"
    ),
    INVALID_PRICE(
            "INVALID_PRICE",
            "Giá sản phẩm không hợp lệ"
    ),
    PRODUCT_NOT_FOUND(
            "PRODUCT_NOT_FOUND",
            "Sản phẩm không tồn tại"
    ),
    SKU_NOT_FOUND(
            "SKU_NOT_FOUND",
            "SKU không tồn tại"
    ),
    SKU_NOT_ACTIVE(
            "SKU_NOT_ACTIVE",
            "SKU không ở trạng thái ACTIVE"
    ),
    PRICE_NOT_FOUND_SKU(
            "PRICE_NOT_FOUND_SKU",
            "Giá sản phẩm không tồn tại"
    ),
    STOCK_NOT_ENOUGH(
            "STOCK_NOT_ENOUGH",
            "Số lượng sản phẩm trong kho không đủ"
    ),
    STOCK_INVALID(
            "STOCK_INVALID","Số lượng tồn kho không hợp lệ"
    ),
    STOCK_INSUFFICIENT(
            " STOCK_INSUFFICIENT", "Số lượng tồn kho không đủ"
    ),
    INVALID_QUANTITY(
            "INVALID_QUANTITY",
            "Số lượng sản phẩm không hợp lệ"
    ),
    CAMPAIN_STATUS_INVALID (
            "CAMPAIN_STATUS_INVALID",
            "Trạng thái campain không hợp lệ"
    ),
    PRODUCT_SKU_NOT_FOUND(
            "PRODUCT_SKU_NOT_FOUND",
            "SKU sản phẩm không tồn tại"
    ),
    MA_KHO_KHONG_DUOC_DE_TRONG("MA_KHO_KHONG_DUOC_DE_TRONG", "Mã kho không được để trống"),
    MA_VAN_DON_KHONG_DUOC_DE_TRONG("MA_VAN_DON_KHONG_DUOC_DE_TRONG", "Mã vận đơn không được để trống"),
    MA_TRANG_THAI_VAN_CHUYEN_KHONG_DUOC_HO_TRO("MA_TRANG_THAI_VAN_CHUYEN_KHONG_DUOC_HO_TRO", "Mã trạng thái vận chuyển không được hỗ trợ"),
    KHONG_XAC_DINH_DUOC_NGUOI_NHAN_HANG("KHONG_XAC_DINH_DUOC_NGUOI_NHAN_HANG", "Không xác định được người nhận hàng"),
    ID_DON_HANG_KHONG_HOP_LE("ID_DON_HANG_KHONG_HOP_LE", "ID đơn hàng không hợp lệ"),
    TOI_DA_10_ANH("TOI_DA_10_ANH", "Tối đa 10 ảnh"),
    TOI_DA_3_VIDEO("TOI_DA_3_VIDEO", "Tối đa 3 video"),
    KHONG_TIM_THAY_DON_HANG_CUA_NGUOI_DUNG("KHONG_TIM_THAY_DON_HANG_CUA_NGUOI_DUNG", "Không tìm thấy đơn hàng của người dùng"),
    KHONG_XAC_DINH_DUOC_TAI_KHOAN_NGUOI_MUA("KHONG_XAC_DINH_DUOC_TAI_KHOAN_NGUOI_MUA", "Không xác định được tài khoản người mua"),
    MEDIA_KHONG_HOP_LE("MEDIA_KHONG_HOP_LE", "Media không hợp lệ"),
    KHONG_TIM_THAY_YEU_CAU_TRA_HANG_CUA_BUYER("KHONG_TIM_THAY_YEU_CAU_TRA_HANG_CUA_BUYER", "Không tìm thấy yêu cầu trả hàng của Buyer"),
    KHONG_TIM_THAY_YEU_CAU_TRA_HANG("KHONG_TIM_THAY_YEU_CAU_TRA_HANG", "Không tìm thấy yêu cầu trả hàng"),
    PHUONG_THUC_PHAI_LA_RETURN_AND_REFUND_HOAC_REFUN("PHUONG_THUC_PHAI_LA_RETURN_AND_REFUND_HOAC_REFUN", "Phương thức phải là RETURN_AND_REFUND hoặc REFUND_IMMEDIATELY"),
    TEN_DANG_NHAP_HOAC_MAT_KHAU_KHONG_CHINH_XAC("TEN_DANG_NHAP_HOAC_MAT_KHAU_KHONG_CHINH_XAC", "Tên đăng nhập hoặc mật khẩu không chính xác"),
    TOI_DA_10_ANH_CHO_MOI_DANH_GIA("TOI_DA_10_ANH_CHO_MOI_DANH_GIA", "Tối đa 10 ảnh cho mỗi đánh giá"),
    TOI_DA_3_VIDEO_CHO_MOI_DANH_GIA("TOI_DA_3_VIDEO_CHO_MOI_DANH_GIA", "Tối đa 3 video cho mỗi đánh giá"),
    CHI_CHU_DON_HANG_DA_GIAO_DA_NHAN_MOI_DUOC_DANH_G("CHI_CHU_DON_HANG_DA_GIAO_DA_NHAN_MOI_DUOC_DANH_G", "Chỉ chủ đơn hàng đã giao/đã nhận mới được đánh giá đúng SKU của đơn"),
    DANH_SACH_MEDIA_KHONG_HOP_LE("DANH_SACH_MEDIA_KHONG_HOP_LE", "Danh sách media không hợp lệ"),
    TEN_DANG_NHAP_DA_TON_TAI("TEN_DANG_NHAP_DA_TON_TAI", "Tên đăng nhập đã tồn tại"),
    CAN_TRUYEN_CA_FROMDATE_VA_TODATE("CAN_TRUYEN_CA_FROMDATE_VA_TODATE", "Cần truyền cả fromDate và toDate"),
    TODATE_PHAI_LON_HON_HOAC_BANG_FROMDATE("TODATE_PHAI_LON_HON_HOAC_BANG_FROMDATE", "toDate phải lớn hơn hoặc bằng fromDate"),
    CAMPAIGN_ID_VA_TRANG_THAI_LA_BAT_BUOC("CAMPAIGN_ID_VA_TRANG_THAI_LA_BAT_BUOC", "Campaign ID và trạng thái là bắt buộc"),
    CAMPAIGN_ID_KHONG_HOP_LE("CAMPAIGN_ID_KHONG_HOP_LE", "Campaign ID không hợp lệ"),
    YEU_CAU_XOA_GIO_HANG_KHONG_DUOC_DE_TRONG("YEU_CAU_XOA_GIO_HANG_KHONG_DUOC_DE_TRONG", "Yêu cầu xóa giỏ hàng không được để trống"),
    DANH_SACH_PRODUCT_ID_KHONG_DUOC_DE_TRONG("DANH_SACH_PRODUCT_ID_KHONG_DUOC_DE_TRONG", "Danh sách product ID không được để trống"),
    TAT_CA_PRODUCT_ID_PHAI_LON_HON_0("TAT_CA_PRODUCT_ID_PHAI_LON_HON_0", "Tất cả product ID phải lớn hơn 0"),
    SAN_PHAM_KHONG_TON_TAI("SAN_PHAM_KHONG_TON_TAI", "Sản phẩm không tồn tại"),
    SKU_KHONG_TON_TAI_HOAC_KHONG_THUOC_SAN_PHAM_DA_C("SKU_KHONG_TON_TAI_HOAC_KHONG_THUOC_SAN_PHAM_DA_C", "SKU không tồn tại hoặc không thuộc sản phẩm đã chọn"),
    SO_LUONG_YEU_CAU_VUOT_QUA_TON_KHO_SKU("SO_LUONG_YEU_CAU_VUOT_QUA_TON_KHO_SKU", "Số lượng yêu cầu vượt quá tồn kho SKU"),
    SO_LUONG_SAN_PHAM_VUOT_GIOI_HAN_CHO_PHEP("SO_LUONG_SAN_PHAM_VUOT_GIOI_HAN_CHO_PHEP", "Số lượng sản phẩm vượt giới hạn cho phép"),
    TONG_SO_LUONG_SKU_TRONG_GIO_VUOT_QUA_TON_KHO("TONG_SO_LUONG_SKU_TRONG_GIO_VUOT_QUA_TON_KHO", "Tổng số lượng SKU trong giỏ vượt quá tồn kho"),
    YEU_CAU_KHONG_DUOC_DE_TRONG("YEU_CAU_KHONG_DUOC_DE_TRONG", "Yêu cầu không được để trống"),
    PRODUCT_ID_PHAI_LON_HON_0("PRODUCT_ID_PHAI_LON_HON_0", "Product ID phải lớn hơn 0"),
    SKU_ID_PHAI_LON_HON_0("SKU_ID_PHAI_LON_HON_0", "SKU ID phải lớn hơn 0"),
    SO_LUONG_SAN_PHAM_PHAI_LON_HON_0("SO_LUONG_SAN_PHAM_PHAI_LON_HON_0", "Số lượng sản phẩm phải lớn hơn 0"),
    USER_ID_PHAI_LON_HON_0("USER_ID_PHAI_LON_HON_0", "User ID phải lớn hơn 0"),
    YEU_CAU_THANH_TOAN_KHONG_HOP_LE("YEU_CAU_THANH_TOAN_KHONG_HOP_LE", "Yêu cầu thanh toán không hợp lệ"),
    KHONG_TIM_THAY_GIAO_DICH_THANH_TOAN("KHONG_TIM_THAY_GIAO_DICH_THANH_TOAN", "Không tìm thấy giao dịch thanh toán"),
    KHONG_TIM_THAY_DON_HANG("KHONG_TIM_THAY_DON_HANG", "Không tìm thấy đơn hàng"),
    SO_TIEN_THANH_TOAN_KHONG_HOP_LE("SO_TIEN_THANH_TOAN_KHONG_HOP_LE", "Số tiền thanh toán không hợp lệ"),
    SO_TIEN_CALLBACK_KHONG_KHOP_TONG_TIEN_DON_HANG("SO_TIEN_CALLBACK_KHONG_KHOP_TONG_TIEN_DON_HANG", "Số tiền callback không khớp tổng tiền đơn hàng"),
    THONG_TIN_TAO_THANH_TOAN_KHONG_HOP_LE("THONG_TIN_TAO_THANH_TOAN_KHONG_HOP_LE", "Thông tin tạo thanh toán không hợp lệ"),
    SO_TIEN_THANH_TOAN_KHONG_KHOP_TONG_TIEN_DON_HANG("SO_TIEN_THANH_TOAN_KHONG_KHOP_TONG_TIEN_DON_HANG", "Số tiền thanh toán không khớp tổng tiền đơn hàng"),
    ID_SAN_PHAM_KHONG_HOP_LE("ID_SAN_PHAM_KHONG_HOP_LE", "ID sản phẩm không hợp lệ"),
    THONG_TIN_PHAN_TRANG_KHONG_DUOC_DE_TRONG("THONG_TIN_PHAN_TRANG_KHONG_DUOC_DE_TRONG", "Thông tin phân trang không được để trống"),
    PAGENO_PHAI_0("PAGENO_PHAI_0", "pageNo phải >= 0"),
    PAGESIZE_PHAI_NAM_TRONG_KHOANG_1_100("PAGESIZE_PHAI_NAM_TRONG_KHOANG_1_100", "pageSize phải nằm trong khoảng 1..100"),
    MA_KHO_KHONG_HOP_LE("MA_KHO_KHONG_HOP_LE", "Mã kho không hợp lệ"),
    KHONG_TIM_THAY_KHO("KHONG_TIM_THAY_KHO", "Không tìm thấy kho"),
    TEN_KHO_KHONG_DUOC_DE_TRONG("TEN_KHO_KHONG_DUOC_DE_TRONG", "Tên kho không được để trống"),
    SKU_VA_SO_LUONG_DIEU_CHINH_PHAI_HOP_LE_SO_LUONG_("SKU_VA_SO_LUONG_DIEU_CHINH_PHAI_HOP_LE_SO_LUONG_", "SKU và số lượng điều chỉnh phải hợp lệ, số lượng không được bằng 0"),
    KHONG_TIM_THAY_SKU_HOAC_SKU_DA_BI_XOA("KHONG_TIM_THAY_SKU_HOAC_SKU_DA_BI_XOA", "Không tìm thấy SKU hoặc SKU đã bị xóa"),
    LY_DO_DIEU_CHINH_KHONG_DUOC_VUOT_QUA_255_KY_TU("LY_DO_DIEU_CHINH_KHONG_DUOC_VUOT_QUA_255_KY_TU", "Lý do điều chỉnh không được vượt quá 255 ký tự"),
    LIMIT_PHAI_NAM_TRONG_KHOANG_1_DEN_200("LIMIT_PHAI_NAM_TRONG_KHOANG_1_DEN_200", "limit phải nằm trong khoảng 1 đến 200"),
    SKU_KHONG_HOP_LE("SKU_KHONG_HOP_LE", "SKU không hợp lệ"),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "Không tìm thấy đơn hàng:"),
    INVALID_EXPORT_TAB("INVALID_EXPORT_TAB", "Tên tab không được hỗ trợ:"),
    CAMPAIGN_NOT_FOUND("CAMPAIGN_NOT_FOUND", "Không tìm thấy chiến dịch:");

    private final String code;
    private final String message;

    MessageCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Phương thức get code cung cấp thông tin hoặc thao tác liên quan đến trạng thái nghiệp vụ.
     */
    public String getCode() {
        return code;
    }

    /**
     * Phương thức get message cung cấp thông tin hoặc thao tác liên quan đến trạng thái nghiệp vụ.
     */
    public String getMessage() {
        return message;
    }
}
