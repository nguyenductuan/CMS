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

    /**
     * Returns the authoritative shipping fee configured for a shipping service.
     * A zero fee is allowed for free-shipping methods.
     */
    public BigDecimal calculateFee(String serviceCode) {
        if (serviceCode == null || serviceCode.isBlank()) {
            throw new BusinessException(MessageCode.SHIPPING_NOT_FOUND);
        }

        Shipping shipping = shippingRepository.getShippingByCode(serviceCode.trim());
        if (shipping == null) {
            throw new BusinessException(MessageCode.SHIPPING_NOT_FOUND);
        }

        BigDecimal fee = shipping.getFee();
        if (fee == null || fee.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(MessageCode.INVALID_SHIPPING_FEE);
        }

        return fee;
    }
}
