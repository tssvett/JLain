package dev.tssvett.schedule_bot.bot.command.impl.settings;

import dev.tssvett.schedule_bot.bot.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.DirectMessageRequired;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.impl.notification.differenceschedule.ScheduleDifferenceNotificationKeyboard;
import dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class DifferenceScheduleNotificationSettingsCommand implements BotCommand {
    private final ScheduleDifferenceNotificationKeyboard scheduleDifferenceNotificationKeyboard;

    @Override
    @DirectMessageRequired
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageTextConstantsUtils.SETUP_NOTIFICATION)
                .replyMarkup(scheduleDifferenceNotificationKeyboard
                        .createInlineKeyboard(Action.SCHEDULE_DIFFERENCE_NOTIFICATION, userId))
                .build();
    }
}
