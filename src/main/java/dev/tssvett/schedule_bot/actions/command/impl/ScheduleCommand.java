package dev.tssvett.schedule_bot.actions.command.impl;

import dev.tssvett.schedule_bot.actions.command.Command;
import dev.tssvett.schedule_bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.constants.MessageConstants;
import dev.tssvett.schedule_bot.schedule.parser.SchoolWeekParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleCommand implements Command {
    private final SchoolWeekParser schoolWeekParser;

    @Override
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId) {
        log.info("Received " + this.getClass().getSimpleName() + " from userId: {}", userId);
        return SendMessage.builder()
                .chatId(chatId)
                .text(schoolWeekParser.parse().toString())
                //.text(MessageConstants.SCHEDULE_COMMAND)
                .build();
    }
}
