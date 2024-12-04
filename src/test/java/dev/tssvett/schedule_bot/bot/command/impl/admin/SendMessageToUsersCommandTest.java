package dev.tssvett.schedule_bot.bot.command.impl.admin;

import dev.tssvett.schedule_bot.backend.service.MessageService;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@ExtendWith(MockitoExtension.class)
class SendMessageToUsersCommandTest {

    @Mock
    StudentService studentService;

    @Mock
    MessageService messageService;

    @InjectMocks
    SendMessageToUsersCommand sendMessageToUsersCommand;

    @BeforeEach
    void setUp() {
    }

    @Test
    void executeWithValidMessage() {
        Long userId = 123L;
        Long chatId = 456L;
        String message = "Hello, students!";

        StudentRecord studentRecord = new StudentRecord();
        studentRecord.setUserId(789L);

        when(studentService.findAll()).thenReturn(List.of(studentRecord));

        SendMessage sendMessage = sendMessageToUsersCommand.execute(userId, chatId, message);

        assertNotNull(sendMessage);
        assertEquals(chatId.toString(), sendMessage.getChatId());
        assertEquals("\uD83C\uDF40 Сообщение добавлено в базу для дальнейшей рассылки 1 пользователям\n",
                sendMessage.getText());
    }

    @Test
    void executeWithBlankMessage() {
        Long userId = 123L;
        Long chatId = 456L;
        String message = "   ";

        SendMessage sendMessage = sendMessageToUsersCommand.execute(userId, chatId, message);

        assertNotNull(sendMessage);
        assertEquals(chatId.toString(), sendMessage.getChatId());
        assertEquals("⚠\uFE0F Внимание! Эта команда должна использовать 1 аргумент\n", sendMessage.getText());
    }
}
