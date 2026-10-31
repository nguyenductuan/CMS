ALTER TABLE order_return_request
    ADD COLUMN return_method VARCHAR(40) NULL,
    ADD COLUMN method_selection_deadline DATETIME NULL,
    ADD COLUMN method_selected_at DATETIME NULL;

CREATE INDEX idx_return_method_deadline
    ON order_return_request (status, method_selection_deadline);
