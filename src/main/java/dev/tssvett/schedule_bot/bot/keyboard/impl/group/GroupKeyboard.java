package dev.tssvett.schedule_bot.bot.keyboard.impl.group;

import dev.tssvett.schedule_bot.persistence.entity.Group;
import dev.tssvett.schedule_bot.persistence.entity.Student;
import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.enums.Action;
import dev.tssvett.schedule_bot.bot.keyboard.Keyboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupKeyboard extends Keyboard {
    private final GroupService groupService;
    private final StudentService studentService;
    private static final Integer GROUP_KEYS_IN_ROW = 3;

    @Transactional
    public InlineKeyboardMarkup createInlineKeyboard(Action action, Long userId) {
        Student user = studentService.findStudentById(userId);
        Long courseNumber = user.getCourse();
        List<Group> groups = groupService.findAllGroups();

        //Фильтруем группы по курсу и факультету
        groups = groups.stream()
                .filter(group -> Objects.equals(group.getCourse(), courseNumber))
                .filter(group -> group.getFaculty().getName().equals(user.getFaculty().getName()))
                .toList();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < groups.size(); i += GROUP_KEYS_IN_ROW) {
            List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();
            for (int j = 0; j < GROUP_KEYS_IN_ROW && (i + j) < groups.size(); j++) {
                Group group = groups.get(i + j);
                String callbackInformation = String.valueOf(group.getGroupId());
                keyboardButtonRow.add(createButton(group.getName(), callbackInformation, action));
            }
            if (!keyboardButtonRow.isEmpty()) {
                rows.add(keyboardButtonRow);
            }
        }
        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
