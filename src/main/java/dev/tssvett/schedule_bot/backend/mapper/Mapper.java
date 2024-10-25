package dev.tssvett.schedule_bot.backend.mapper;

import dev.tssvett.schedule_bot.backend.dto.FacultyInfoDto;
import dev.tssvett.schedule_bot.backend.dto.GroupInfoDto;
import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.dto.NotificationInfoDto;
import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.persistence.entity.Faculty;
import dev.tssvett.schedule_bot.persistence.entity.Group;
import dev.tssvett.schedule_bot.persistence.entity.Lesson;
import dev.tssvett.schedule_bot.persistence.entity.Notification;
import dev.tssvett.schedule_bot.persistence.entity.Student;

import javax.validation.constraints.NotNull;

public class Mapper {

    public static StudentInfoDto toStudentInfoDto(@NotNull Student student) {
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

    public static Student toStudent(@NotNull StudentInfoDto studentInfoDto) {
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

    public static GroupInfoDto toGroupInfoDto(@NotNull Group group) {
        return new GroupInfoDto(
                group.getGroupId(),
                group.getName(),
                group.getCourse(),
                toFacultyInfoDto(group.getFaculty())
        );
    }

    public static Group toGroup(@NotNull GroupInfoDto groupInfoDto) {
        return Group.builder()
                .groupId(groupInfoDto.groupId())
                .name(groupInfoDto.name())
                .course(groupInfoDto.course())
                .faculty(toFaculty(groupInfoDto.faculty()))
                .build();
    }

    public static FacultyInfoDto toFacultyInfoDto(@NotNull Faculty faculty) {
        return new FacultyInfoDto(
                faculty.getFacultyId(),
                faculty.getName()
        );
    }

    public static Faculty toFaculty(@NotNull FacultyInfoDto facultyInfoDto) {
        return Faculty.builder()
                .facultyId(facultyInfoDto.facultyId())
                .name(facultyInfoDto.name())
                .build();
    }

    public static Notification toNotification(@NotNull NotificationInfoDto notification) {
        return Notification.builder()
                .id(notification.notificationId())
                .enabled(notification.enabled())
                .build();
    }

    public static NotificationInfoDto toNotificationInfoDto(@NotNull Notification notification) {
        return new NotificationInfoDto(
                notification.getId(),
                notification.isEnabled()
        );
    }

    public static LessonInfoDto toLessonInfoDto(@NotNull Lesson lesson) {
        return new LessonInfoDto(
                lesson.getId(),
                lesson.getName(),
                lesson.getType(),
                lesson.getPlace(),
                lesson.getTeacher(),
                lesson.getSubgroup(),
                lesson.getTime(),
                lesson.getDateDay(),
                lesson.getDateNumber()
        );
    }

    public static Lesson toLesson(@NotNull LessonInfoDto lessonInfoDto) {
        return Lesson.builder()
                .id(lessonInfoDto.lessonId())
                .name(lessonInfoDto.name())
                .type(lessonInfoDto.type())
                .place(lessonInfoDto.place())
                .teacher(lessonInfoDto.teacher())
                .subgroup(lessonInfoDto.subgroup())
                .time(lessonInfoDto.time())
                .dateDay(lessonInfoDto.dateDay())
                .dateNumber(lessonInfoDto.dateNumber())
                .build();
    }
}