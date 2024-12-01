package dev.tssvett.schedule_bot.bot.keyboard.impl.faculty;


import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacultyKeyboard extends Keyboard {
    private final FacultyService facultyService;
    private static final int FACULTY_KEYS_IN_ROW = 2;

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<FacultyRecord> faculties = facultyService.findAllFaculties();

        return new InlineKeyboardMarkup(
                IntStream.iterate(0, i -> i < faculties.size(), i -> i + FACULTY_KEYS_IN_ROW)
                        .mapToObj(i -> createRow(i, faculties, action))
                        .toList()
        );
    }

    private InlineKeyboardRow createRow(int startIndex, List<FacultyRecord> faculties, Action action) {
        return new InlineKeyboardRow(
                IntStream.iterate(0, i -> (i < FACULTY_KEYS_IN_ROW && (startIndex + i) < faculties.size()), i -> i + 1)
                        .mapToObj(i -> {
                            FacultyRecord faculty = faculties.get(startIndex + i);
                            return createButton(faculty.getName(), String.valueOf(faculty.getFacultyId()), action);
                        })
                        .toList()
        );
    }
}