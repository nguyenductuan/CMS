package com.vt.cms.service.Impl;

import com.vt.cms.mapper.Modelmapper;
import com.vt.cms.model.entity.Shipping;
import com.vt.cms.model.repository.ShippingRepository;
import com.vt.cms.service.ShippingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingServiceImpl implements ShippingService {
    private final ShippingRepository shippingRepository;
    private final Modelmapper modelMapper = Mappers.getMapper(Modelmapper.class);
    @Transactional
    @Override
    public List<Shipping> getShipping(){
        return  modelMapper.convertShipping(shippingRepository.getShipping());

    }

}
