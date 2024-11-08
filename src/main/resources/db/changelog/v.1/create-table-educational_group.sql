--liquibase formatted sql

--changeset tssvett:create-table-educational_group

CREATE TABLE IF NOT EXISTS educational_group(
    group_id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    course BIGINT NOT NULL,
    faculty_id BIGINT NOT NULL,
    FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id)
);

