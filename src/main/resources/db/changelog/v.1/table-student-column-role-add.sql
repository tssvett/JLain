--liquibase formatted sql

--changeset tssvett:table-student-column-role-add

alter table student
add column role TEXT NOT NULL DEFAULT 'STUDENT';

ALTER TABLE student
ADD CONSTRAINT chk_role CHECK (role IN ('STUDENT', 'ADMIN'));