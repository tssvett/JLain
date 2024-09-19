package dev.tssvett.schedule_bot.bot.config;

import dev.tssvett.schedule_bot.bot.actions.handler.MenuHandler;
import dev.tssvett.schedule_bot.bot.TelegramBot;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BotInitializerConfig {
    private final TelegramBot telegramBot;
    private final MenuHandler menuHandler;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        log.info("Initializing bot");
        try {
            botsApi.registerBot(telegramBot);
            log.info("Bot initialized successfully");
        } catch (TelegramApiException e) {
            log.error("Error with bot initialization: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void initBotMenu() {
        log.info("Initializing bot menu...");
        try {
            telegramBot.execute(new SetMyCommands(menuHandler.getBotCommandList(), new BotCommandScopeDefault(), null));
            log.info("Bot menu initialized successfully");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
