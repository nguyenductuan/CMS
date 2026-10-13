package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Campain;
import com.vt.cms.model.enums.CampainStatus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CampainRepository {
    void savecampain(Campain campain);
  int  updatestatus (Integer campainId, CampainStatus status);
   Campain getCampainById(Integer campain_id);
  int getlistcampain();
    Campain deletecampain(Integer campainID );

}
