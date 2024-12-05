package dev.tssvett.schedule_bot.bot.command.impl.schedule;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.service.LessonService;
import dev.tssvett.schedule_bot.bot.enums.persistense.LessonType;
import dev.tssvett.schedule_bot.bot.enums.persistense.Subgroup;
import dev.tssvett.schedule_bot.bot.formatter.ScheduleStringFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@ExtendWith(MockitoExtension.class)
class WeekScheduleBotCommandTest {
    @Mock
    LessonService lessonService;

    @Mock
    ScheduleStringFormatter scheduleStringFormatter;

    @InjectMocks
    WeekScheduleBotCommand weekScheduleBotCommand;

    private Map<String, List<LessonInfoDto>> map;

    @BeforeEach
    void setup() {
        LessonInfoDto lessonInfoDto = new LessonInfoDto(UUID.randomUUID(), "name", LessonType.LECTURE,
                "place", "teacher", Subgroup.FIRST, "8:00", "понедельник",
                "21.02.2024", 1L);
        List<LessonInfoDto> lessonInfoDtos = List.of(lessonInfoDto, lessonInfoDto);
        map = Map.of("key", lessonInfoDtos);
    }

    @Test
    void execute_happyPath() {
        //Arrange
        when(lessonService.getWeekScheduleMapByDate(1L)).thenReturn(map);
        when(scheduleStringFormatter.formatWeek(map)).thenReturn("StringLessongs");

        //Act
        SendMessage result = weekScheduleBotCommand.execute(1L, 1L);

        //Assert
        verify(lessonService).getWeekScheduleMapByDate(1L);
        verify(scheduleStringFormatter).formatWeek(map);
        assertEquals("StringLessongs", result.getText());
    }
}