-- Store delivery recipient details on each order.
ALTER TABLE orders
    ADD COLUMN recipient_name VARCHAR(150) NULL,
    ADD COLUMN recipient_phone VARCHAR(30) NULL,
    ADD COLUMN delivery_address VARCHAR(500) NULL;
