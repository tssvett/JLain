package dev.tssvett.schedule_bot.bot.keyboard.impl.faculty;


import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
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
    private static final int FACULTY_KEYS_IN_ROW = 2;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<FacultyRecord> faculties = facultyService.findAllFaculties();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int i = 0; i < faculties.size(); i += FACULTY_KEYS_IN_ROW) {
            rows.add(createRow(i, faculties, action));
        }

        return new InlineKeyboardMarkup(rows);
    }

    private List<InlineKeyboardButton> createRow(int startIndex, List<FacultyRecord> faculties, Action action) {
        List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();

        for (int j = 0; j < FACULTY_KEYS_IN_ROW && (startIndex + j) < faculties.size(); j++) {
            FacultyRecord faculty = faculties.get(startIndex + j);
            keyboardButtonRow.add(createButton(faculty.getName(), String.valueOf(faculty.getFacultyId()), action));
        }

        return keyboardButtonRow;
    }
}