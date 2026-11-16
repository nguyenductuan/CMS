-- Persist whether the product approver is an ADMIN or SELLER.
ALTER TABLE products
    ADD COLUMN approve_by_role VARCHAR(20) NULL;
