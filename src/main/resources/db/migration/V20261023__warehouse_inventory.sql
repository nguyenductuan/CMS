-- Run once against the application MySQL database before deploying the warehouse inventory endpoints.
-- Existing project setup does not automatically execute these SQL migration files.

ALTER TABLE warehouse
    ADD COLUMN address_detail VARCHAR(255) NULL;

CREATE TABLE IF NOT EXISTS warehouse_inventory (
    id BIGINT NOT NULL AUTO_INCREMENT,
    warehouse_id INT NOT NULL,
    sku_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_warehouse_inventory_warehouse_sku (warehouse_id, sku_id),
    KEY idx_warehouse_inventory_sku (sku_id),
    CONSTRAINT chk_warehouse_inventory_quantity CHECK (quantity >= 0)
);

CREATE TABLE IF NOT EXISTS warehouse_inventory_movement (
    id BIGINT NOT NULL AUTO_INCREMENT,
    warehouse_id INT NOT NULL,
    sku_id INT NOT NULL,
    quantity_delta INT NOT NULL,
    quantity_after INT NOT NULL,
    reason VARCHAR(255) NULL,
    created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_warehouse_movement_warehouse_created (warehouse_id, created_at),
    KEY idx_warehouse_movement_sku_created (sku_id, created_at)
);
