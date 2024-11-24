package dev.tssvett.schedule_bot.bot.actions.command.impl.settings;

import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.DirectMessageRequired;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.enums.Action;
import dev.tssvett.schedule_bot.bot.keyboard.impl.notification.tomorrowschedule.TomorrowScheduleNotificationKeyboard;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class TomorrowScheduleNotificationSettingsCommand implements BotCommand {
    private final TomorrowScheduleNotificationKeyboard tomorrowScheduleNotificationKeyboard;

    @Override
    @DirectMessageRequired
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageTextConstantsUtils.SETUP_NOTIFICATION)
                .replyMarkup(tomorrowScheduleNotificationKeyboard
                        .createInlineKeyboard(Action.TOMORROW_SCHEDULE_NOTIFICATION, userId))
                .build();
    }
}
