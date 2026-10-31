-- Carrier-facing order status code, kept separate from the internal order_status state.
ALTER TABLE orders
    ADD COLUMN shipping_status_code VARCHAR(32) NULL;

-- Historical orders remain NULL until an actual carrier event is received.
CREATE INDEX idx_orders_shipping_status_code ON orders (shipping_status_code);
