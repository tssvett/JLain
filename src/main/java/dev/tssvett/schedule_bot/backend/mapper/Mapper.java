package dev.tssvett.schedule_bot.backend.mapper;

import dev.tssvett.schedule_bot.backend.dto.FacultyInfoDto;
import dev.tssvett.schedule_bot.backend.dto.GroupInfoDto;
import dev.tssvett.schedule_bot.backend.dto.NotificationInfoDto;
import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.persistence.entity.Faculty;
import dev.tssvett.schedule_bot.persistence.entity.Group;
import dev.tssvett.schedule_bot.persistence.entity.Notification;
import dev.tssvett.schedule_bot.persistence.entity.Student;

public class Mapper {

    public static StudentInfoDto toStudentInfoDto(Student student) {
        return new StudentInfoDto(
                student.getUserId(),
                student.getChatId(),
                student.getCourse(),
                student.getRegistrationState(),
                toFacultyInfoDto(student.getFaculty()),
                toGroupInfoDto(student.getGroup()),
                toNotificationInfoDto(student.getNotification())
        );
    }

    public static Student toStudent(StudentInfoDto studentInfoDto) {
        return Student.builder()
                .userId(studentInfoDto.userId())
                .chatId(studentInfoDto.chatId())
                .course(studentInfoDto.course())
                .registrationState(studentInfoDto.registrationState())
                .faculty(toFaculty(studentInfoDto.faculty()))
                .group(toGroup(studentInfoDto.group()))
                .notification(toNotification(studentInfoDto.notification()))
                .build();
    }

    public static GroupInfoDto toGroupInfoDto(Group group) {
        return new GroupInfoDto(
                group.getGroupId(),
                group.getName(),
                group.getCourse(),
                toFacultyInfoDto(group.getFaculty())
        );
    }

    public static Group toGroup(GroupInfoDto groupInfoDto) {
        return Group.builder()
                .groupId(groupInfoDto.groupId())
                .name(groupInfoDto.name())
                .course(groupInfoDto.course())
                .faculty(toFaculty(groupInfoDto.faculty()))
                .build();
    }

    public static FacultyInfoDto toFacultyInfoDto(Faculty faculty) {
        return new FacultyInfoDto(
                faculty.getFacultyId(),
                faculty.getName()
        );
    }

    public static Faculty toFaculty(FacultyInfoDto facultyInfoDto) {
        return Faculty.builder()
                .facultyId(facultyInfoDto.facultyId())
                .name(facultyInfoDto.name())
                .build();
    }

    public static Notification toNotification(NotificationInfoDto notification) {
        return Notification.builder()
                .id(notification.notificationId())
                .enabled(notification.enabled())
                .build();
    }

    public static NotificationInfoDto toNotificationInfoDto(Notification notification) {
        return new NotificationInfoDto(
                notification.getId(),
                notification.getEnabled()
        );
    }

}