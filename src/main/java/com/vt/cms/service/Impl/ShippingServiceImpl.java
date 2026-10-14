package com.vt.cms.service.Impl;

import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.repository.ShippingRepository;
import com.vt.cms.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingServiceImpl implements ShippingService{
private final ShippingRepository shippingRepository;
    @Override
    public List<Shipping> getShipping() {
       shippingRepository.getShipping();
        return List.of();
    }
}
