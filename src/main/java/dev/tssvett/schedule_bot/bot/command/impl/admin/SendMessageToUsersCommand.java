package dev.tssvett.schedule_bot.bot.command.impl.admin;

import dev.tssvett.schedule_bot.backend.service.MessageService;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.annotation.AdminRequired;
import dev.tssvett.schedule_bot.bot.command.BotCommand;
import dev.tssvett.schedule_bot.bot.utils.message.MessageCreateUtils;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class SendMessageToUsersCommand implements BotCommand {
    private final StudentService studentService;
    private final MessageService messageService;

    @Override
    @AdminRequired
    public SendMessage execute(Long userId, Long chatId, String message) {
        List<Long> studentIds = studentService.findAll()
                .stream()
                .map(StudentRecord::getUserId)
                .toList();

        return processSendMessageToUsers(studentIds, chatId, message);
    }

    private SendMessage processSendMessageToUsers(List<Long> studentIds, Long chatId, String message) {
        if (isBlankArgument(message)) {
            return createWarningNotBlankMessage(chatId);
        }
        messageService.saveMessagesToDatabase(studentIds, message);

        return createSendMessageToUsersMessage(studentIds, chatId);
    }

    private boolean isBlankArgument(String argument) {
        return argument == null || argument.isBlank();
    }

    private SendMessage createSendMessageToUsersMessage(List<Long> studentIds, Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageCreateUtils.createSendMessageToUsersMessage(studentIds))
                .build();
    }

    private SendMessage createWarningNotBlankMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageCreateUtils.createNotBlankMessageWarning())
                .build();
    }
}
