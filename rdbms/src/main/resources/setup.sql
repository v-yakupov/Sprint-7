--liquibase formatted sql

--changeset vladislav:init

ALTER TABLE account1 ADD CONSTRAINT positive_amount CHECK (amount >= 0);
CREATE UNIQUE INDEX ON account1(id);
INSERT INTO account1 VALUES (1, 1000, 0), (2, 500, 0);