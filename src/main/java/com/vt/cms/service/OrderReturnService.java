package com.vt.cms.service;

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

    @Transactional(rollbackFor = Exception.class)
    public OrderReturnResponse requestReturn(Integer orderId, CreateReturnRequest request,
                                             Integer buyerId, String username) {
        if (request.getImages() != null && request.getImages().size() > 10)
            throw new IllegalArgumentException("Tối đa 10 ảnh");
        if (request.getVideos() != null && request.getVideos().size() > 3)
            throw new IllegalArgumentException("Tối đa 3 video");
        Order order = orderRepository.getorderbyIdForUpdate(orderId);
        if (order == null || !username.equals(order.getCreated_by()))
            throw new IllegalArgumentException("Không tìm thấy đơn hàng của người dùng");
        if (!List.of(OrderStatus.DELIVERED, OrderStatus.RECEIVED, OrderStatus.COMPLETED).contains(order.getOrder_status()))
            throw new IllegalStateException("Chỉ đơn đã giao/đã nhận mới được yêu cầu trả hàng");

        User buyer = userRepository.findByUsername(username);
        if (buyer == null || !buyerId.equals(buyer.getId()))
            throw new IllegalArgumentException("Không xác định được tài khoản người mua");
        OrderReturn ret = new OrderReturn();
        ret.setOrderId(orderId); ret.setBuyerId(buyerId); ret.setBuyerUsername(username);
        ret.setBuyerName(buyer.getName()); ret.setPhone(buyer.getPhone()); ret.setEmail(buyer.getEmail());
        ret.setAddress(buyer.getAddress()); ret.setBankName(buyer.getBankName());
        ret.setBankAccountNumber(buyer.getBankAccountNumber()); ret.setBankAccountHolder(buyer.getBankAccountHolder());
        ret.setReason(request.getReason().trim());
        try {
            ret.setImagesJson(objectMapper.writeValueAsString(request.getImages() == null ? List.of() : request.getImages()));
            ret.setVideosJson(objectMapper.writeValueAsString(request.getVideos() == null ? List.of() : request.getVideos()));
        } catch (JsonProcessingException e) { throw new IllegalArgumentException("Media không hợp lệ", e); }
        ret.setCreatedAt(LocalDateTime.now());
        if (returnRepository.insert(ret) != 1) throw new IllegalStateException("Không tạo được yêu cầu trả hàng");
        if (orderRepository.setReturnRequested(orderId, username, LocalDateTime.now(), username) != 1)
            throw new IllegalStateException("Đơn hàng đã thay đổi trạng thái");
        return map(returnRepository.findById(ret.getId()));
    }

    public OrderReturnResponse getReturn(Integer returnId, String username, String role) {
        OrderReturn ret = returnRepository.findById(returnId);
        if (ret == null) throw new IllegalArgumentException("Không tìm thấy yêu cầu trả hàng");
        if ("BUYER".equals(role) && !username.equals(ret.getBuyerUsername())) throw new SecurityException("Không có quyền xem yêu cầu trả hàng này");
        if (!List.of("BUYER", "SELLER", "ADMIN").contains(role)) throw new SecurityException("Không có quyền xem yêu cầu trả hàng");
        return map(ret);
    }

    public List<OrderReturnResponse> getPending(String role) {
        String status;
        if ("SELLER".equals(role)) status = "PENDING_SELLER_REVIEW";
        else if ("ADMIN".equals(role)) status = "PENDING_ADMIN_REVIEW";
        else throw new SecurityException("Chỉ SELLER hoặc ADMIN được xem danh sách chờ duyệt");
        return returnRepository.findPending(status).stream().map(this::map).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderReturnResponse sellerReview(Integer returnId, ReturnReviewRequest request, String reviewer, String role) {
        requireRole(role, "SELLER");
        OrderReturn current = requirePending(returnId, "PENDING_SELLER_REVIEW");
        int rows = returnRepository.sellerReview(returnId, request.getApproved(), reviewer, request.getNote(), LocalDateTime.now());
        if (rows != 1) throw new IllegalStateException("Yêu cầu đã được xử lý");
        if (!request.getApproved()) orderRepository.setReturnRejected(current.getOrderId(), LocalDateTime.now(), reviewer);
        else orderRepository.updateReturnSubStatus(current.getOrderId(), "PENDING_ADMIN_REVIEW", reviewer, LocalDateTime.now());
        return map(returnRepository.findById(returnId));
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderReturnResponse adminReview(Integer returnId, ReturnReviewRequest request, String reviewer, String role) {
        requireRole(role, "ADMIN");
        OrderReturn current = requirePending(returnId, "PENDING_ADMIN_REVIEW");
        int rows = returnRepository.adminReview(returnId, request.getApproved(), reviewer, request.getNote(), LocalDateTime.now());
        if (rows != 1) throw new IllegalStateException("Yêu cầu đã được xử lý");
        if (!request.getApproved()) orderRepository.setReturnRejected(current.getOrderId(), LocalDateTime.now(), reviewer);
        else orderRepository.updateReturnSubStatus(current.getOrderId(), "APPROVED", reviewer, LocalDateTime.now());
        return map(returnRepository.findById(returnId));
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void escalateSellerTimeouts() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.minusHours(24);
        returnRepository.escalateExpiredSellerReviews(cutoff);
        orderRepository.escalateReturnReviews(cutoff, now, "SYSTEM");
    }

    private OrderReturn requirePending(Integer id, String status) {
        OrderReturn ret = returnRepository.findById(id);
        if (ret == null || !status.equals(ret.getStatus())) throw new IllegalStateException("Yêu cầu không tồn tại hoặc không ở trạng thái chờ duyệt");
        return ret;
    }
    private void requireRole(String actual, String expected) {
        if (!expected.equals(actual)) throw new SecurityException("Chỉ " + expected + " được thực hiện bước duyệt này");
    }
    private OrderReturnResponse map(OrderReturn r) {
        OrderReturnResponse out = new OrderReturnResponse();
        out.setId(r.getId()); out.setOrderId(r.getOrderId()); out.setBuyerUsername(r.getBuyerUsername());
        var orderSummary = orderRepository.getorderbyid(r.getOrderId());
        if (orderSummary != null) {
            out.setOrderStatus(orderSummary.getOrderStatus());
            out.setOrderTotal(orderSummary.getTotal());
            out.setOrderItems(orderRepository.getOrderItemsByOrderId(r.getOrderId()));
        }
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
