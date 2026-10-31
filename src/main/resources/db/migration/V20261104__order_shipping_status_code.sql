-- Carrier-facing order status code, kept separate from the internal order_status state.
ALTER TABLE orders
    ADD COLUMN shipping_status_code VARCHAR(10) NULL;

UPDATE orders
SET shipping_status_code = CASE
    WHEN order_status IN ('WAIT_PAYMENT') THEN '104'
    WHEN order_status IN ('PAID', 'PREPARING') THEN '104'
    WHEN order_status = 'SHIPPING' THEN '500'
    WHEN order_status IN ('DELIVERED', 'RECEIVED', 'COMPLETED') THEN '200'
    ELSE shipping_status_code
END
WHERE shipping_status_code IS NULL;

CREATE INDEX idx_orders_shipping_status_code ON orders (shipping_status_code);
