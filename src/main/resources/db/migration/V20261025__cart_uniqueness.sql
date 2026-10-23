-- Unique constraints are the final guard against concurrent find-then-insert races.
-- Preflight and reconcile existing duplicate rows before applying this migration:
-- SELECT user_id, COUNT(*) FROM cart GROUP BY user_id HAVING COUNT(*) > 1;
-- SELECT cart_id, sku_id, COUNT(*) FROM cart_item GROUP BY cart_id, sku_id HAVING COUNT(*) > 1;
CREATE UNIQUE INDEX uk_cart_user_id ON cart (user_id);
CREATE UNIQUE INDEX uk_cart_item_cart_sku ON cart_item (cart_id, sku_id);
