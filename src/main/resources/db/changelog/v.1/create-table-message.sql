--liquibase formatted sql

--changeset tssvett::create-table-message

CREATE TABLE IF NOT EXISTS message(
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    text TEXT NOT NULL
);