package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.NoneRequired;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartBotCommand implements BotCommand {
    private final StudentService studentService;

    @Override
    @NoneRequired
    public SendMessage execute(Long userId, Long chatId) {
        studentService.createUserIfNotExists(userId, chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.START_COMMAND)
                .build();
    }
}
