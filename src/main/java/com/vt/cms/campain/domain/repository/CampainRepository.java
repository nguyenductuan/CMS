package com.vt.cms.campain.domain.repository;

import com.vt.cms.base.request.Pagerequest;
import com.vt.cms.campain.application.dto.response.CampainResponse;
import com.vt.cms.campain.domain.enity.Campain;
import com.vt.cms.campain.domain.enums.CampainStatus;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CampainRepository {
  long countCampains(Pagerequest request);

  List<CampainResponse> getlistcampain(Pagerequest request);

  void savecampain(Campain campain);

  void finishCampain(Campain campain);

  int updatestatus(
      @Param("campain_id") Integer campainId,
      @Param("status") CampainStatus status,
      @Param("userId") Integer userId);

  Campain getCampainById(@Param("campaign_id") Integer campaignId);

  int deletecampain(@Param("campaign_id") Integer campaignId, @Param("userId") Integer userId);

  List<Campain> findAllCampainByIds(Collection<Integer> ids);

  int decreaseTotalStock(
      @Param("campaignId") Integer campaignId, @Param("quantity") Integer quantity);
}
