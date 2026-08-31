package com.vt.cms.service.Impl;

import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.PriceService;
import io.swagger.v3.oas.annotations.servers.Server;

import java.math.BigDecimal;
@Server
public class PriceServiceImpl implements PriceService {
    @Override
    public BigDecimal calculateItemPrice(ProductResponse product, int quantity) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
