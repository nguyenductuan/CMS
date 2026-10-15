package com.vt.cms.service;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.dto.SkuOrderRequest;
import com.vt.cms.model.entity.ProductDetail;
import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.resp.SkuResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InventoryService {

    /**
     * Validates requested SKU quantities against the product snapshot.
     * Quantities for duplicate SKU rows are aggregated before stock validation.
     *
     * This method does not reserve/decrement stock. The persistence layer must
     * perform an atomic conditional update to prevent concurrent overselling.
     */
    public Map<Integer, SkuResponse> validateRequestedItems(
            ProductDetail product,
            List<SkuOrderRequest> requestedItems) {

        if (product == null || product.getSkus() == null) {
            throw new BusinessException(MessageCode.PRODUCT_SKU_NOT_FOUND);
        }
        if (requestedItems == null || requestedItems.isEmpty()) {
            throw new BusinessException(MessageCode.INVALID_ORDER);
        }

        Map<Integer, SkuResponse> skuById = new HashMap<>();
        for (SkuResponse sku : product.getSkus()) {
            if (sku != null && sku.getId() != null) {
                skuById.put(sku.getId(), sku);
            }
        }

        Map<Integer, Long> requestedQuantityBySku = new HashMap<>();
        for (SkuOrderRequest item : requestedItems) {
            if (item == null || item.getSkuId() == null) {
                throw new BusinessException(MessageCode.SKU_NOT_FOUND);
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new BusinessException(MessageCode.INVALID_QUANTITY);
            }

            if (!skuById.containsKey(item.getSkuId())) {
                throw new BusinessException(MessageCode.SKU_NOT_FOUND);
            }

            requestedQuantityBySku.merge(
                    item.getSkuId(),
                    item.getQuantity().longValue(),
                    Long::sum
            );
        }

        for (Map.Entry<Integer, Long> entry : requestedQuantityBySku.entrySet()) {
            SkuResponse sku = skuById.get(entry.getKey());
            Integer availableStock = sku.getStock();

            if (availableStock == null || entry.getValue() > availableStock.longValue()) {
                throw new BusinessException(MessageCode.STOCK_NOT_ENOUGH);
            }
        }

        return skuById;
    }
}
