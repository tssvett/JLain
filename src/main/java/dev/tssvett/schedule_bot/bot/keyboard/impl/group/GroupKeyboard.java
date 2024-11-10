package dev.tssvett.schedule_bot.bot.keyboard.impl.group;

import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.bot.enums.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupKeyboard extends Keyboard {
    private final GroupService groupService;
    private static final int GROUP_KEYS_IN_ROW = 3;

    @Transactional
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        List<EducationalGroupRecord> educationalGroups = groupService.getFilteredByCourseAndFacultyGroups(userId);
        List<List<InlineKeyboardButton>> rows = createRows(educationalGroups, action);

        return new InlineKeyboardMarkup(rows);
    }

    private List<List<InlineKeyboardButton>> createRows(List<EducationalGroupRecord> educationalGroups, Action action) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int i = 0; i < educationalGroups.size(); i += GROUP_KEYS_IN_ROW) {
            List<InlineKeyboardButton> keyboardButtonRow = createRow(i, educationalGroups, action);
            if (!keyboardButtonRow.isEmpty()) {
                rows.add(keyboardButtonRow);
            }
        }

        return rows;
    }

    private List<InlineKeyboardButton> createRow(int startIndex, List<EducationalGroupRecord> educationalGroups, Action action) {
        List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();

        for (int j = 0; j < GROUP_KEYS_IN_ROW && (startIndex + j) < educationalGroups.size(); j++) {
            EducationalGroupRecord educationalGroup = educationalGroups.get(startIndex + j);
            keyboardButtonRow.add(createButton(educationalGroup.getName(), String.valueOf(educationalGroup.getGroupId()), action));
        }

        return keyboardButtonRow;
    }
}