package dev.tssvett.schedule_bot.actions.keyboard.impl.faculty;

import dev.tssvett.schedule_bot.actions.keyboard.Keyboard;
import dev.tssvett.schedule_bot.entity.Faculty;
import dev.tssvett.schedule_bot.enums.Action;
import dev.tssvett.schedule_bot.repository.FacultyRepository;
import dev.tssvett.schedule_bot.service.FacultyService;
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
public class FacultyKeyboard extends Keyboard {
    private final FacultyService facultyService;
    private static final Integer FACULTY_KEYS_IN_ROW = 2;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<Faculty> faculties = facultyService.findAllFaculties();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < faculties.size(); i += FACULTY_KEYS_IN_ROW) {
            List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();
            for (int j = 0; j < FACULTY_KEYS_IN_ROW && (i + j) < faculties.size(); j++) {
                Faculty faculty = faculties.get(i + j);
                String callbackInformation = String.valueOf(faculty.getFacultyId());
                keyboardButtonRow.add(createButton(faculty.getName(), callbackInformation, action));
            }
            if (!keyboardButtonRow.isEmpty()) {
                rows.add(keyboardButtonRow);
            }
        }
        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
