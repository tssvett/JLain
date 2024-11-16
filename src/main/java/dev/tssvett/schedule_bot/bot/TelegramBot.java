package dev.tssvett.schedule_bot.bot;

import dev.tssvett.schedule_bot.bot.actions.provider.CallbackProvider;
import dev.tssvett.schedule_bot.bot.properties.BotProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotProperties botProperties;
    private final CallbackProvider callbackProvider;

    @Override
    public String getBotUsername() {
        return botProperties.name();
    }

    @Override
    public String getBotToken() {
        return botProperties.token();
    }

    @Override
    public void onUpdateReceived(Update update) {
        executeBotMethod(callbackProvider.handleMessage(update));
    }

    public void executeBotMethod(BotApiMethod<?> botApiMethod) {
        try {
            this.execute(botApiMethod);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
