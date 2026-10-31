package com.vt.cms.service;


import com.vt.cms.Exception.BusinessException;import com.vt.cms.model.enums.MessageCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vt.cms.model.dto.CreateReviewRequest;
import com.vt.cms.model.entity.ProductReview;
import com.vt.cms.model.repository.ProductReviewRepository;
import com.vt.cms.model.resp.ProductReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewService {
    private final ProductReviewRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional(rollbackFor = Exception.class)
    public ProductReviewResponse createReview(CreateReviewRequest request, Integer userId, String username) {
        if (request.getImages() != null && request.getImages().size() > 10)
            throw new BusinessException(MessageCode.TOI_DA_10_ANH_CHO_MOI_DANH_GIA);
        if (request.getVideos() != null && request.getVideos().size() > 3)
            throw new BusinessException(MessageCode.TOI_DA_3_VIDEO_CHO_MOI_DANH_GIA);
        if (repository.isEligibleOrderItem(request.getOrderId(), request.getProductId(),
                request.getSkuId(), username) != 1)
            throw new BusinessException(MessageCode.CHI_CHU_DON_HANG_DA_GIAO_DA_NHAN_MOI_DUOC_DANH_G);
        if (repository.countByOrderAndSku(request.getOrderId(), request.getSkuId()) > 0)
            throw new IllegalStateException("SKU này trong đơn hàng đã được đánh giá");

        ProductReview review = new ProductReview();
        review.setOrderId(request.getOrderId());
        review.setProductId(request.getProductId());
        review.setSkuId(request.getSkuId());
        review.setUserId(userId);
        review.setUsername(username);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        try {
            review.setImagesJson(objectMapper.writeValueAsString(request.getImages() == null ? List.of() : request.getImages()));
            review.setVideosJson(objectMapper.writeValueAsString(request.getVideos() == null ? List.of() : request.getVideos()));
        } catch (JsonProcessingException e) {
            throw new BusinessException(MessageCode.DANH_SACH_MEDIA_KHONG_HOP_LE, e);
        }
        review.setCreatedAt(LocalDateTime.now());
        if (repository.insertReview(review) != 1)
            throw new IllegalStateException("Không thể lưu đánh giá");
        return map(review);
    }

    public List<ProductReviewResponse> getByProduct(Integer productId) {
        return repository.findByProductId(productId).stream().map(this::map).toList();
    }

    public BigDecimal getAverage(Integer productId) {
        BigDecimal avg = repository.averageRating(productId);
        return avg == null ? BigDecimal.ZERO : avg.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private ProductReviewResponse map(ProductReview r) {
        ProductReviewResponse out = new ProductReviewResponse();
        out.setId(r.getId()); out.setOrderId(r.getOrderId()); out.setSkuId(r.getSkuId());
        out.setSkuCode(r.getSkuCode()); out.setUserId(r.getUserId()); out.setUsername(r.getUsername());
        out.setRating(r.getRating()); out.setContent(r.getContent()); out.setCreatedAt(r.getCreatedAt());
        try {
            out.setImages(r.getImagesJson() == null ? List.of() : objectMapper.readValue(r.getImagesJson(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)));
            out.setVideos(r.getVideosJson() == null ? List.of() : objectMapper.readValue(r.getVideosJson(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Dữ liệu media đánh giá không hợp lệ", e);
        }
        return out;
    }
}
