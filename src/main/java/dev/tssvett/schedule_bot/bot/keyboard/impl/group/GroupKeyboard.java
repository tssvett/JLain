package dev.tssvett.schedule_bot.bot.keyboard.impl.group;

import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import java.util.List;
import java.util.stream.IntStream;
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

        return new InlineKeyboardMarkup(createRows(educationalGroups, action));
    }

    private List<InlineKeyboardRow> createRows(List<EducationalGroupRecord> educationalGroups, Action action) {
        return IntStream.iterate(0, i -> i < educationalGroups.size(), i -> i + GROUP_KEYS_IN_ROW)
                .mapToObj(i -> createRow(i, educationalGroups, action))
                .filter(row -> !row.isEmpty())
                .toList();
    }

    private InlineKeyboardRow createRow(int startIndex, List<EducationalGroupRecord> educationalGroups, Action action) {
        return new InlineKeyboardRow(
                IntStream.iterate(0, i -> (i < GROUP_KEYS_IN_ROW && (startIndex + i) < educationalGroups.size()),
                                i -> i + 1)
                        .mapToObj(i -> {
                            EducationalGroupRecord educationalGroup = educationalGroups.get(startIndex + i);
                            return createButton(educationalGroup.getName(),
                                    String.valueOf(educationalGroup.getGroupId()), action);
                        })
                        .toList()
        );
    }
}