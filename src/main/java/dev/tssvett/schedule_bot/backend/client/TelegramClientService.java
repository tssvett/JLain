package dev.tssvett.schedule_bot.backend.client;

import dev.tssvett.schedule_bot.bot.TelegramBot;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramClientService {
    private final TelegramBot telegramBot;

    public void sendMessage(BotApiMethod<?> sendMessage) {
        telegramBot.sendMessage(sendMessage);
    }

    public void sendMessageList(List<SendMessage> sendMessages) {
        sendMessages.forEach(this::sendMessage);
    }
}
