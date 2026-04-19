package com.vt.cms.service.Impl;

import com.vt.cms.mapper.Modelmapper;
import com.vt.cms.model.repository.ProductRepository;
import com.vt.cms.model.resp.ProductResponse;
import com.vt.cms.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final Modelmapper modelMapper = Mappers.getMapper(Modelmapper.class);
    private final ProductRepository productRepository;


    @Override
    public ProductResponse detail(Integer id) {
        return modelMapper.convertProduct(
                (productRepository.detailProduct(id)
                )
        );
    }
}
