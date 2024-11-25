--liquibase formatted sql

--changeset tssvett:table-notification-column-student_id-add

alter table notification
add column student_id BIGINT;

alter table notification
add constraint fk_student_id
foreign key (student_id) references student(user_id);