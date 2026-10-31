package com.vt.cms.service;


import com.vt.cms.Exception.BusinessException;import com.vt.cms.model.enums.MessageCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vt.cms.model.dto.CreateReturnRequest;
import com.vt.cms.model.dto.ReturnReviewRequest;
import com.vt.cms.model.entity.Order;
import com.vt.cms.model.entity.OrderReturn;
import com.vt.cms.model.entity.User;
import com.vt.cms.model.enums.OrderStatus;
import com.vt.cms.model.repository.OrderRepository;
import com.vt.cms.model.repository.OrderReturnRepository;
import com.vt.cms.model.repository.UserRepository;
import com.vt.cms.model.resp.OrderReturnResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderReturnService {
    private final OrderRepository orderRepository;
    private final OrderReturnRepository returnRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OrderHistoryService orderHistoryService;

    /**
     * Kiểm tra điều kiện và tạo mới dữ liệu nghiệp vụ.
     * @param orderId Tham số đầu vào của hàm.
     * @param request Tham số đầu vào của hàm.
     * @param buyerId Tham số đầu vào của hàm.
     * @param username Tham số đầu vào của hàm.
     * @return Kết quả xử lý.
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderReturnResponse requestReturn(Integer orderId, CreateReturnRequest request,
                                             Integer buyerId, String username) {
        if (request.getImages() != null && request.getImages().size() > 10)
            throw new BusinessException(MessageCode.TOI_DA_10_ANH);
        if (request.getVideos() != null && request.getVideos().size() > 3)
            throw new BusinessException(MessageCode.TOI_DA_3_VIDEO);
        Order order = orderRepository.getorderbyIdForUpdate(orderId);
        if (order == null || !username.equals(order.getCreated_by()))
            throw new BusinessException(MessageCode.KHONG_TIM_THAY_DON_HANG_CUA_NGUOI_DUNG);
        if (!List.of(OrderStatus.DELIVERED, OrderStatus.RECEIVED, OrderStatus.COMPLETED).contains(order.getOrder_status()))
            throw new IllegalStateException("Chỉ đơn đã giao/đã nhận mới được yêu cầu trả hàng");

        User buyer = userRepository.findByUsername(username);
        if (buyer == null || !buyerId.equals(buyer.getId()))
            throw new BusinessException(MessageCode.KHONG_XAC_DINH_DUOC_TAI_KHOAN_NGUOI_MUA);
        OrderReturn ret = new OrderReturn();
        ret.setOrderId(orderId); ret.setBuyerId(buyerId); ret.setBuyerUsername(username);
        ret.setBuyerName(buyer.getName()); ret.setPhone(buyer.getPhone()); ret.setEmail(buyer.getEmail());
        ret.setAddress(buyer.getAddress()); ret.setBankName(buyer.getBankName());
        ret.setBankAccountNumber(buyer.getBankAccountNumber()); ret.setBankAccountHolder(buyer.getBankAccountHolder());
        ret.setReason(request.getReason().trim());
        try {
            ret.setImagesJson(objectMapper.writeValueAsString(request.getImages() == null ? List.of() : request.getImages()));
            ret.setVideosJson(objectMapper.writeValueAsString(request.getVideos() == null ? List.of() : request.getVideos()));
        } catch (JsonProcessingException e) { throw new BusinessException(MessageCode.MEDIA_KHONG_HOP_LE, e); }
        int previousRequests = returnRepository.countByOrderId(orderId);
        ret.setStatus(previousRequests > 0 ? "PENDING_ADMIN_REVIEW" : "PENDING_SELLER_REVIEW");
        ret.setCreatedAt(LocalDateTime.now());
        if (returnRepository.insert(ret) != 1) throw new IllegalStateException("Không tạo được yêu cầu trả hàng");
        if (orderRepository.setReturnRequested(orderId, username, LocalDateTime.now(), username, ret.getStatus()) != 1)
            throw new IllegalStateException("Đơn hàng đã thay đổi trạng thái");
        orderHistoryService.record(orderId, "BUYER_RETURN_REQUESTED", ret.getStatus(), username, "BUYER", "Người mua tạo yêu cầu Trả hàng/Hoàn tiền", "Lý do: " + ret.getReason());
        return map(returnRepository.findById(ret.getId()));
    }

    /**
     * Buyer hủy yêu cầu trả hàng khi yêu cầu vẫn đang chờ Seller hoặc Admin xét duyệt.
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelReturnRequest(Integer returnId, String buyerUsername) {
        OrderReturn current = returnRepository.findById(returnId);
        if (current == null || !buyerUsername.equals(current.getBuyerUsername()))
            throw new BusinessException(MessageCode.KHONG_TIM_THAY_YEU_CAU_TRA_HANG_CUA_BUYER);
        int rows = returnRepository.cancelBuyerRequest(returnId, buyerUsername);
        if (rows != 1) throw new IllegalStateException("Chỉ được hủy yêu cầu khi đang chờ xét duyệt");
        orderRepository.setReturnRejected(current.getOrderId(), LocalDateTime.now(), buyerUsername);
        orderHistoryService.record(current.getOrderId(), "BUYER_RETURN_REQUEST_CANCELLED", "DELIVERED",
                buyerUsername, "BUYER", "Người mua hủy yêu cầu Trả hàng/Hoàn tiền", "Yêu cầu #" + returnId);
    }

    /**
     * Truy vấn thông tin nghiệp vụ theo tham số đầu vào.
     * @param returnId Tham số đầu vào của hàm.
     * @param username Tham số đầu vào của hàm.
     * @param role Tham số đầu vào của hàm.
     * @return Kết quả xử lý.
     */
    public OrderReturnResponse getReturn(Integer returnId, String username, String role) {
        OrderReturn ret = returnRepository.findById(returnId);
        if (ret == null) throw new BusinessException(MessageCode.KHONG_TIM_THAY_YEU_CAU_TRA_HANG);
        if ("BUYER".equals(role) && !username.equals(ret.getBuyerUsername())) throw new SecurityException("Không có quyền xem yêu cầu trả hàng này");
        if (!List.of("BUYER", "SELLER", "ADMIN").contains(role)) throw new SecurityException("Không có quyền xem yêu cầu trả hàng");
        return map(ret);
    }

    /**
     * Truy vấn thông tin nghiệp vụ theo tham số đầu vào.
     * @param role Tham số đầu vào của hàm.
     * @return Kết quả xử lý.
     */
    public List<OrderReturnResponse> getPending(String role) {
        String status;
        if ("SELLER".equals(role)) return returnRepository.findSellerPendingActions().stream().map(this::map).toList();
        if ("ADMIN".equals(role)) return returnRepository.findPending("PENDING_ADMIN_REVIEW").stream().map(this::map).toList();
        throw new SecurityException("Chỉ SELLER hoặc ADMIN được xem danh sách chờ duyệt");
    }

    /**
     * Xét duyệt yêu cầu và cập nhật trạng thái theo vai trò được phân quyền.
     * @param returnId Tham số đầu vào của hàm.
     * @param request Tham số đầu vào của hàm.
     * @param reviewer Tham số đầu vào của hàm.
     * @param role Tham số đầu vào của hàm.
     * @return Kết quả xử lý.
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderReturnResponse sellerReview(Integer returnId, ReturnReviewRequest request, String reviewer, String role) {
        requireRole(role, "SELLER");
        OrderReturn current = requirePending(returnId, "PENDING_SELLER_REVIEW");
        int rows = returnRepository.sellerReview(returnId, request.getApproved(), reviewer, request.getNote(), LocalDateTime.now());
        if (rows != 1) throw new IllegalStateException("Yêu cầu đã được xử lý");
        if (!request.getApproved()) {
            orderRepository.setReturnRejected(current.getOrderId(), LocalDateTime.now(), reviewer);
            orderHistoryService.record(current.getOrderId(), "SELLER_RETURN_REJECTED", "DELIVERED", reviewer, "SELLER", "Seller từ chối yêu cầu Trả hàng/Hoàn tiền", request.getNote());
        } else {
            orderRepository.updateReturnSubStatus(current.getOrderId(), "PENDING_SELLER_METHOD_SELECTION", reviewer, LocalDateTime.now());
            orderHistoryService.record(current.getOrderId(), "SELLER_RETURN_APPROVED", "PENDING_SELLER_METHOD_SELECTION", reviewer, "SELLER", "Seller phê duyệt yêu cầu Trả hàng/Hoàn tiền", request.getNote());
        }
        return map(returnRepository.findById(returnId));
    }

    /**
     * Xét duyệt yêu cầu và cập nhật trạng thái theo vai trò được phân quyền.
     * @param returnId Tham số đầu vào của hàm.
     * @param request Tham số đầu vào của hàm.
     * @param reviewer Tham số đầu vào của hàm.
     * @param role Tham số đầu vào của hàm.
     * @return Kết quả xử lý.
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderReturnResponse adminReview(Integer returnId, ReturnReviewRequest request, String reviewer, String role) {
        requireRole(role, "ADMIN");
        OrderReturn current = requirePending(returnId, "PENDING_ADMIN_REVIEW");
        int rows = returnRepository.adminReview(returnId, request.getApproved(), reviewer, request.getNote(), LocalDateTime.now());
        if (rows != 1) throw new IllegalStateException("Yêu cầu đã được xử lý");
        if (!request.getApproved()) {
            orderRepository.setReturnRejected(current.getOrderId(), LocalDateTime.now(), reviewer);
            orderHistoryService.record(current.getOrderId(), "ADMIN_RETURN_REJECTED", "DELIVERED", reviewer, "ADMIN", "Admin từ chối yêu cầu Trả hàng/Hoàn tiền", request.getNote());
        } else {
            orderRepository.updateReturnSubStatus(current.getOrderId(), "PENDING_SELLER_METHOD_SELECTION", reviewer, LocalDateTime.now());
            orderHistoryService.record(current.getOrderId(), "ADMIN_RETURN_APPROVED", "PENDING_SELLER_METHOD_SELECTION", reviewer, "ADMIN", "Admin phê duyệt yêu cầu; chuyển Seller chọn phương thức", request.getNote());
        }
        return map(returnRepository.findById(returnId));
    }

    /**
     * Cho phép Seller chọn phương thức xử lý sau khi yêu cầu trả hàng được duyệt.
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderReturnResponse selectReturnMethod(Integer returnId, String method, String reviewer, String role) {
        requireRole(role, "SELLER");
        if (!List.of("RETURN_AND_REFUND", "REFUND_IMMEDIATELY").contains(method))
            throw new BusinessException(MessageCode.PHUONG_THUC_PHAI_LA_RETURN_AND_REFUND_HOAC_REFUN);
        OrderReturn current = requirePending(returnId, "PENDING_SELLER_METHOD_SELECTION");
        LocalDateTime now = LocalDateTime.now();
        if (returnRepository.selectReturnMethod(returnId, method, now) != 1)
            throw new IllegalStateException("Yêu cầu đã được chọn phương thức hoặc hết hạn");
        String subStatus = "RETURN_AND_REFUND".equals(method)
                ? "WAITING_FOR_RETURN_SHIPMENT" : "REFUND_PENDING";
        orderRepository.updateReturnSubStatus(current.getOrderId(), subStatus, reviewer, now);
        orderHistoryService.record(current.getOrderId(), "SELLER_RETURN_METHOD_SELECTED", subStatus, reviewer, "SELLER", "Seller chọn phương thức " + method, "Phương thức xử lý yêu cầu trả hàng: " + method);
        return map(returnRepository.findById(returnId));
    }

    /**
     * Thực hiện xử lý nghiệp vụ tự động theo điều kiện cấu hình.
     */
    @Scheduled(fixedDelay = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void escalateSellerTimeouts() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.minusHours(24);
        List<OrderReturn> expiredSellerReviews = returnRepository.findExpiredSellerReviews(cutoff);
        returnRepository.escalateExpiredSellerReviews(cutoff);
        orderRepository.escalateReturnReviews(cutoff, now, "SYSTEM");
        for (OrderReturn item : expiredSellerReviews) {
            orderHistoryService.record(item.getOrderId(), "SYSTEM_ESCALATED_TO_ADMIN", "PENDING_ADMIN_REVIEW",
                    "SYSTEM", "SYSTEM", "Hệ thống chuyển yêu cầu trả hàng lên Admin do Seller không phê duyệt trong 24h",
                    "Yêu cầu trả hàng #" + item.getId());
        }

        List<OrderReturn> expiredMethodSelections = returnRepository.findExpiredMethodSelections(now);
        returnRepository.autoSelectExpiredReturnMethods(now);
        orderRepository.syncReturnMethodStatus(now, "SYSTEM");
        for (OrderReturn item : expiredMethodSelections) {
            orderHistoryService.record(item.getOrderId(), "SYSTEM_AUTO_RETURN_METHOD", "WAITING_FOR_RETURN_SHIPMENT",
                    "SYSTEM", "SYSTEM", "Hệ thống tự động chọn phương thức Trả hàng & Hoàn tiền do quá 24h không thao tác",
                    "Yêu cầu trả hàng #" + item.getId() + "; phương thức RETURN_AND_REFUND");
        }
    }

    /**
     * Thực hiện chức năng nghiệp vụ theo tên hàm.
     * @param id Tham số đầu vào của hàm.
     * @param status Tham số đầu vào của hàm.
     * @return Kết quả xử lý.
     */
    private OrderReturn requirePending(Integer id, String status) {
        OrderReturn ret = returnRepository.findById(id);
        if (ret == null || !status.equals(ret.getStatus())) throw new IllegalStateException("Yêu cầu không tồn tại hoặc không ở trạng thái chờ duyệt");
        return ret;
    }
    /**
     * Thực hiện chức năng nghiệp vụ theo tên hàm.
     * @param actual Tham số đầu vào của hàm.
     * @param expected Tham số đầu vào của hàm.
     */
    private void requireRole(String actual, String expected) {
        if (!expected.equals(actual)) throw new SecurityException("Chỉ " + expected + " được thực hiện bước duyệt này");
    }
    /**
     * Thực hiện chức năng nghiệp vụ theo tên hàm.
     * @param r Tham số đầu vào của hàm.
     * @return Kết quả xử lý.
     */
    private OrderReturnResponse map(OrderReturn r) {
        OrderReturnResponse out = new OrderReturnResponse();
        out.setId(r.getId()); out.setOrderId(r.getOrderId()); out.setBuyerUsername(r.getBuyerUsername());
        var orderSummary = orderRepository.getorderbyid(r.getOrderId());
        if (orderSummary != null) {
            out.setOrderStatus(orderSummary.getOrderStatus());
            out.setOrderTotal(orderSummary.getTotal());
            out.setOrderItems(orderRepository.getOrderItemsByOrderId(r.getOrderId()));
        }
        out.setReturnMethod(r.getReturnMethod()); out.setMethodSelectionDeadline(r.getMethodSelectionDeadline()); out.setMethodSelectedAt(r.getMethodSelectedAt());
        out.setBuyerName(r.getBuyerName()); out.setPhone(r.getPhone()); out.setEmail(r.getEmail());
        out.setAddress(r.getAddress()); out.setBankName(r.getBankName()); out.setBankAccountNumber(r.getBankAccountNumber());
        out.setBankAccountHolder(r.getBankAccountHolder()); out.setReason(r.getReason()); out.setStatus(r.getStatus());
        out.setSellerReviewer(r.getSellerReviewer()); out.setAdminReviewer(r.getAdminReviewer());
        out.setSellerNote(r.getSellerNote()); out.setAdminNote(r.getAdminNote()); out.setCreatedAt(r.getCreatedAt());
        out.setSellerReviewedAt(r.getSellerReviewedAt()); out.setAdminReviewedAt(r.getAdminReviewedAt());
        try {
            out.setImages(r.getImagesJson() == null ? List.of() : objectMapper.readValue(r.getImagesJson(), objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)));
            out.setVideos(r.getVideosJson() == null ? List.of() : objectMapper.readValue(r.getVideosJson(), objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)));
        } catch (JsonProcessingException e) { throw new IllegalStateException("Media trả hàng không hợp lệ", e); }
        return out;
    }
}
