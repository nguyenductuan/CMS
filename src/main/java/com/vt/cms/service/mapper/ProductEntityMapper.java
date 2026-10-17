package com.vt.cms.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vt.cms.model.dto.CreateProductRequest;
import com.vt.cms.model.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ProductEntityMapper {
    private final ObjectMapper objectMapper;

    public ProductEntityMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Product toNewProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setStatus("PENDING_APPROVAL");
        product.setIsDeleted("false");
        product.setCreatedAt(LocalDateTime.now());
        product.setCreatedBy("SYSTEM");
        product.setTypeProducts(toJson(request.getType_products()));
        product.setJsonAttributes(toJson(request.getAttributes()));
        product.setJsonMedias(toJson(request.getMedias()));
        product.setJsonImages(toJson(request.getImages()));
        product.setJson_attributes_name(request.getAttributes_name());
        product.setType_products_name(request.getType_products_name());
        return product;
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value == null ? List.of() : value);
        } catch (JsonProcessingException ex) {
            log.error("Cannot serialize product request data", ex);
            throw new IllegalArgumentException("Dữ liệu sản phẩm không thể chuyển đổi sang JSON", ex);
        }
    }
}
