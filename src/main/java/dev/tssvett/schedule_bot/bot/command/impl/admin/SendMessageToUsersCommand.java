package dev.tssvett.schedule_bot.bot.command.impl.admin;

import dev.tssvett.schedule_bot.backend.client.TelegramClientService;
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
    //private final TelegramClientService telegramClientService;


    @Override
    @AdminRequired
    public SendMessage execute(Long userId, Long chatId) {
        List<Long> studentIds = studentService.findAll()
                .stream()
                .map(StudentRecord::getUserId)
                .toList();

        return processSendMessageToUsers(studentIds, chatId);
    }

    private SendMessage processSendMessageToUsers(List<Long> studentIds, Long chatId) {
        /*
        telegramClientService.sendMessageList(studentIds.stream()
                .map(userId -> SendMessage.builder()
                        .chatId(userId)
                        .text("darova")
                        .build())
                .toList());

         */
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageCreateUtils.createSendMessageToUsersMessage(studentIds))
                .build();
    }
}
