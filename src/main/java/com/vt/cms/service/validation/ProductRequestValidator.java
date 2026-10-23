package com.vt.cms.service.validation;

import com.vt.cms.model.dto.CreateProductRequest;
import com.vt.cms.model.dto.product.SkuRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class ProductRequestValidator {
    /**
     * Kiểm tra tính hợp lệ validate. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void validate(CreateProductRequest request) {
        if (request == null) throw new IllegalArgumentException("Thông tin sản phẩm không được để trống");
        if (request.getName() == null || request.getName().isBlank()) throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        if (request.getSkus() == null || request.getSkus().isEmpty()) throw new IllegalArgumentException("Sản phẩm phải có ít nhất một SKU");

        Set<String> codes = new HashSet<>();
        for (SkuRequest sku : request.getSkus()) {
            if (sku == null) throw new IllegalArgumentException("Thông tin SKU không được để trống");
            if (sku.getSku_code() == null || sku.getSku_code().isBlank()) {
                sku.setSku_code("SKU-" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase());
            } else {
                sku.setSku_code(sku.getSku_code().trim());
            }
            if (!codes.add(sku.getSku_code())) throw new IllegalArgumentException("Mã SKU bị trùng trong request: " + sku.getSku_code());
            if (sku.getPrice() == null || sku.getPrice().compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Giá SKU phải lớn hơn 0: " + sku.getSku_code());
            if (sku.getStock() == null || sku.getStock() < 0) throw new IllegalArgumentException("Tồn kho SKU phải >= 0: " + sku.getSku_code());
            if (sku.getImage_url() == null || sku.getImage_url().isBlank()) throw new IllegalArgumentException("Ảnh SKU không được để trống: " + sku.getSku_code());
            validateDimension(sku.getWeight_gram(), "khối lượng", sku.getSku_code());
            validateDimension(sku.getLength_cm(), "chiều dài", sku.getSku_code());
            validateDimension(sku.getWidth_cm(), "chiều rộng", sku.getSku_code());
            validateDimension(sku.getHeight_cm(), "chiều cao", sku.getSku_code());
        }
    }

    /**
     * Kiểm tra tính hợp lệ validate dimension. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    private void validateDimension(String value, String label, String skuCode) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Thiếu " + label + " SKU: " + skuCode);
        try {
            if (new BigDecimal(value).compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException(label + " SKU phải >= 0: " + skuCode);
        } catch (NumberFormatException ex) {
            /**
             * Thực hiện illegal argument exception. Tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new IllegalArgumentException(label + " SKU không hợp lệ: " + skuCode, ex);
        }
    }
}
