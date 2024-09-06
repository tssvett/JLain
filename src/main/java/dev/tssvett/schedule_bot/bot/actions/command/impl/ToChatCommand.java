package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.bot.actions.command.Command;
import dev.tssvett.schedule_bot.bot.annotation.AdminRequired;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class ToChatCommand implements Command {

    @Override
    @RegistrationRequired
    @AdminRequired
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received {} from userId: {}", this.getClass().getSimpleName(), userId);
        return SendMessage.builder()
                .chatId("-4191336905")
                .text("che oni tam na c++ delayt?")
                .build();
    }
}
