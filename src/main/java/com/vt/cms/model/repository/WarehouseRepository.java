package com.vt.cms.model.repository;

import com.vt.cms.model.dto.WarehouseInventoryMovementResponse;
import com.vt.cms.model.dto.WarehouseInventoryResponse;
import com.vt.cms.model.dto.WarehouseRequest;
import com.vt.cms.model.entity.Warehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WarehouseRepository {
    /**
     * Lấy dữ liệu getlistwahouse. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<Warehouse> getlistwahouse();
    /**
     * Lấy dữ liệu getdetail. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    Warehouse getdetail(@Param("id") Integer id);
    /**
     * Thêm add. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int add(@Param("warehouse") WarehouseRequest warehouse, @Param("createdAt") LocalDateTime createdAt);
    /**
     * Thiết lập hoặc cập nhật set deleted. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int setDeleted(@Param("id") Integer id, @Param("deleted") boolean deleted);
    /**
     * Lấy danh sách list inventory. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<WarehouseInventoryResponse> listInventory(@Param("warehouseId") Integer warehouseId);
    /**
     * Lấy danh sách list inventory movements. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    List<WarehouseInventoryMovementResponse> listInventoryMovements(@Param("warehouseId") Integer warehouseId, @Param("skuId") Integer skuId, @Param("limit") Integer limit);
    /**
     * Thực hiện truy cập dữ liệu sku exists. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int skuExists(@Param("skuId") Integer skuId);
    /**
     * Thực hiện truy cập dữ liệu ensure inventory row. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int ensureInventoryRow(@Param("warehouseId") Integer warehouseId, @Param("skuId") Integer skuId);
    /**
     * Điều chỉnh adjust inventory. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int adjustInventory(@Param("warehouseId") Integer warehouseId, @Param("skuId") Integer skuId, @Param("delta") Integer delta);
    /**
     * Lấy dữ liệu get inventory quantity. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    Integer getInventoryQuantity(@Param("warehouseId") Integer warehouseId, @Param("skuId") Integer skuId);
    /**
     * Thêm bản ghi insert inventory movement. Các tham số và giá trị trả về được mô tả bởi chữ ký phương thức.
     */
    int insertInventoryMovement(@Param("warehouseId") Integer warehouseId, @Param("skuId") Integer skuId,
        @Param("delta") Integer delta, @Param("quantityAfter") Integer quantityAfter,
        @Param("reason") String reason, @Param("createdBy") String createdBy);
}
