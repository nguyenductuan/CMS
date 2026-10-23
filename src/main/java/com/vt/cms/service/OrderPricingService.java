package com.vt.cms.service;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.resp.SkuResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderPricingService {

    private final PriceService priceService;

    /**
     * Thực hiện xử lý nghiệp vụ order pricing service. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public OrderPricingService(PriceService priceService) {
        this.priceService = priceService;
    }

    /**
     * Lấy dữ liệu get unit price. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public BigDecimal getUnitPrice(SkuResponse sku) {
        if (sku == null) {
            /**
             * Thực hiện xử lý nghiệp vụ business exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new BusinessException(MessageCode.SKU_NOT_FOUND);
        }

        BigDecimal price = sku.getPriceDiscountCampaign();
        if (price == null) {
            price = sku.getPrice();
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            /**
             * Thực hiện xử lý nghiệp vụ business exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new BusinessException(MessageCode.INVALID_PRICE);
        }

        return price;
    }

    /**
     * Tính toán calculate line total. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public BigDecimal calculateLineTotal(BigDecimal unitPrice, int quantity) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            /**
             * Thực hiện xử lý nghiệp vụ business exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new BusinessException(MessageCode.INVALID_PRICE);
        }
        if (quantity <= 0) {
            /**
             * Thực hiện xử lý nghiệp vụ business exception. Các tham số và kết quả tuân theo chữ ký phương thức.
             */
            throw new BusinessException(MessageCode.INVALID_QUANTITY);
        }

        return priceService.calculateItemPrice(unitPrice, quantity);
    }
}
