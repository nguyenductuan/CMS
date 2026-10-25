package com.vt.cms.model.repository;

import com.vt.cms.model.entity.Campain;
import com.vt.cms.model.resp.CampainResponse;
import java.util.List;
import com.vt.cms.model.enums.CampainStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CampainRepository {
    /**
     * Lưu savecampain. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    void savecampain(Campain campain);
  /**
   * Cập nhật updatestatus. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
   */
  int  updatestatus (@Param("campain_id") Integer campainId, @Param("status") CampainStatus status);
   /**
    * Lấy dữ liệu get campain by id. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
    */
   Campain getCampainById(@Param("campain_id") Integer campain_id);
  /**
   * Lấy dữ liệu getlistcampain. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
   */
  List<CampainResponse> getlistcampain();
    /**
     * Xóa deletecampain. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int deletecampain(@Param("campain_id") Integer campainID );

}
