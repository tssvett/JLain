--liquibase formatted sql

--changeset tssvett:create-table-notification

CREATE TABLE IF NOT EXISTS notification (
    id BIGSERIAL PRIMARY KEY,
    enabled BOOLEAN NOT NULL
);