package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.annotation.NoneRequired;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.entity.BotUser;
import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfoCommand implements Command {
    private final UserRepository userRepository;

    @Override
    @NoneRequired
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received " + this.getClass().getSimpleName() + " from userId: {}", userId);
        return SendMessage.builder()
                .chatId(chatId)
                .text(createInfoMessage(userId, chatId))
                .build();
    }

    private String createInfoMessage(Long userId, Long chatId) {
        BotUser botUser = userRepository.findById(userId).orElse(null);
        if (botUser == null) {
            return MessageConstants.crateNotFoundUserMessage(userId);
        }

        String facultyName = botUser.getFacultyName();
        String groupName = botUser.getGroupName();
        String course = botUser.getCourse();
        RegistrationState registrationState = botUser.getRegistrationState();
        Boolean enabled = botUser.getNotification().getEnabled();

        return MessageConstants.createInfoMessageFromParams(userId, chatId, facultyName, groupName, course, registrationState, enabled);
    }
}
