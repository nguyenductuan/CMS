package com.vt.cms.service;

import java.math.BigDecimal;

public interface PriceService {

    /**
     * Tính toán calculate item price. Tham số và kết quả tuân theo chữ ký phương thức.
     */
    BigDecimal calculateItemPrice(BigDecimal price, int quantity);

}
