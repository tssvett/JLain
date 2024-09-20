package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.DirectMessageRequired;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.enums.Action;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
import dev.tssvett.schedule_bot.bot.keyboard.impl.notification.NotificationKeyboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationBotCommand implements BotCommand {
    private final NotificationKeyboard notificationKeyboard;

    @Override
    @DirectMessageRequired
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageConstants.SETUP_NOTIFICATION)
                .replyMarkup(notificationKeyboard.createInlineKeyboard(Action.NOTIFICATION, userId))
                .build();
    }
}
