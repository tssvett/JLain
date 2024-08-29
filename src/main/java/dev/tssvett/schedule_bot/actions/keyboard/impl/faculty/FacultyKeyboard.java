package dev.tssvett.schedule_bot.actions.keyboard.impl;

import dev.tssvett.schedule_bot.actions.keyboard.Keyboard;
import dev.tssvett.schedule_bot.actions.keyboard.callback.details.CallbackDetails;
import dev.tssvett.schedule_bot.enums.Action;
import dev.tssvett.schedule_bot.entity.Faculty;
import dev.tssvett.schedule_bot.schedule.parser.FacultyParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FacultyKeyboard implements Keyboard {
    //тут должна быть бд вместо парсера
    //а парсер вынесется в отдельный сервис с @Scheduled
    private final FacultyParser facultyParser;
    private static final Integer FACULTY_KEYS_IN_ROW = 2;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action) {
        List<Faculty> faculties = facultyParser.parse();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < faculties.size(); i += FACULTY_KEYS_IN_ROW) {
            List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();
            for (int j = 0; j < FACULTY_KEYS_IN_ROW && (i + j) < faculties.size(); j++) {
                Faculty faculty = faculties.get(i + j);
                InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
                keyboardButton.setText(faculty.getName());
                //Настройка коллбейка для кнопки
                //Решено использовать название команды, которая вызвала клавиатуру
                //И айди факультета
                CallbackDetails callbackDetails = CallbackDetails.builder()
                        .action(action)
                        //УСТАНОВКА ID факультета как текста коллбека
                        .callbackText(String.valueOf(faculty.getFacultyId()))
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
