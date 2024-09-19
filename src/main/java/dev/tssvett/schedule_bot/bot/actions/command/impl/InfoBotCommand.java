package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.backend.entity.BotUser;
import dev.tssvett.schedule_bot.backend.service.UserService;
import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.bot.enums.RegistrationState;
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
        log.info("Received {} from userId: {}", this.getClass().getSimpleName(), userId);
        BotUser botUser = userService.findUserById(userId);
        String facultyName = botUser.getFaculty().getName();
        String groupName = botUser.getGroup().getName();
        Long course = botUser.getCourse();
        RegistrationState registrationState = botUser.getRegistrationState();
        Boolean enabled = botUser.getNotification().getEnabled();
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.createInfoMessageFromParams(userId, chatId, facultyName, groupName, course, registrationState, enabled))
                .build();
    }
}
