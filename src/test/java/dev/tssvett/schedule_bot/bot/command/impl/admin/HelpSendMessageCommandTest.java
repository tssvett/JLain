package dev.tssvett.schedule_bot.bot.command.impl.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@ExtendWith(MockitoExtension.class)
class HelpSendMessageCommandTest {

    @InjectMocks
    HelpSendMessageCommand helpSendMessageCommand;

    @BeforeEach
    void setUp() {
    }

    @Test
    void execute() {
        Long userId = 123L;
        Long chatId = 456L;
        String argument = "testArgument";

        SendMessage sendMessage = helpSendMessageCommand.execute(userId, chatId, argument);

        assertNotNull(sendMessage);
        assertEquals(chatId.toString(), sendMessage.getChatId());
        assertEquals("\uD83C\uDF40 Для рассылки используется команда /send_message_to_users" +
                " [сообщение, которое будет разослано]\n", sendMessage.getText());
    }
}
