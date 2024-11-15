--liquibase formatted sql

--changeset tssvett:create-table-student

CREATE TABLE IF NOT EXISTS student (
    user_id BIGINT PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    course BIGINT,
    registration_state TEXT NOT NULL,
    faculty_id BIGINT,
    group_id BIGINT,
    FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id),
    FOREIGN KEY (group_id) REFERENCES educational_group(group_id)
);