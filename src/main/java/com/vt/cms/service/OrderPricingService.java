package com.vt.cms.service;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.resp.SkuResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderPricingService {

    private final PriceService priceService;

    public OrderPricingService(PriceService priceService) {
        this.priceService = priceService;
    }

    public BigDecimal getUnitPrice(SkuResponse sku) {
        if (sku == null) {
            throw new BusinessException(MessageCode.SKU_NOT_FOUND);
        }

        BigDecimal price = sku.getPriceDiscountCampaign();
        if (price == null) {
            price = sku.getPrice();
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(MessageCode.INVALID_PRICE);
        }

        return price;
    }

    public BigDecimal calculateLineTotal(BigDecimal unitPrice, int quantity) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(MessageCode.INVALID_PRICE);
        }
        if (quantity <= 0) {
            throw new BusinessException(MessageCode.INVALID_QUANTITY);
        }

        return priceService.calculateItemPrice(unitPrice, quantity);
    }
}
