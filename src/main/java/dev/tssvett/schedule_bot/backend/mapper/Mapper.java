package dev.tssvett.schedule_bot.backend.mapper;

import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.persistence.entity.Student;

public class Mapper {

    public static StudentInfoDto toStudentInfoDto(Student student) {
        return new StudentInfoDto(
                student.getUserId(),
                student.getChatId(),
                student.getCourse(),
                student.getRegistrationState(),
                student.getFaculty(),
                student.getGroup(),
                student.getNotification()
        );
    }

    public static Student toStudent(StudentInfoDto studentInfoDto) {
        return Student.builder()
                .userId(studentInfoDto.userId())
                .chatId(studentInfoDto.chatId())
                .course(studentInfoDto.course())
                .registrationState(studentInfoDto.registrationState())
                .faculty(studentInfoDto.faculty())
                .group(studentInfoDto.group())
                .notification(studentInfoDto.notification())
                .build();
    }
}