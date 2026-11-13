package com.vt.cms.product.application.service.mapper;


import com.vt.cms.Exception.BusinessException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vt.cms.base.enums.MessageCode;
import com.vt.cms.product.application.dto.req.CreateProductRequest;
import com.vt.cms.product.domain.entity.Product;
import com.vt.cms.product.domain.enums.ProductStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ProductEntityMapper {
    private final ObjectMapper objectMapper;

    /**
     * Thực hiện product entity mapper
     */
    public ProductEntityMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Thực hiện thêm dữ liệu mới product
     */
    public Product toNewProduct(CreateProductRequest request, Integer userId, String role) {
        LocalDateTime approvedTime = ProductStatus.PENDING_APPROVAL.equals(request.getStatus())
                ? LocalDateTime.now() : null;

        Product product = new Product();
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus());
        product.setIsDeleted("false");
        product.setCreatedAt(LocalDateTime.now());
        product.setCreatedBy(userId.toString());
        product.setTypeProducts(toJson(request.getType_products()));
        product.setJsonAttributes(toJson(request.getAttributes()));
        product.setJsonMedias(toJson(request.getMedias()));
        product.setApprovedTime(approvedTime);
        product.setJsonImages(toJson(request.getImages()));
        product.setJsonAttributesName(request.getAttributes_name());
        product.setTypeProductsName(request.getType_products_name());
        return product;
    }

    /**
     * Thực hiện to json. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value == null ? List.of() : value);
        } catch (JsonProcessingException ex) {
            log.error("Cannot serialize product request data", ex);
            /**
             * Thực hiện illegal argument exception. Tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new BusinessException(MessageCode.DU_LIEU_SAN_PHAM_KHONG_THE_CHUYEN_DOI_SANG_JSON, ex);
        }
    }
}
