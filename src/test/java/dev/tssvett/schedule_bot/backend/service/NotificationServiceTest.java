package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.bot.enums.persistense.LessonType;
import dev.tssvett.schedule_bot.bot.enums.persistense.Subgroup;
import dev.tssvett.schedule_bot.bot.formatter.ScheduleStringFormatter;
import dev.tssvett.schedule_bot.persistence.model.tables.records.LessonRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.NotificationRecord;
import dev.tssvett.schedule_bot.persistence.repository.NotificationRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    NotificationRepository notificationRepository;

    @Mock
    LessonService lessonService;

    @Mock
    ScheduleStringFormatter scheduleStringFormatter;

    @InjectMocks
    NotificationService notificationService;

    private Map<String, List<LessonInfoDto>> map;
    private List<NotificationRecord> notifications;
    private ScheduleDifference scheduleDifference;

    @BeforeEach
    void setUp() {
        String key = "test_key";
        LessonInfoDto lessonInfoDto = new LessonInfoDto(UUID.randomUUID(), "test", LessonType.ANOTHER, "test",
                "test", Subgroup.FIRST, "test", "test", "test", 1L);
        List<LessonInfoDto> lessonInfoDtos = List.of(lessonInfoDto);
        map = Map.of(key, lessonInfoDtos);
        NotificationRecord notification = new NotificationRecord(1L, true, 1L, true);
        notifications = List.of(notification);
        List<LessonRecord> list = List.of(new LessonRecord(UUID.randomUUID(), "test", "другое", "test",
                "test", "1", "test", "test", "test", 1L, 1L));
        scheduleDifference = new ScheduleDifference(list, list, list, list);
    }

    @Test
    void findAllTomorrowScheduleWithRegisteredStudents() {
        //Act
        notificationService.findAllTomorrowScheduleWithRegisteredStudents();

        //Assert
        verify(notificationRepository).findAllEnabledTomorrowScheduleWithRegisteredStudents();
    }

    @Test
    void findAllEnableScheduleDifferenceWithRegisteredStudents() {
        //Act
        notificationService.findAllEnabledScheduleDifferenceWithRegisteredStudents();

        //Assert
        verify(notificationRepository).findAllEnabledScheduleDifferenceWithRegisteredStudents();
    }

    @Test
    void createTomorrowScheduleNotificationsMessages_happyPath() {
        //Arrange
        when(lessonService.getWeekScheduleMapByDate(any())).thenReturn(map);
        when(scheduleStringFormatter.formatToTomorrowNotificationMessage(any())).thenReturn("test");
        when(notificationRepository.findAllEnabledTomorrowScheduleWithRegisteredStudents()).thenReturn(notifications);

        //Act
        notificationService.createTomorrowScheduleNotificationsMessages();

        //Assert
        verify(lessonService).getWeekScheduleMapByDate(any());
        verify(scheduleStringFormatter).formatToTomorrowNotificationMessage(any());
    }

    @Test
    void createScheduleDifferenceNotificationsMessages_happyPath() {
        //Arrange
        when(scheduleStringFormatter.formatToScheduleDifference(any())).thenReturn("test");
        when(notificationRepository.findAllEnabledScheduleDifferenceWithRegisteredStudents()).thenReturn(notifications);
        when(lessonService.findScheduleDifference(any())).thenReturn(Optional.of(scheduleDifference));

        //Act
        notificationService.createScheduleDifferenceNotificationsMessages();

        //Assert
        verify(scheduleStringFormatter).formatToScheduleDifference(any());
    }
}