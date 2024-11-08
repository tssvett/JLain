package dev.tssvett.schedule_bot.bot.actions.handler;

import dev.tssvett.schedule_bot.bot.enums.Command;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.stream.Stream;

@Component
@Getter
public class MenuHandler {
    private final List<BotCommand> botCommandList;

    public MenuHandler() {
        this.botCommandList = createBotCommandList();
    }

    private List<BotCommand> createBotCommandList() {
        return Stream.of(Command.values())
                .map(this::createBotCommand)
                .toList();
    }

    private BotCommand createBotCommand(Command command) {
        return new BotCommand(command.getCommandName(), command.getDescription());
    }
}
