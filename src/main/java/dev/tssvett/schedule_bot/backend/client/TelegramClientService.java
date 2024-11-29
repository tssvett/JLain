package dev.tssvett.schedule_bot.backend.client;

import dev.tssvett.schedule_bot.bot.handler.provider.CallbackProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramClientService {
    private final TelegramClient telegramClient;
    private final CallbackProvider callbackProvider;

    public void handleUpdate(Update update) {
        try {
            telegramClient.execute(callbackProvider.handleMessage(update));
        } catch (TelegramApiException e) {
            log.error("Error while sending message: {}", e.getMessage());
        }
    }

    public void sendMessageList(List<SendMessage> messages) {
        messages.forEach(message -> {
            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                log.error("Error while sending message: {}", e.getMessage());
            }
        });
    }
}
