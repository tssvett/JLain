package dev.tssvett.schedule_bot.persistence.repository;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
class FacultyRepositoryTest {

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
    private FacultyRepository facultyRepository;

    private List<FacultyRecord> expectedRecords;

    @BeforeEach
    void beforeEach() {
        expectedRecords = List.of(new FacultyRecord(1L, "Faculty 1"),
                new FacultyRecord(2L, "Faculty 2"), new FacultyRecord(3L, "Faculty 3"));
        facultyRepository.saveAll(expectedRecords);
    }

    @AfterEach
    void tearDown() {
        facultyRepository.deleteAll();
    }

    @Test
    void findAll() {
        //Act
        List<FacultyRecord> actualFaculties = facultyRepository.findAll();
        //Assert
        assertEquals(expectedRecords, actualFaculties);
    }

    @Test
    void findById() {
        //Act
        Optional<FacultyRecord> actualFaculty = facultyRepository.findById(1L);
        //Assert
        assertEquals(expectedRecords.get(0), actualFaculty.get());
    }

    @Test
    void deleteAll() {
        //Act
        facultyRepository.deleteAll();
        //Assert
        assertEquals(0, facultyRepository.findAll().size());
    }
}