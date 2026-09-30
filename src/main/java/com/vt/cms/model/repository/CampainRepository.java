package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Campain;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CampainRepository {
    void savecampain1(Integer campain_id, String status);
   Campain getCampainById(Integer campainId);
  int getlistcampain();
    Campain deletecampain(Integer campainID );

}
