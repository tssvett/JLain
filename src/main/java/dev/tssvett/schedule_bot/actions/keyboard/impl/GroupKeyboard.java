package dev.tssvett.schedule_bot.actions.keyboard.impl;

import dev.tssvett.schedule_bot.actions.keyboard.Keyboard;
import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.enums.Action;
import dev.tssvett.schedule_bot.schedule.group.Group;
import dev.tssvett.schedule_bot.schedule.parser.GroupParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupKeyboard implements Keyboard {


    private final GroupParser groupParser;
    private static final Integer GROUP_KEYS_IN_ROW = 3;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action) {
        List<Group> groups = groupParser.parse();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < groups.size(); i += GROUP_KEYS_IN_ROW) {
            List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();
            for (int j = 0; j < GROUP_KEYS_IN_ROW && (i + j) < groups.size(); j++) {
                Group group = groups.get(i + j);
                InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
                keyboardButton.setText(group.getName());
                //Настройка коллбейка для кнопки
                //Решено использовать название команды, которая вызвала клавиатуру
                //И айди факультета
                CallbackDetails callbackDetails = CallbackDetails.builder()
                        .action(action)
                        //УСТАНОВКА ID факультета как текста коллбека
                        .callbackText(group.getId())
                        .build();
                keyboardButton.setCallbackData(callbackDetails.toString());
                keyboardButtonRow.add(keyboardButton);
            }
            if (!keyboardButtonRow.isEmpty()) {
                rows.add(keyboardButtonRow);
            }
        }
        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
