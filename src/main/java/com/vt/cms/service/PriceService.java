package com.vt.cms.service;

import java.math.BigDecimal;

public interface PriceService {

    BigDecimal calculateItemPrice(BigDecimal price, int quantity);

}
