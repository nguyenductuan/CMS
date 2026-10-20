package com.vt.cms.service.validation;

import com.vt.cms.model.dto.CampainProductRequest;
import com.vt.cms.model.dto.CampainProductSku;
import com.vt.cms.model.dto.CreatedCampainRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class CampaignRequestValidator {
    public void validate(CreatedCampainRequest request) {
        if (request == null) throw new IllegalArgumentException("Request is required");
        if (request.getName() == null || request.getName().isBlank()) throw new IllegalArgumentException("Tên chiến dịch không được để trống");
        //if (request.getTotal_stock() == null || request.getTotal_stock() < 0) throw new IllegalArgumentException("Tổng tồn kho chiến dịch phải >= 0");
        if (request.getStartTime() == null || request.getEndTime() == null) throw new IllegalArgumentException("Start time and end time are required");
        if (request.getStartTime().isAfter(request.getEndTime())) throw new IllegalArgumentException("Start time must be before or equal to end time");
        validateDiscount(request.getDiscount());
        if (request.getProducts() == null || request.getProducts().isEmpty()) throw new IllegalArgumentException("Campaign must contain at least one product");

        Set<Integer> requestedSkuIds = new HashSet<>();
        for (CampainProductRequest product : request.getProducts()) {
            if (product == null || product.getProductid() <= 0) throw new IllegalArgumentException("Product ID must be valid");
            if (product.getSkus() == null || product.getSkus().isEmpty()) throw new IllegalArgumentException("Each campaign product must contain at least one SKU");
            for (CampainProductSku sku : product.getSkus()) {
                if (sku == null || sku.getSkuId() == null) throw new IllegalArgumentException("SKU ID is required");
                if (!requestedSkuIds.add(sku.getSkuId())) throw new IllegalArgumentException("Duplicate SKU ID: " + sku.getSkuId());
            }
        }
    }

    private void validateDiscount(BigDecimal discount) {
        if (discount == null) throw new IllegalArgumentException("Discount is required");
        if (discount.stripTrailingZeros().scale() > 0) throw new IllegalArgumentException("Discount must be an integer");
        if (discount.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(BigDecimal.valueOf(100)) > 0) throw new IllegalArgumentException("Discount must not exceed 100%");
    }
}
