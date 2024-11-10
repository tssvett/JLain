package dev.tssvett.schedule_bot.backend.mapper;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.bot.enums.LessonType;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.bot.enums.Subgroup;
import dev.tssvett.schedule_bot.persistence.model.tables.records.LessonRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;

import javax.validation.constraints.NotNull;

public class Mapper {

    public static StudentInfoDto toStudentInfoDto(@NotNull StudentRecord student) {
        return new StudentInfoDto(
                student.getUserId(),
                student.getChatId(),
                student.getCourse(),
                RegistrationState.valueOf(student.getRegistrationState()),
                student.getFacultyId(),
                student.getGroupId()
        );
    }

    public static LessonInfoDto toLessonInfoDto(@NotNull LessonRecord lesson) {
        return new LessonInfoDto(
                lesson.getId(),
                lesson.getName(),
                LessonType.fromName(lesson.getType()),
                lesson.getPlace(),
                lesson.getTeacher(),
                Subgroup.fromName(lesson.getSubgroup()),
                lesson.getTime(),
                lesson.getDateDay(),
                lesson.getDateNumber()
        );
    }

    public static LessonRecord toLesson(@NotNull LessonInfoDto lessonInfoDto) {
        return new LessonRecord(
                lessonInfoDto.lessonId(),
                lessonInfoDto.name(),
                lessonInfoDto.type().getName(),
                lessonInfoDto.place(),
                lessonInfoDto.teacher(),
                lessonInfoDto.subgroup().getName(),
                lessonInfoDto.time(),
                lessonInfoDto.dateDay(),
                lessonInfoDto.dateNumber());

    }
}