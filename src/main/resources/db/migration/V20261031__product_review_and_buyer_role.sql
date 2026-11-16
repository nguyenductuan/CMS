-- The existing role column is VARCHAR; BUYER is assigned through the account-creation API.

-- One review per SKU in a given order. Media are stored as JSON URL arrays.
CREATE TABLE product_review (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    sku_id INT NOT NULL,
    user_id INT NOT NULL,
    username VARCHAR(50) NOT NULL,
    rating TINYINT NOT NULL,
    content VARCHAR(2000) NULL,
    images_json JSON NULL,
    videos_json JSON NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT chk_product_review_rating CHECK (rating BETWEEN 1 AND 5),
    CONSTRAINT uk_product_review_order_sku UNIQUE (order_id, sku_id),
    INDEX idx_product_review_product (product_id, created_at)
);
