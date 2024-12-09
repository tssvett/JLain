package dev.tssvett.schedule_bot.persistence.repository;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import static dev.tssvett.schedule_bot.bot.enums.persistense.RegistrationState.START_REGISTER;
import dev.tssvett.schedule_bot.bot.enums.persistense.Role;
import dev.tssvett.schedule_bot.persistence.model.tables.records.NotificationRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
class StudentRepositoryTest {

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
        dynamicPropertyRegistry.add("spring.liquibase.url", postgresContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.liquibase.user", postgresContainer::getUsername);
        dynamicPropertyRegistry.add("spring.liquibase.password", postgresContainer::getPassword);
        dynamicPropertyRegistry.add("spring.liquibase.driver-class-name", postgresContainer::getDriverClassName);
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
    private StudentRepository studentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private List<StudentRecord> studentRecords;


    @BeforeEach
    void setup() {
        studentRecords = List.of(new StudentRecord(1L, 1L, 1L, START_REGISTER.name(), null,
                null, null, Role.STUDENT.name()));
        studentRepository.save(studentRecords.get(0));
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
    }

    @Test
    void findAll() {
        //Act
        List<StudentRecord> actualRecords = studentRepository.findAll();
        //Assert
        assertEquals(studentRecords, actualRecords);
    }

    @Test
    void findById() {
        //Act
        Optional<StudentRecord> actualRecord = studentRepository.findById(1L);
        //Assert
        assertEquals(studentRecords.get(0), actualRecord.get());
    }

    @Test
    void updateAllFields() {
        //Act
        studentRepository.updateAllFields(1L, new StudentRecord(1L, 1L, 1L, "GROUP_CHOOSING", null, null, null, Role.STUDENT.name()));
        //Assert
        assertEquals(1, studentRepository.findAll().size());
        assertEquals("GROUP_CHOOSING", studentRepository.findById(1L).get().getRegistrationState());
    }

    @Test
    void updateState() {
        //Act
        studentRepository.updateState(1L, "GROUP_CHOOSING");
        //Assert
        assertEquals(1, studentRepository.findAll().size());
        assertEquals("GROUP_CHOOSING", studentRepository.findById(1L).get().getRegistrationState());
    }

    @Test
    void updateNotificationId() {
        //Arrange
        notificationRepository.save(new NotificationRecord(1L, true, null, true));

        //Act
        studentRepository.updateNotificationId(new StudentRecord(1L, 1L, 1L, "GROUP_CHOOSING", null, null, null, Role.STUDENT.name()), 1L);
        //Assert
        assertEquals(1, studentRepository.findAll().size());
        assertEquals(1L, studentRepository.findById(1L).get().getNotificationId());
    }

    @Test
    void deleteAll() {
        //Act
        studentRepository.deleteAll();
        //Assert
        assertEquals(0, studentRepository.findAll().size());
    }
}
