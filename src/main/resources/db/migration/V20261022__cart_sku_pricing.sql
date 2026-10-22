-- Run this migration before deploying the SKU-based cart implementation.
-- Nullable initially to preserve existing cart rows; new cart rows always store sku_id.
ALTER TABLE cart_item
    ADD COLUMN sku_id INT NULL;

CREATE INDEX idx_cart_item_cart_sku
    ON cart_item (cart_id, sku_id);
