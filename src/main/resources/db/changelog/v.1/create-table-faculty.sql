--liquibase formatted sql

--changeset tssvett:create-table-faculty

CREATE TABLE IF NOT EXISTS faculty(
    faculty_id BIGINT PRIMARY KEY,
    name TEXT NOT NULL
);

