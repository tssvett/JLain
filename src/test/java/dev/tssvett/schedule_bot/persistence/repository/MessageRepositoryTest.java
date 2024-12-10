package dev.tssvett.schedule_bot.persistence.repository;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import dev.tssvett.schedule_bot.persistence.model.tables.records.MessageRecord;
import java.util.List;
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
class MessageRepositoryTest {

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
    private MessageRepository messageRepository;

    private List<MessageRecord> messageRecords;

    @BeforeEach
    void beforeEach() {
        messageRecords = List.of(new MessageRecord(1L, 1L, "test"));
        messageRepository.saveAll(messageRecords);
    }

    @AfterEach
    void tearDown() {
        messageRepository.deleteAll();
    }

    @Test
    void findAll() {
        //Act
        List<MessageRecord> messageRecordList = messageRepository.findAll();
        //Assert
        assertEquals(messageRecords, messageRecordList);
    }

    @Test
    void deleteAll() {
        //Act
        messageRepository.deleteAll();
        //Assert
        assertEquals(0, messageRepository.findAll().size());
    }
}