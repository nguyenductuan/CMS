CREATE TABLE order_return_request (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    buyer_id INT NOT NULL,
    buyer_username VARCHAR(50) NOT NULL,
    buyer_name VARCHAR(150) NULL,
    phone VARCHAR(30) NULL,
    email VARCHAR(150) NULL,
    address VARCHAR(500) NULL,
    bank_name VARCHAR(120) NULL,
    bank_account_number VARCHAR(50) NULL,
    bank_account_holder VARCHAR(150) NULL,
    reason VARCHAR(2000) NOT NULL,
    images_json JSON NULL,
    videos_json JSON NULL,
    status VARCHAR(40) NOT NULL,
    seller_reviewer VARCHAR(50) NULL,
    admin_reviewer VARCHAR(50) NULL,
    seller_note VARCHAR(1000) NULL,
    admin_note VARCHAR(1000) NULL,
    created_at DATETIME NOT NULL,
    seller_reviewed_at DATETIME NULL,
    admin_reviewed_at DATETIME NULL,
    INDEX idx_return_status_created (status, created_at),
    INDEX idx_return_order (order_id)
);

ALTER TABLE user
    ADD COLUMN phone VARCHAR(30) NULL,
    ADD COLUMN email VARCHAR(150) NULL,
    ADD COLUMN address VARCHAR(500) NULL,
    ADD COLUMN bank_name VARCHAR(120) NULL,
    ADD COLUMN bank_account_number VARCHAR(50) NULL,
    ADD COLUMN bank_account_holder VARCHAR(150) NULL;
