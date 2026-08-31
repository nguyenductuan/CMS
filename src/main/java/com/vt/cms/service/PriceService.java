package com.vt.cms.service;

import com.vt.cms.model.dto.ProductRequest;
import com.vt.cms.model.resp.ProductResponse;

import java.math.BigDecimal;

public interface PriceService {

    BigDecimal calculateItemPrice(ProductResponse product, int quantity);

}
