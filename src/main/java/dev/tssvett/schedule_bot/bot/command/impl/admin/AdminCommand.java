package dev.tssvett.schedule_bot.bot.command.impl.admin;

import dev.tssvett.schedule_bot.bot.annotation.AdminRequired;
import dev.tssvett.schedule_bot.bot.command.BotCommand;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.impl.admin.AdminKeyboard;
import dev.tssvett.schedule_bot.bot.utils.message.MessageCreateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class AdminCommand implements BotCommand {
    private final AdminKeyboard adminKeyboard;

    @Override
    @AdminRequired
    public SendMessage execute(Long userId, Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageCreateUtils.createAdminMessage())
                .replyMarkup(adminKeyboard.createInlineKeyboard(Action.ADMIN_COMMAND_SELECTION, userId))
                .build();
    }
}
