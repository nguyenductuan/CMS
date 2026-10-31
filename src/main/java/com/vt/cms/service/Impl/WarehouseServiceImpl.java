package com.vt.cms.service.Impl;


import com.vt.cms.Exception.BusinessException;
import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.dto.WarehouseInventoryAdjustRequest;
import com.vt.cms.model.dto.WarehouseInventoryMovementResponse;
import com.vt.cms.model.dto.WarehouseInventoryResponse;
import com.vt.cms.model.dto.WarehouseRequest;
import com.vt.cms.model.entity.Warehouse;
import com.vt.cms.model.repository.WarehouseRepository;
import com.vt.cms.model.resp.WarehouseResponse;
import com.vt.cms.service.WarehouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;

    /**
     * Thực hiện xử lý nghiệp vụ warehouse service impl. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    /**
     * Lấy danh sách listqwarehouse. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public List<Warehouse> listqwarehouse() {
        return warehouseRepository.getlistwahouse();
    }

    @Override
    /**
     * Lấy dữ liệu getdetail. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public WarehouseResponse getdetail(Integer id) {
        if (id == null || id <= 0) throw new BusinessException(MessageCode.MA_KHO_KHONG_HOP_LE);
        Warehouse warehouse = warehouseRepository.getdetail(id);
        if (warehouse == null) throw new BusinessException(MessageCode.KHONG_TIM_THAY_KHO);
        WarehouseResponse response = new WarehouseResponse();
        response.setId(warehouse.getId());
        response.setAddress_default(warehouse.isAddress_is_default());
        try {
            response.setWarehouse_code(warehouse.getWarehouse_code() == null ? null
                    : Integer.valueOf(warehouse.getWarehouse_code()));
        } catch (NumberFormatException ignored) {
            response.setWarehouse_code(null);
        }
        response.setWarehouse_name(warehouse.getWarehouse_name());
        return response;
    }

    @Override
    /**
     * Thêm dữ liệu addwarehouse. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void addwarehouse(WarehouseRequest warehouse) {
        if (warehouse == null || warehouse.getWarehouse_name() == null || warehouse.getWarehouse_name().isBlank()) {
            throw new BusinessException(MessageCode.TEN_KHO_KHONG_DUOC_DE_TRONG);
        }
        if (warehouseRepository.add(warehouse, LocalDateTime.now()) != 1) {
            throw new IllegalStateException("Không thể tạo kho");
        }
    }

    @Override
    /**
     * Thiết lập hoặc cập nhật set warehouse deleted. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void setWarehouseDeleted(Integer id, boolean deleted) {
        if (id == null || id <= 0) throw new BusinessException(MessageCode.MA_KHO_KHONG_HOP_LE);
        if (warehouseRepository.setDeleted(id, deleted) != 1) throw new BusinessException(MessageCode.KHONG_TIM_THAY_KHO);
    }

    @Override
    /**
     * Lấy danh sách list inventory. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public List<WarehouseInventoryResponse> listInventory(Integer warehouseId) {
        requireActiveWarehouse(warehouseId);
        return warehouseRepository.listInventory(warehouseId);
    }

    @Override
    @Transactional
    /**
     * Điều chỉnh dữ liệu adjust inventory. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public void adjustInventory(Integer warehouseId, WarehouseInventoryAdjustRequest request) {
        requireActiveWarehouse(warehouseId);
        if (request == null || request.getSkuId() == null || request.getSkuId() <= 0
                || request.getQuantityDelta() == null || request.getQuantityDelta() == 0) {
            throw new BusinessException(MessageCode.SKU_VA_SO_LUONG_DIEU_CHINH_PHAI_HOP_LE_SO_LUONG_);
        }
        if (warehouseRepository.skuExists(request.getSkuId()) == 0) {
            throw new BusinessException(MessageCode.KHONG_TIM_THAY_SKU_HOAC_SKU_DA_BI_XOA);
        }
        warehouseRepository.ensureInventoryRow(warehouseId, request.getSkuId());
        if (warehouseRepository.adjustInventory(warehouseId, request.getSkuId(), request.getQuantityDelta()) != 1) {
            throw new IllegalStateException("Số lượng tồn kho không đủ để thực hiện điều chỉnh");
        }
        Integer quantityAfter = warehouseRepository.getInventoryQuantity(warehouseId, request.getSkuId());
        String reason = request.getReason() == null || request.getReason().isBlank()
                ? "Điều chỉnh tồn kho" : request.getReason().trim();
        if (reason.length() > 255) throw new BusinessException(MessageCode.LY_DO_DIEU_CHINH_KHONG_DUOC_VUOT_QUA_255_KY_TU);
        warehouseRepository.insertInventoryMovement(warehouseId, request.getSkuId(),
                request.getQuantityDelta(), quantityAfter, reason, "SYSTEM");
    }

    @Override
    /**
     * Lấy danh sách list inventory movements. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    public List<WarehouseInventoryMovementResponse> listInventoryMovements(Integer warehouseId, Integer skuId, Integer limit) {
        requireActiveWarehouse(warehouseId);
        int safeLimit = limit == null ? 50 : limit;
        if (safeLimit < 1 || safeLimit > 200) throw new BusinessException(MessageCode.LIMIT_PHAI_NAM_TRONG_KHOANG_1_DEN_200);
        if (skuId != null && skuId <= 0) throw new BusinessException(MessageCode.SKU_KHONG_HOP_LE);
        return warehouseRepository.listInventoryMovements(warehouseId, skuId, safeLimit);
    }

    /**
     * Kiểm tra điều kiện require active warehouse. Các tham số và kết quả tuân theo chữ ký phương thức.
     */
    private void requireActiveWarehouse(Integer warehouseId) {
        if (warehouseId == null || warehouseId <= 0 || warehouseRepository.getdetail(warehouseId) == null) {
            throw new BusinessException(MessageCode.KHONG_TIM_THAY_KHO);
        }
    }
}
