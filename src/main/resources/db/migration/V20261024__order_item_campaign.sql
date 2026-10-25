-- Preserve the exact campaign SKU used when an order was priced.
-- Apply this migration to the MySQL schema before deploying the matching application build.
ALTER TABLE order_item
    ADD COLUMN campaign_id INT NULL;

-- Backfill historical rows only where the SKU has exactly one campaign association.
-- Ambiguous historical rows remain NULL and must be reconciled before processing pending payments.
UPDATE order_item oi
JOIN (
    SELECT product_id, sku_id, MIN(campaign_id) AS campaign_id
    FROM campain_product
    GROUP BY product_id, sku_id
    HAVING COUNT(DISTINCT campaign_id) = 1
) cp ON cp.product_id = oi.product_id AND cp.sku_id = oi.sku_id
SET oi.campaign_id = cp.campaign_id
WHERE oi.campaign_id IS NULL;

CREATE INDEX idx_order_item_campaign_sku
    ON order_item (campaign_id, sku_id);

-- Transaction codes must be unique for reliable callback lookup and idempotency.
CREATE UNIQUE INDEX uk_payment_transaction_code ON payment (transactionCode);
