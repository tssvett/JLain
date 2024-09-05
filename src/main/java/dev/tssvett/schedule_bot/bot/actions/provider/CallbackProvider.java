package dev.tssvett.schedule_bot.bot.actions.provider;

import dev.tssvett.schedule_bot.bot.actions.handler.CommandHandler;
import dev.tssvett.schedule_bot.bot.actions.handler.KeyboardHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackProvider {
    private final CommandHandler commandsHandler;
    private final KeyboardHandler keyboardHandler;

    public SendMessage handleMessage(Update update) {
        return messageIsText(update) ? commandsHandler.handleCommands(update) : keyboardHandler.handleKeyboardAction(update);
    }

    private boolean messageIsText(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }
}
