package com.vt.cms.model.repository;

import com.vt.cms.model.entity.CampainProduct;
import org.mapstruct.Mapper;

@Mapper
public interface CampainProductRepository {
    CampainProduct savecampainproduct(CampainProduct campainProduct);
}
