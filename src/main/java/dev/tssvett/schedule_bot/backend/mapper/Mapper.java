package dev.tssvett.schedule_bot.backend.mapper;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
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
import dev.tssvett.schedule_bot.persistence.model.tables.records.MessageRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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

    public static EducationalGroupRecord toEducationalGroupRecord(GroupParserDto groupParserDto) {
        return new EducationalGroupRecord(
                groupParserDto.groupId(),
                groupParserDto.groupName(),
                null,
                null
        );
    }

    public static FacultyRecord toFacultyRecord(FacultyParserDto facultyParserDto) {
        return new FacultyRecord(
                facultyParserDto.facultyId(),
                facultyParserDto.facultyName()
        );
    }

    public static SendMessage toSendMessage(MessageRecord messageRecord) {
        return SendMessage.builder()
                .chatId(messageRecord.getChatId())
                .text(messageRecord.getText())
                .build();
    }
}
