--liquibase formatted sql

--changeset tssvett:table-notification-column-tomorrow-add

alter table notification
add column schedule_difference_enabled BOOLEAN NOT NULL DEFAULT TRUE;