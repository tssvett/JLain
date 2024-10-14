package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.utils.message.MessageCreateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfoBotCommand implements BotCommand {
    private final StudentService studentService;

    @Override
    @Transactional
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId) {
        StudentInfoDto studentInfoDto = studentService.getStudentInfoById(userId);

        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageCreateUtils.createInfoCommandMessageText(studentInfoDto))
                .build();
    }
}