package dev.tssvett.schedule_bot.bot.actions.handler;

import dev.tssvett.schedule_bot.bot.enums.Command;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class MenuHandler {
    private final List<BotCommand> botCommandList = initCommands();

    private List<BotCommand> initCommands() {
        List<BotCommand> botCommandList = new ArrayList<>();
        for (Command command : Command.values()) {
            botCommandList.add(new BotCommand(command.getCommandName(), command.getDescription()));
        }
        return botCommandList;
    }
}
