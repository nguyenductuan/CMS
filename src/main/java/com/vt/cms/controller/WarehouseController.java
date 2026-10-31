package com.vt.cms.controller;


import com.vt.cms.Exception.BusinessException;import com.vt.cms.model.enums.MessageCode;
import com.vt.cms.model.dto.WarehouseInventoryAdjustRequest;
import com.vt.cms.model.dto.WarehouseInventoryMovementResponse;
import com.vt.cms.model.dto.WarehouseInventoryResponse;
import com.vt.cms.model.dto.WarehouseRequest;
import com.vt.cms.model.dto.WarehouseStatusRequest;
import com.vt.cms.model.entity.Warehouse;
import com.vt.cms.model.resp.APIRessponse;
import com.vt.cms.model.resp.WarehouseResponse;
import com.vt.cms.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping
public class WarehouseController {
    private final WarehouseService warehouseService;

    /** Khởi tạo controller và nhận service quản lý kho thông qua dependency injection. */
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * Lấy danh sách các kho đang hoạt động để giao diện quản trị hiển thị.
     * @return danh sách kho
     */
    /**
     * Truy vấn và trả về dữ liệu theo điều kiện đầu vào.
     * @return Kết quả xử lý của hàm.
     */
    @GetMapping({"/warehouse", "/warehouse/"})
    public List<Warehouse> getWarehouse() {
        return warehouseService.listqwarehouse();
    }

    /**
     * Lấy thông tin chi tiết của kho theo mã kho.
     * @param id mã kho cần tra cứu
     * @return thông tin chi tiết kho
     */
    /**
     * Truy vấn và trả về dữ liệu theo điều kiện đầu vào.
     * @param id Tham số phục vụ xử lý getWarehouse.
     * @return Kết quả xử lý của hàm.
     */
    @GetMapping("/warehouse/{id}")
    public WarehouseResponse getWarehouse(@PathVariable Integer id) {
        return warehouseService.getdetail(id);
    }

    /**
     * Tạo kho mới sau khi kiểm tra dữ liệu request hợp lệ.
     * @param warehouse thông tin kho cần tạo
     * @return thông báo tạo kho thành công
     */
    /**
     * Tạo mới và lưu dữ liệu nghiệp vụ.
     * @param warehouse Tham số phục vụ xử lý createWarehouse.
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping("/warehouse")
    public ResponseEntity<APIRessponse> createWarehouse(@Valid @RequestBody WarehouseRequest warehouse) {
        warehouseService.addwarehouse(warehouse);
        return ResponseEntity.ok(new APIRessponse(200, "Tạo kho thành công"));
    }

    /**
     * Endpoint tương thích ngược để thêm kho qua API cũ.
     * @param warehouse thông tin kho cần tạo
     * @return thông báo thêm kho thành công
     */
    /**
     * Tạo mới và lưu dữ liệu nghiệp vụ.
     * @param warehouse Tham số phục vụ xử lý addWarehouse.
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping({"/addwarehouse", "/warehouse/addwarehouse"})
    public ResponseEntity<APIRessponse> addWarehouse(@Valid @RequestBody WarehouseRequest warehouse) {
        warehouseService.addwarehouse(warehouse);
        return ResponseEntity.ok(new APIRessponse(200, "Thêm kho thành công"));
    }

    /**
     * Xóa mềm kho theo mã kho, không xóa vật lý bản ghi khỏi cơ sở dữ liệu.
     * @param id mã kho cần xóa
     * @return thông báo xóa kho thành công
     */
    /**
     * Xử lý xóa, loại bỏ hoặc hủy dữ liệu theo quy tắc nghiệp vụ.
     * @param id Tham số phục vụ xử lý deleteWarehouse.
     * @return Kết quả xử lý của hàm.
     */
    @DeleteMapping("/warehouse/{id}")
    public ResponseEntity<APIRessponse> deleteWarehouse(@PathVariable Integer id) {
        warehouseService.setWarehouseDeleted(id, true);
        return ResponseEntity.ok(new APIRessponse(200, "Xóa kho thành công"));
    }

