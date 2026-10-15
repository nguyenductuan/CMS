package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Campain;
import com.vt.cms.model.resp.CampainResponse;
import java.util.List;
import com.vt.cms.model.enums.CampainStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CampainRepository {
    void savecampain(Campain campain);
  int  updatestatus (@Param("campain_id") Integer campainId, @Param("status") CampainStatus status);
   Campain getCampainById(@Param("campain_id") Integer campain_id);
  List<CampainResponse> getlistcampain();
    int deletecampain(@Param("campain_id") Integer campainID );

}
