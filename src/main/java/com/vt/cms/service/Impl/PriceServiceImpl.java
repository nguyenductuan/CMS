package com.vt.cms.service.Impl;

import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.PriceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service

public class PriceServiceImpl implements PriceService {
    @Override
    public BigDecimal calculateItemPrice(BigDecimal price, int quantity) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
