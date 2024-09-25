package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.backend.dto.BotUserInfoDto;
import dev.tssvett.schedule_bot.backend.entity.BotUser;
import dev.tssvett.schedule_bot.backend.service.UserService;
import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfoBotCommand implements BotCommand {
    private final UserService userService;

    @Override
    @Transactional
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId) {
        BotUser botUser = userService.findUserById(userId);

        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.createInfoMessageFromParams(BotUserInfoDto.builder()
                        .userId(userId)
                        .chatId(chatId)
                        .faculty(botUser.getFaculty())
                        .group(botUser.getGroup())
                        .course(botUser.getCourse())
                        .registrationState(botUser.getRegistrationState())
                        .notification(botUser.getNotification())
                        .build()))
                .build();
    }
}