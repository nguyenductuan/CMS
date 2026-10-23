package com.vt.cms.service.Impl;

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

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public List<Warehouse> listqwarehouse() {
        return warehouseRepository.getlistwahouse();
    }

    @Override
    public WarehouseResponse getdetail(Integer id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Mã kho không hợp lệ");
        Warehouse warehouse = warehouseRepository.getdetail(id);
        if (warehouse == null) throw new IllegalArgumentException("Không tìm thấy kho");
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
    public void addwarehouse(WarehouseRequest warehouse) {
        if (warehouse == null || warehouse.getWarehouse_name() == null || warehouse.getWarehouse_name().isBlank()) {
            throw new IllegalArgumentException("Tên kho không được để trống");
        }
        if (warehouseRepository.add(warehouse, LocalDateTime.now()) != 1) {
            throw new IllegalStateException("Không thể tạo kho");
        }
    }

    @Override
    public void setWarehouseDeleted(Integer id, boolean deleted) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Mã kho không hợp lệ");
        if (warehouseRepository.setDeleted(id, deleted) != 1) throw new IllegalArgumentException("Không tìm thấy kho");
    }

    @Override
    public List<WarehouseInventoryResponse> listInventory(Integer warehouseId) {
        requireActiveWarehouse(warehouseId);
        return warehouseRepository.listInventory(warehouseId);
    }

    @Override
    @Transactional
    public void adjustInventory(Integer warehouseId, WarehouseInventoryAdjustRequest request) {
        requireActiveWarehouse(warehouseId);
        if (request == null || request.getSkuId() == null || request.getSkuId() <= 0
                || request.getQuantityDelta() == null || request.getQuantityDelta() == 0) {
            throw new IllegalArgumentException("SKU và số lượng điều chỉnh phải hợp lệ, số lượng không được bằng 0");
        }
        if (warehouseRepository.skuExists(request.getSkuId()) == 0) {
            throw new IllegalArgumentException("Không tìm thấy SKU hoặc SKU đã bị xóa");
        }
        warehouseRepository.ensureInventoryRow(warehouseId, request.getSkuId());
        if (warehouseRepository.adjustInventory(warehouseId, request.getSkuId(), request.getQuantityDelta()) != 1) {
            throw new IllegalStateException("Số lượng tồn kho không đủ để thực hiện điều chỉnh");
        }
        Integer quantityAfter = warehouseRepository.getInventoryQuantity(warehouseId, request.getSkuId());
        String reason = request.getReason() == null || request.getReason().isBlank()
                ? "Điều chỉnh tồn kho" : request.getReason().trim();
        if (reason.length() > 255) throw new IllegalArgumentException("Lý do điều chỉnh không được vượt quá 255 ký tự");
        warehouseRepository.insertInventoryMovement(warehouseId, request.getSkuId(),
                request.getQuantityDelta(), quantityAfter, reason, "SYSTEM");
    }

    @Override
    public List<WarehouseInventoryMovementResponse> listInventoryMovements(Integer warehouseId, Integer skuId, Integer limit) {
        requireActiveWarehouse(warehouseId);
        int safeLimit = limit == null ? 50 : limit;
        if (safeLimit < 1 || safeLimit > 200) throw new IllegalArgumentException("limit phải nằm trong khoảng 1 đến 200");
        if (skuId != null && skuId <= 0) throw new IllegalArgumentException("SKU không hợp lệ");
        return warehouseRepository.listInventoryMovements(warehouseId, skuId, safeLimit);
    }

    private void requireActiveWarehouse(Integer warehouseId) {
        if (warehouseId == null || warehouseId <= 0 || warehouseRepository.getdetail(warehouseId) == null) {
            throw new IllegalArgumentException("Không tìm thấy kho");
        }
    }
}
