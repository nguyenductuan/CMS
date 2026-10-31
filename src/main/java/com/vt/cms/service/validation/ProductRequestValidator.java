package com.vt.cms.service.validation;


import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.enums.MessageCode;
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
        if (request == null) throw new BusinessException(MessageCode.THONG_TIN_SAN_PHAM_KHONG_DUOC_DE_TRONG);
        if (request.getName() == null || request.getName().isBlank()) throw new BusinessException(MessageCode.TEN_SAN_PHAM_KHONG_DUOC_DE_TRONG);
        if (request.getSkus() == null || request.getSkus().isEmpty()) throw new BusinessException(MessageCode.SAN_PHAM_PHAI_CO_IT_NHAT_MOT_SKU);

        Set<String> codes = new HashSet<>();
        for (SkuRequest sku : request.getSkus()) {
            if (sku == null) throw new BusinessException(MessageCode.THONG_TIN_SKU_KHONG_DUOC_DE_TRONG);
            if (sku.getSku_code() == null || sku.getSku_code().isBlank()) {
                sku.setSku_code("SKU-" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase());
            } else {
                sku.setSku_code(sku.getSku_code().trim());
            }
            if (!codes.add(sku.getSku_code())) throw new BusinessException(MessageCode.MA_SKU_BI_TRUNG_TRONG_REQUEST);
            if (sku.getPrice() == null || sku.getPrice().compareTo(BigDecimal.ZERO) <= 0) throw new BusinessException(MessageCode.GIA_SKU_PHAI_LON_HON_0);
            if (sku.getStock() == null || sku.getStock() < 0) throw new BusinessException(MessageCode.TON_KHO_SKU_PHAI_0);
            if (sku.getImage_url() == null || sku.getImage_url().isBlank()) throw new BusinessException(MessageCode.ANH_SKU_KHONG_DUOC_DE_TRONG);
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
        if (value == null || value.isBlank()) throw new BusinessException(MessageCode.THIEU);
        try {
            if (new BigDecimal(value).compareTo(BigDecimal.ZERO) < 0) throw new BusinessException(MessageCode.TON_KHO_SKU_PHAI_0);
        } catch (NumberFormatException ex) {
            /**
             * Thực hiện illegal argument exception. Tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new BusinessException(MessageCode.TON_KHO_SKU_PHAI_0, ex);
        }
    }
}
