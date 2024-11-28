package dev.tssvett.schedule_bot.bot.menu;

import dev.tssvett.schedule_bot.bot.enums.command.StudentAllowedCommands;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Getter
@Component
public class BotCommandMenu {
    private final List<BotCommand> botCommandList;

    public BotCommandMenu() {
        this.botCommandList = createBotCommandList();
    }

    /**
     * Создает список ОБЩЕДОСТУПНЫХ команд бота
     * @return list of bot commands
     */
    private List<BotCommand> createBotCommandList() {
        return Stream.of(StudentAllowedCommands.values())
                .map(this::createBotCommand)
                .toList();
    }

    private BotCommand createBotCommand(StudentAllowedCommands studentAllowedCommands) {
        return new BotCommand(studentAllowedCommands.getCommandName(), studentAllowedCommands.getDescription());
    }
}
