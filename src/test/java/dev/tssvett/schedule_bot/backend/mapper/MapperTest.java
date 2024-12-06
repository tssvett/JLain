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
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

class MapperTest {
    private StudentRecord studentRecord;
    private StudentInfoDto studentInfoDto;

    private LessonRecord lessonRecord;
    private LessonInfoDto lessonInfoDto;
    private LessonParserDto lessonParserDto;

    private EducationalGroupRecord educationalGroupRecord;
    private GroupParserDto groupParserDto;

    private FacultyRecord facultyRecord;
    private FacultyParserDto facultyParserDto;

    private MessageRecord messageRecord;
    private SendMessage sendMessage;

    @BeforeEach
    void setUp() {
        studentRecord = new StudentRecord(123L, 123L, 1L,
                RegistrationState.FACULTY_CHOOSING.toString(), 123L, 123L, 123L,
                Role.STUDENT.toString());
        studentInfoDto = new StudentInfoDto(123L, 123L, 1L, RegistrationState.FACULTY_CHOOSING,
                123L, 123L, 123L, Role.STUDENT);

        UUID uuid = UUID.randomUUID();
        lessonRecord = new LessonRecord(uuid, "lesson", "практика", "place", "teacher",
                "1", "time", "day", "number", 123L, 1L);
        lessonInfoDto = new LessonInfoDto(uuid, "lesson", LessonType.fromName("практика"),
                "place", "teacher", Subgroup.fromName("1"),
                "time", "day", "number", 123L);
        lessonParserDto = new LessonParserDto(uuid, "lesson", "практика", "place", "teacher",
                "1", "time", "day", "number", 123L, 1L);

        educationalGroupRecord = new EducationalGroupRecord(123L, "group", 123L, 123L);
        groupParserDto = new GroupParserDto(123L, "group");

        facultyRecord = new FacultyRecord(123L, "faculty");
        facultyParserDto = new FacultyParserDto(123L, "faculty");

        messageRecord = new MessageRecord(123L, 123L, "message");
        sendMessage = new SendMessage("123", "message");
    }

    @Test
    void toStudentInfoDto() {
        StudentInfoDto actualStudent = Mapper.toStudentInfoDto(studentRecord);
        assertEquals(studentInfoDto, actualStudent);
    }

    @Test
    void toLessonInfoDto() {
        LessonInfoDto actualLesson = Mapper.toLessonInfoDto(lessonRecord);
        assertEquals(lessonInfoDto, actualLesson);
    }

    @Test
    void toLessonRecord() {
        LessonRecord actualLesson = Mapper.toLessonRecord(lessonParserDto);
        assertEquals(lessonRecord, actualLesson);
    }

    @Test
    void toEducationalGroupRecord() {
        EducationalGroupRecord actualGroup = Mapper.toEducationalGroupRecord(groupParserDto);
        educationalGroupRecord.setCourse(null);
        educationalGroupRecord.setFacultyId(null);

        assertEquals(educationalGroupRecord, actualGroup);
    }

    @Test
    void toFacultyRecord() {
        FacultyRecord actualFaculty = Mapper.toFacultyRecord(facultyParserDto);
        assertEquals(facultyRecord, actualFaculty);
    }

    @Test
    void toSendMessage() {
        SendMessage actualMessage = Mapper.toSendMessage(messageRecord);
        assertEquals(sendMessage, actualMessage);
    }
}