# Warehouse and inventory API

This change adds warehouse soft-delete/status management, per-warehouse SKU balances, and an audit trail for stock adjustments.

## Endpoints

- `GET /warehouse` — list active warehouses.
- `GET /warehouse/{id}` — warehouse details.
- `POST /warehouse` — create a warehouse.
- `DELETE /warehouse/{id}` — soft-delete a warehouse.
- `PUT /warehouse/status` — update active state using `{"id": 1, "active": true}`.
- `GET /warehouse/{warehouseId}/inventory` — list SKU balances.
- `POST /warehouse/{warehouseId}/inventory/adjust` — adjust a balance.
- `GET /warehouse/{warehouseId}/inventory/movements?skuId=10&limit=50` — list stock movement history.

Legacy `/addwarehouse`, `/deletewarehouse`, and `/statuswarehouse` routes are retained for compatibility.

### Example inventory adjustment

```json
{
  "skuId": 10,
  "quantityDelta": 5,
  "reason": "Nhập hàng đầu kỳ"
}
```

Positive `quantityDelta` adds stock; negative removes stock. An adjustment that would make a balance negative is rejected. Each successful adjustment writes a movement record in the same transaction.

## Database setup

Run `src/main/resources/db/migration/V20261023__warehouse_inventory.sql` once against the application's MySQL database before using these endpoints. The project currently does not auto-run these SQL files. If `address_detail` already exists in your warehouse table, omit that ALTER TABLE statement.

## Important integration note

These endpoints store balances in `warehouse_inventory`. Existing checkout validation still reads `product_sku.stock`; this change does not yet reserve/decrement global SKU stock during checkout or reconcile it with warehouse balances. Treat the warehouse balances as a separate ledger until checkout and order fulfillment are integrated.
