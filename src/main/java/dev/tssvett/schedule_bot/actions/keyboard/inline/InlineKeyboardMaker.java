package dev.tssvett.schedule_bot.actions.keyboard.inline;

import dev.tssvett.schedule_bot.actions.command.schedule.faculty.Faculty;
import dev.tssvett.schedule_bot.actions.command.schedule.group.Group;
import dev.tssvett.schedule_bot.actions.command.schedule.parser.FacultyParser;
import dev.tssvett.schedule_bot.actions.command.schedule.parser.GroupParser;
import dev.tssvett.schedule_bot.actions.keyboard.callback.CallbackDetails;
import dev.tssvett.schedule_bot.enums.Action;
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
public class InlineKeyboardMaker {

    private final FacultyParser facultyParser;
    private final GroupParser groupParser;
    private static final Integer FACULTY_KEYS_IN_ROW = 2;
    private static final Integer GROUP_KEYS_IN_ROW = 3;
    private static final Integer COURSE_KEYS_IN_ROW = 3;

    public InlineKeyboardMarkup createInlineKeyboard() {
        List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText("Тестовая кнопка");
        keyboardButton.setCallbackData("Тестовая кнопка тыкнута успешно");

        InlineKeyboardButton keyboardButton2 = new InlineKeyboardButton();
        keyboardButton2.setText("Тестовая кнопка2");
        keyboardButton2.setCallbackData("Тестовая кнопка тыкнута успешно2");

        keyboardButtonRow.add(keyboardButton);
        keyboardButtonRow.add(keyboardButton2);

        rows.add(keyboardButtonRow);
        inlineKeyboardMarkup.setKeyboard(rows);
        log.info("Inline keyboard created" + inlineKeyboardMarkup);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup createFacultyKeyboard(Action action) {
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
                        .callbackText(faculty.getId())
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

    public InlineKeyboardMarkup createCourseKeyboard(Action action) {
        List<Integer> courses = List.of(1, 2 ,3, 4 , 5);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int i = 0; i < courses.size(); i += COURSE_KEYS_IN_ROW) {
            List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();

            for (int j = 0; j < COURSE_KEYS_IN_ROW && (i + j) < courses.size(); j++) {
                Integer course = courses.get(i + j);
                InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
                keyboardButton.setText(course.toString());

                //Настройка коллбейка для кнопки
                //Решено использовать название команды, которая вызвала клавиатуру
                //И айди факультета
                CallbackDetails callbackDetails = CallbackDetails.builder()
                        .action(action)
                        //УСТАНОВКА НОМЕРА КУРСА КАК ТЕКСТ КОЛБЕКА
                        .callbackText(course.toString())
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

    public InlineKeyboardMarkup createGroupKeyboard(Action action) {
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
