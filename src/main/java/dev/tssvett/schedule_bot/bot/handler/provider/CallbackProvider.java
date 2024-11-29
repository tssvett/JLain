package dev.tssvett.schedule_bot.bot.handler.provider;

import dev.tssvett.schedule_bot.bot.handler.CommandHandler;
import dev.tssvett.schedule_bot.bot.handler.KeyboardHandler;
import dev.tssvett.schedule_bot.bot.utils.UpdateUtils;
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
        return UpdateUtils.messageIsText(update) ?
                commandsHandler.handleCommands(update) :
                keyboardHandler.handleKeyboardAction(update);
    }
}
