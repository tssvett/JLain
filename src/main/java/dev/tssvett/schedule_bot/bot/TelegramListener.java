package dev.tssvett.schedule_bot.bot;

import dev.tssvett.schedule_bot.backend.client.TelegramClientService;
import dev.tssvett.schedule_bot.bot.properties.BotProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramListener implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final BotProperties botProperties;
    private final TelegramClientService telegramClientService;

    @Override
    public String getBotToken() {
        return botProperties.token();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        log.debug("Received update: {}", update);
        telegramClientService.handleUpdate(update);
    }
}
