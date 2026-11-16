CREATE TABLE order_export_history (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    exported_at DATETIME NOT NULL,
    tab_name VARCHAR(100) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    from_date DATETIME NULL,
    to_date DATETIME NULL,
    exported_by VARCHAR(150) NOT NULL,
    row_count INT NOT NULL DEFAULT 0,
    INDEX idx_order_export_history_exported_at (exported_at)
);