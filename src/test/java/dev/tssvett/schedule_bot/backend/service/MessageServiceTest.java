package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.persistence.model.tables.records.MessageRecord;
import dev.tssvett.schedule_bot.persistence.repository.MessageRepository;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    MessageRepository messageRepository;

    @InjectMocks
    MessageService messageService;

    private SendMessage sendMessage;
    private MessageRecord messageRecord;
    private List<SendMessage> sendMessageList;
    private List<MessageRecord> messageRecordList;

    @BeforeEach
    void setUp() {
        sendMessage = new SendMessage("1234", "test");
        messageRecord = new MessageRecord(1234L, 1234L, "test");
        sendMessageList = List.of(sendMessage);
        messageRecordList = List.of(messageRecord);
    }

    @Test
    void findAllMessages() {
        //Arrange
        when(messageRepository.findAll()).thenReturn(messageRecordList);

        //Act
        List<SendMessage> result = messageService.findAllMessages();

        //Assert
        assertEquals(sendMessageList, result);
    }

    @Test
    void saveMessagesToDatabase() {
        //Arrange
        List<Long> studentIds = List.of(1234L);
        String message = "test";

        //Act
        messageService.saveMessagesToDatabase(studentIds, message);

        //Assert
        verify(messageRepository).saveAll(any());
    }

    @Test
    void removeAllMessages() {
        //Act
        messageService.removeAllMessages();

        //Assert
        verify(messageRepository).deleteAll();
    }
}