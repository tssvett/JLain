--liquibase formatted sql

--changeset tssvett:table-notification-column-enabled-rename

alter table notification
rename column enabled to tomorrow_schedule_enabled;