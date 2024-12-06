package dev.tssvett.schedule_bot.bot.keyboard.impl.group;

import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@ExtendWith(MockitoExtension.class)
class GroupKeyboardTest {
    @Mock
    GroupService groupService;

    @InjectMocks
    GroupKeyboard groupKeyboard;

    @Test
    void createInlineKeyboard() {
        //Arrange
        Action action = Action.GROUP_CHOOSE;
        Long userId = 1L;
        InlineKeyboardMarkup expectedKeyboard = getInlineKeyboardMarkup();
        when(groupService.getFilteredByCourseAndFacultyGroups(userId)).thenReturn(List.of(new EducationalGroupRecord(1L, "group",
                3L, 3L)));

        //Act
        InlineKeyboardMarkup inlineKeyboard = groupKeyboard.createInlineKeyboard(action, userId);

        //Assert
        assertEquals(expectedKeyboard.getKeyboard().size(), inlineKeyboard.getKeyboard().size());
        assertEquals(expectedKeyboard.getKeyboard().get(0).get(0).getText(),
                inlineKeyboard.getKeyboard().get(0).get(0).getText());
    }

    @NotNull
    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton("group");

        InlineKeyboardRow inlineKeyboardButtons1 = new InlineKeyboardRow(keyboardButton1);

        return new InlineKeyboardMarkup(List.of(inlineKeyboardButtons1));
    }
}