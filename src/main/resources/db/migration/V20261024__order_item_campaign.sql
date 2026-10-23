-- Preserve the exact campaign SKU used when an order was priced.
-- Apply this migration to the MySQL schema before deploying the matching application build.
ALTER TABLE order_item
    ADD COLUMN campaign_id INT NULL;

CREATE INDEX idx_order_item_campaign_sku
    ON order_item (campaign_id, sku_id);
