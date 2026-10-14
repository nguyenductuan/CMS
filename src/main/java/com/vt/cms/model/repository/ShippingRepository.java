package com.vt.cms.model.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShippingRepository {
   void getShipping();
}
