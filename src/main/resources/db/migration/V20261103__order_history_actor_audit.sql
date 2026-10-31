-- Extend order_history with actor, event and timestamp details.
ALTER TABLE order_history
    ADD COLUMN description VARCHAR(1000) NULL,
    ADD COLUMN actor VARCHAR(150) NULL,
    ADD COLUMN actor_role VARCHAR(30) NULL,
    ADD COLUMN event_code VARCHAR(30) NULL,
    ADD COLUMN created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

CREATE INDEX idx_order_history_order_time ON order_history (order_id, created_at, id);
