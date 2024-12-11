package dev.tssvett.schedule_bot.persistence.repository;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.telegram.telegrambots.longpolling.starter.TelegramBotInitializer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
class GroupRepositoryTest {

    @MockBean
    TelegramBotInitializer telegramBotInitializer;

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
    private GroupRepository groupRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private List<EducationalGroupRecord> educationalGroupRecords;


    @BeforeEach
    void setup() {
        facultyRepository.saveAll(List.of(new FacultyRecord(1L, "faculty1"), new FacultyRecord(2L, "faculty2")));
        educationalGroupRecords = List.of(new EducationalGroupRecord(1L, "group1", 1L, 1L),
                new EducationalGroupRecord(2L, "group2", 2L, 2L));
        groupRepository.saveAll(educationalGroupRecords);
    }

    @AfterEach
    void tearDown() {
        groupRepository.deleteAll();
    }

    @Test
    void findAll() {
        //Act
        List<EducationalGroupRecord> actualFaculties = groupRepository.findAll();
        //Assert
        assertEquals(educationalGroupRecords, actualFaculties);
    }

    @Test
    void findById() {
        //Act
        Optional<EducationalGroupRecord> actualFaculty = groupRepository.findById(1L);
        //Assert
        assertEquals(educationalGroupRecords.get(0), actualFaculty.get());
    }

    @Test
    void deleteAll() {
        //Act
        groupRepository.deleteAll();
        //Assert
        assertEquals(0, groupRepository.findAll().size());
    }

    @Test
    void findAllByFacultyIdAndCourse() {
        //Act
        List<EducationalGroupRecord> actualFaculties = groupRepository
                .findAllByFacultyIdAndCourse(1L, 1L);
        //Assert
        assertEquals(List.of(educationalGroupRecords.get(0)), actualFaculties);
    }
}
