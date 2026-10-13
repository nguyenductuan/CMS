-- Apply once to the existing MySQL schema before deploying this feature.
ALTER TABLE orders
    ADD COLUMN warehouse_code VARCHAR(100) NULL,
    ADD COLUMN shipping_provider VARCHAR(50) NULL,
    ADD COLUMN tracking_number VARCHAR(100) NULL,
    ADD COLUMN shipper_code VARCHAR(100) NULL,
    ADD COLUMN shipper_name VARCHAR(150) NULL,
    ADD COLUMN shipper_phone VARCHAR(30) NULL,
    ADD COLUMN shipped_at DATETIME NULL,
    ADD COLUMN carrier_delivered_at DATETIME NULL,
    ADD COLUMN customer_received_at DATETIME NULL;

CREATE UNIQUE INDEX uk_orders_tracking_number ON orders (tracking_number);
