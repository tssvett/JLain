--liquibase formatted sql

--changeset tssvett:create-table-lesson

CREATE TABLE IF NOT EXISTS lesson(
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    type TEXT NOT NULL,
    place TEXT NOT NULL,
    teacher TEXT NOT NULL,
    subgroup TEXT NOT NULL,
    time TEXT NOT NULL,
    date_day TEXT NOT NULL,
    date_number TEXT NOT NULL
);