--liquibase formatted sql

--changeset tssvett:create-table-notification

CREATE TABLE IF NOT EXISTS notification (
    id BIGSERIAL PRIMARY KEY,
    enabled BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES student(user_id)
);