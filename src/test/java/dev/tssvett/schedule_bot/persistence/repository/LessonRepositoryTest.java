package dev.tssvett.schedule_bot.persistence.repository;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.LessonRecord;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
class LessonRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(new HostConfig()
                    .withPortBindings(new PortBinding(Ports.Binding.bindPort(23333), new ExposedPort(5432)))));

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgresContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgresContainer::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);
    }

    @BeforeAll
    static void startContainer() {
        postgresContainer.start();
    }

    @AfterAll
    static void stopContainer() {
        postgresContainer.stop();
    }

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private List<LessonRecord> lessonRecords;

    @BeforeEach
    void setup() {
        facultyRepository.saveAll(List.of(new FacultyRecord(1L, "faculty1"),
                new FacultyRecord(2L, "faculty2")));
        groupRepository.saveAll(List.of(new EducationalGroupRecord(1L, "group1", 1L, 1L),
                new EducationalGroupRecord(2L, "group2", 2L, 2L)));

        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        lessonRecords = List.of(new LessonRecord(uuid1, "Math", "лекция", "Room 101",
                        "Teacher", "1", "8:00", "monday", "04.12.2024",
                        1L, 1L),
                new LessonRecord(uuid2, "Math", "лекция", "Room 101", "Teacher", "1",
                        "8:00", "monday", "04.12.2024", 1L, 1L));
        lessonRepository.saveAll(lessonRecords);
    }

    @AfterEach
    void tearDown() {
        lessonRepository.deleteAll();
    }

    @Test
    void findAll() {
        //Act
        List<LessonRecord> actualLessons = lessonRepository.findAllLessons();
        //Assert
        assertEquals(lessonRecords, actualLessons);
    }

    @Test
    void findLessonsByGroupIdAndEducationalWeek() {
        //Act
        List<LessonRecord> actualLessons = lessonRepository
                .findLessonsByGroupIdAndEducationalWeek(1L, 1L);
        //Assert
        assertEquals(lessonRecords, actualLessons);
    }

    @Test
    void findLessonsByGroupIdAndEducationalDay() {
        //Act
        List<LessonRecord> actualLessons = lessonRepository
                .findLessonsByGroupIdAndEducationalDayNumber(1L, "04.12.2024");
        //Assert
        assertEquals(lessonRecords, actualLessons);
    }

    @Test
    void deleteAll() {
        //Act
        lessonRepository.deleteAll();
        //Assert
        assertEquals(0, lessonRepository.findAllLessons().size());
    }
}
