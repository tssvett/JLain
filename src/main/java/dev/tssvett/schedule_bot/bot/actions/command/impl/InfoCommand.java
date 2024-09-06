package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.backend.entity.BotUser;
import dev.tssvett.schedule_bot.backend.service.UserService;
import dev.tssvett.schedule_bot.bot.actions.command.Command;
import dev.tssvett.schedule_bot.bot.annotation.NoneRequired;
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
public class InfoCommand implements Command {
    private final UserService userService;

    @Override
    @NoneRequired
    @Transactional
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received {} from userId: {}", this.getClass().getSimpleName(), userId);
        return SendMessage.builder()
                .chatId(chatId)
                .text(createInfoMessage(userId, chatId))
                .build();
    }

    private String createInfoMessage(Long userId, Long chatId) {
        //Что-то надо делать с исключением
        BotUser botUser = userService.findUserById(userId);
        String facultyName = botUser.getFaculty().getName();
        String groupName = botUser.getGroup().getName();
        Long course = botUser.getCourse();
        RegistrationState registrationState = botUser.getRegistrationState();
        Boolean enabled = botUser.getNotification().getEnabled();

        return MessageConstants.createInfoMessageFromParams(userId, chatId, facultyName, groupName, course, registrationState, enabled);
    }
}
