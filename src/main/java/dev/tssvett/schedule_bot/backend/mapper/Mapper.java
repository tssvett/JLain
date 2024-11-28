package dev.tssvett.schedule_bot.backend.mapper;

import dev.tssvett.schedule_bot.backend.dto.FacultyInfoDto;
import dev.tssvett.schedule_bot.backend.dto.GroupInfoDto;
import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.dto.NotificationInfoDto;
import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.bot.enums.persistense.LessonType;
import dev.tssvett.schedule_bot.bot.enums.persistense.RegistrationState;
import dev.tssvett.schedule_bot.bot.enums.persistense.Role;
import dev.tssvett.schedule_bot.bot.enums.persistense.Subgroup;
import dev.tssvett.schedule_bot.parsing.dto.FacultyParserDto;
import dev.tssvett.schedule_bot.parsing.dto.GroupParserDto;
import dev.tssvett.schedule_bot.parsing.dto.LessonParserDto;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.LessonRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.NotificationRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Mapper {

    public static StudentInfoDto toStudentInfoDto(StudentRecord student) {
        return new StudentInfoDto(
                student.getUserId(),
                student.getChatId(),
                student.getCourse(),
                RegistrationState.valueOf(student.getRegistrationState()),
                student.getFacultyId(),
                student.getGroupId(),
                student.getNotificationId(),
                Role.valueOf(student.getRole())
        );
    }

    public static StudentRecord toStudentRecord(StudentInfoDto student) {
        return new StudentRecord(
                student.userId(),
                student.chatId(),
                student.course(),
                student.registrationState().name(),
                student.facultyId(),
                student.groupId(),
                student.notificationId(),
                student.role().name()
        );
    }

    public static LessonInfoDto toLessonInfoDto(LessonRecord lesson) {
        return new LessonInfoDto(
                lesson.getId(),
                lesson.getName(),
                LessonType.fromName(lesson.getType()),
                lesson.getPlace(),
                lesson.getTeacher(),
                Subgroup.fromName(lesson.getSubgroup()),
                lesson.getTime(),
                lesson.getDateDay(),
                lesson.getDateNumber(),
                lesson.getGroupId()
        );
    }

    public static LessonRecord toLessonRecord(LessonInfoDto lesson) {
        return new LessonRecord(
                lesson.lessonId(),
                lesson.name(),
                lesson.type().getName(),
                lesson.place(),
                lesson.teacher(),
                lesson.subgroup().getName(),
                lesson.time(),
                lesson.dateDay(),
                lesson.dateNumber(),
                lesson.groupId()
        );
    }

    public static LessonRecord toLessonRecord(LessonParserDto lesson) {
        return new LessonRecord(
                lesson.id(),
                lesson.name(),
                LessonType.fromName(lesson.type()).getName(),
                lesson.place(),
                lesson.teacher(),
                Subgroup.fromName(lesson.subgroup()).getName(),
                lesson.time(),
                lesson.dateDay(),
                lesson.dateNumber(),
                lesson.groupId()
        );
    }

    public static GroupInfoDto toGroupInfoDto(EducationalGroupRecord educationalGroupRecord) {
        return new GroupInfoDto(
                educationalGroupRecord.getGroupId(),
                educationalGroupRecord.getName(),
                educationalGroupRecord.getCourse(),
                educationalGroupRecord.getFacultyId()
        );
    }

    public static EducationalGroupRecord toEducationalGroupRecord(GroupInfoDto group) {
        return new EducationalGroupRecord(
                group.groupId(),
                group.name(),
                group.course(),
                group.facultyId()
        );
    }

    public static EducationalGroupRecord toEducationalGroupRecord(GroupParserDto groupParserDto) {
        return new EducationalGroupRecord(
                groupParserDto.groupId(),
                groupParserDto.groupName(),
                null,
                null
        );
    }

    public static FacultyInfoDto toFacultyInfoDto(EducationalGroupRecord educationalGroupRecord) {
        return new FacultyInfoDto(
                educationalGroupRecord.getFacultyId(),
                educationalGroupRecord.getName()
        );
    }

    public static FacultyRecord toFacultyRecord(FacultyInfoDto faculty) {
        return new FacultyRecord(
                faculty.facultyId(),
                faculty.name()
        );
    }

    public static FacultyRecord toFacultyRecord(FacultyParserDto facultyParserDto) {
        return new FacultyRecord(
                facultyParserDto.facultyId(),
                facultyParserDto.facultyName()
        );
    }

    public static NotificationInfoDto toNotificationInfoDto(NotificationRecord notificationRecord) {
        return new NotificationInfoDto(
                notificationRecord.getId(),
                notificationRecord.getTomorrowScheduleEnabled(),
                notificationRecord.getScheduleDifferenceEnabled(),
                notificationRecord.getStudentId()
        );
    }

    public static NotificationRecord toNotificationRecord(NotificationInfoDto notification) {
        return new NotificationRecord(
                notification.notificationId(),
                notification.tomorrowScheduleEnabled(),
                notification.studentId(),
                notification.scheduleDifferenceEnabled()
        );
    }
}
