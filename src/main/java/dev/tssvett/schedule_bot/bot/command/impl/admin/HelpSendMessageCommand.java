package dev.tssvett.schedule_bot.bot.command.impl.admin;

import dev.tssvett.schedule_bot.bot.annotation.AdminRequired;
import dev.tssvett.schedule_bot.bot.command.BotCommand;
import dev.tssvett.schedule_bot.bot.utils.message.MessageCreateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class HelpSendMessageCommand implements BotCommand {

    @Override
    @AdminRequired
    public SendMessage execute(Long userId, Long chatId, String argument) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageCreateUtils.createHelpSendMessageMessage())
                .build();
    }
}
