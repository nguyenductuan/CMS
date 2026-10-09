package com.vt.cms.service;

import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.repository.ShippingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShippingFeeService {

    private final ShippingRepository shippingRepository;

    public ShippingFeeService(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    public BigDecimal calculateFee(String shippingServiceCode) {
        if (shippingServiceCode == null || shippingServiceCode.isBlank()) {
            throw new BusinessException(MessageCode.SHIPPING_NOT_FOUND);
        }

        Shipping shipping = shippingRepository.detailShipping(shippingServiceCode);
        if (shipping == null || shipping.getFee() == null) {
            throw new BusinessException(MessageCode.SHIPPING_NOT_FOUND);
        }
        if (shipping.getFee().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(MessageCode.INVALID_SHIPPING_FEE);
        }

        return shipping.getFee();
    }
}
