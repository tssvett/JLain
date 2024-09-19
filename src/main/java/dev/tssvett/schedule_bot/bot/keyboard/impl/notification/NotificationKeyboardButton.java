package dev.tssvett.schedule_bot.bot.keyboard.impl.notification;

import dev.tssvett.schedule_bot.backend.entity.BotUser;
import dev.tssvett.schedule_bot.backend.service.UserService;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKeyboardButton implements KeyboardButton {
    private final UserService userService;

    @Override
    public SendMessage click(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Long userId = update.getCallbackQuery().getFrom().getId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String notificationStatus = callbackDetails.getCallbackInformation();

        return createNotificationSendMessage(userId, chatId, notificationStatus);
    }

    public SendMessage createNotificationSendMessage(Long userId, Long chatId, String notificationStatus) {
        boolean enableNotification = notificationStatus.equals("Включить");
        BotUser user = userService.chooseNotification(userId, enableNotification);
        log.info("User {} successfully choose notification {}", user.getUserId(), enableNotification);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Правила уведомлений успешно обновлены!")
                .build();
    }
}
