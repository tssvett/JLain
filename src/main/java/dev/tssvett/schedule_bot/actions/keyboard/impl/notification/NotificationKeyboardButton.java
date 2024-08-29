package dev.tssvett.schedule_bot.actions.keyboard.impl.notification;

import dev.tssvett.schedule_bot.actions.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.entity.Notification;
import dev.tssvett.schedule_bot.repository.NotificationRepository;
import dev.tssvett.schedule_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKeyboardButton implements KeyboardButton {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public SendMessage click(Update update) {
        CallbackDetails callbackDetails = CallbackDetails.fromString(update.getCallbackQuery().getData());
        Long userId = update.getCallbackQuery().getFrom().getId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        if (callbackDetails.getCallbackInformation().equals("Включить")) {
            userRepository.findById(userId).ifPresent(user -> {
                Notification notification = user.getNotification();
                notification.setEnabled(true);
                notificationRepository.save(notification);
                userRepository.save(user);
            });
        } else {
            userRepository.findById(userId).ifPresent(user -> {
                Notification notification = user.getNotification();
                notification.setEnabled(false);
                notificationRepository.save(notification);
                userRepository.save(user);
            });
        }
        return SendMessage.builder()
                .chatId(chatId)
                .text("Правила уведомлений успешно обновлены!")
                .build();
    }
}
