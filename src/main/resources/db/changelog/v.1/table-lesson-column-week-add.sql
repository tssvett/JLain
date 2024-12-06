--liquibase formatted sql

--changeset tssvett:table-lesson-column-week-add

ALTER TABLE lesson
ADD COLUMN week BIGINT NOT NULL;