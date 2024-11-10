--liquibase formatted sql

--changeset tssvett:table-student-column-notification_id-add

alter table student
add column notification_id BIGINT;

alter table student
add constraint fk_notification_id
foreign key (notification_id) references notification(id);