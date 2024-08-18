package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.actions.keyboard.impl.NotificationKeyboard;
import dev.tssvett.schedule_bot.enums.Action;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationCommand implements Command {
    private final NotificationKeyboard notificationKeyboard;
    @Override
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received " + this.getClass().getSimpleName() + " from userId: {}", userId);
        return SendMessage.builder()
                .chatId(chatId)
                .text("\uD83D\uDD14 Настройте свои уведомления \uD83D\uDD14")
                .replyMarkup(notificationKeyboard.createInlineKeyboard(Action.NOTIFICATION))
                .build();
    }

}
