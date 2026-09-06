package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Shipper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShiperRepository {
    int saveShiper(Shipper shipper);

    Shipper getlistShipper();
}