package dev.tssvett.schedule_bot.bot.keyboard.impl.group;

import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupKeyboard extends Keyboard {
    private final GroupService groupService;
    private static final int GROUP_KEYS_IN_ROW = 3;

    @Transactional
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<EducationalGroupRecord> educationalGroups = groupService.getFilteredByCourseAndFacultyGroups(userId);
        List<InlineKeyboardRow> rows = createRows(educationalGroups, action);

        return new InlineKeyboardMarkup(rows);
    }

    private List<InlineKeyboardRow> createRows(List<EducationalGroupRecord> educationalGroups, Action action) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (int i = 0; i < educationalGroups.size(); i += GROUP_KEYS_IN_ROW) {
            InlineKeyboardRow keyboardButtonRow = createRow(i, educationalGroups, action);
            if (!keyboardButtonRow.isEmpty()) {
                rows.add(keyboardButtonRow);
            }
        }

        return rows;
    }

    private InlineKeyboardRow createRow(int startIndex, List<EducationalGroupRecord> educationalGroups, Action action) {
        InlineKeyboardRow keyboardButtonRow = new InlineKeyboardRow();

        for (int j = 0; j < GROUP_KEYS_IN_ROW && (startIndex + j) < educationalGroups.size(); j++) {
            EducationalGroupRecord educationalGroup = educationalGroups.get(startIndex + j);
            keyboardButtonRow.add(createButton(educationalGroup.getName(), String.valueOf(educationalGroup.getGroupId()), action));
        }

        return keyboardButtonRow;
    }
}