    /**
     * Xử lý yêu cầu xóa kho theo định dạng API cũ.
     * @param warehouse đối tượng chứa mã kho
     * @return thông báo xóa kho thành công
     */
    /**
     * Xử lý xóa, loại bỏ hoặc hủy dữ liệu theo quy tắc nghiệp vụ.
     * @param warehouse Tham số phục vụ xử lý deleteWarehouseLegacy.
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping({"/deletewarehouse", "/warehouse/deletewarehouse"})
    public ResponseEntity<APIRessponse> deleteWarehouseLegacy(@RequestBody Warehouse warehouse) {
        if (warehouse == null || warehouse.getId() == null) {
            throw new BusinessException(MessageCode.MA_KHO_KHONG_DUOC_DE_TRONG);
        }
        warehouseService.setWarehouseDeleted(warehouse.getId(), true);
        return ResponseEntity.ok(new APIRessponse(200, "Xóa kho thành công"));
    }

    /**
     * Cập nhật trạng thái hoạt động của kho theo trường active trong request.
     * @param request mã kho và trạng thái hoạt động mong muốn
     * @return thông báo cập nhật trạng thái
     */
    /**
     * Cập nhật thông tin hoặc trạng thái theo nghiệp vụ.
     * @param request Tham số phục vụ xử lý updateWarehouseStatus.
     * @return Kết quả xử lý của hàm.
     */
    @PutMapping("/warehouse/status")
    public ResponseEntity<APIRessponse> updateWarehouseStatus(@Valid @RequestBody WarehouseStatusRequest request) {
        warehouseService.setWarehouseDeleted(request.getId(), !request.getActive());
        return ResponseEntity.ok(new APIRessponse(200, "Cập nhật trạng thái kho thành công"));
    }

    /**
     * Endpoint trạng thái tương thích ngược; chuyển kho sang trạng thái đã xóa.
     * @param id mã kho nhận từ request body
     * @return thông báo cập nhật trạng thái
     */
    /**
     * Thực hiện chức năng nghiệp vụ theo tên hàm và các tham số đầu vào.
     * @param id Tham số phục vụ xử lý statusWarehouse.
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping({"/statuswarehouse", "/warehouse/statuswarehouse"})
    public ResponseEntity<APIRessponse> statusWarehouse(@RequestBody Integer id) {
        warehouseService.setWarehouseDeleted(id, true);
        return ResponseEntity.ok(new APIRessponse(200, "Cập nhật trạng thái kho thành công"));
    }

    /**
     * Lấy tồn kho theo từng SKU trong một kho cụ thể.
     * @param warehouseId mã kho
     * @return danh sách SKU và số lượng tồn
     */
    /**
     * Truy vấn và trả về dữ liệu theo điều kiện đầu vào.
     * @param warehouseId Tham số phục vụ xử lý listInventory.
     * @return Kết quả xử lý của hàm.
     */
    @GetMapping("/warehouse/{warehouseId}/inventory")
    public List<WarehouseInventoryResponse> listInventory(@PathVariable Integer warehouseId) {
        return warehouseService.listInventory(warehouseId);
    }

    /**
     * Điều chỉnh tồn kho theo SKU. Số lượng dương là nhập, âm là xuất; lịch sử được ghi cùng transaction.
     * @param warehouseId mã kho cần điều chỉnh
     * @param request SKU, lượng thay đổi và lý do điều chỉnh
     * @return thông báo điều chỉnh thành công
     */
    /**
     * Cập nhật thông tin hoặc trạng thái theo nghiệp vụ.
     * @param warehouseId Tham số phục vụ xử lý adjustInventory.
     * @param request Tham số phục vụ xử lý adjustInventory.
     * @return Kết quả xử lý của hàm.
     */
    @PostMapping("/warehouse/{warehouseId}/inventory/adjust")
    public ResponseEntity<APIRessponse> adjustInventory(@PathVariable Integer warehouseId,
            @Valid @RequestBody WarehouseInventoryAdjustRequest request) {
        warehouseService.adjustInventory(warehouseId, request);
        return ResponseEntity.ok(new APIRessponse(200, "Điều chỉnh tồn kho thành công"));
    }

    /**
     * Lấy lịch sử nhập/xuất/điều chỉnh tồn kho, có thể lọc theo SKU và giới hạn số bản ghi.
     * @param warehouseId mã kho
     * @param skuId mã SKU cần lọc, có thể bỏ trống
     * @param limit số bản ghi tối đa cần trả về
     * @return danh sách lịch sử biến động tồn kho
     */
    /**
     * Truy vấn và trả về dữ liệu theo điều kiện đầu vào.
     * @param warehouseId Tham số phục vụ xử lý listInventoryMovements.
     * @param skuId Tham số phục vụ xử lý listInventoryMovements.
     * @param limit Tham số phục vụ xử lý listInventoryMovements.
     * @return Kết quả xử lý của hàm.
     */
    @GetMapping("/warehouse/{warehouseId}/inventory/movements")
    public List<WarehouseInventoryMovementResponse> listInventoryMovements(
            @PathVariable Integer warehouseId, @RequestParam(required = false) Integer skuId,
            @RequestParam(required = false) Integer limit) {
        return warehouseService.listInventoryMovements(warehouseId, skuId, limit);
    }
}
