package dev.tssvett.schedule_bot.bot.keyboard.impl.faculty;

import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.bot.enums.keyboard.Action;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
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
class FacultyKeyboardTest {
    @Mock
    FacultyService facultyService;

    @InjectMocks
    FacultyKeyboard facultyKeyboard;

    @Test
    void createInlineKeyboard() {
        //Arrange
        Action action = Action.FACULTY_CHOOSE;
        Long userId = 1L;
        InlineKeyboardMarkup expectedKeyboard = getInlineKeyboardMarkup();
        when(facultyService.findAllFaculties()).thenReturn(List.of(new FacultyRecord(1L, "faculty")));

        //Act
        InlineKeyboardMarkup inlineKeyboard = facultyKeyboard.createInlineKeyboard(action, userId);

        //Assert
        assertEquals(expectedKeyboard.getKeyboard().size(), inlineKeyboard.getKeyboard().size());
        assertEquals(expectedKeyboard.getKeyboard().get(0).get(0).getText(),
                inlineKeyboard.getKeyboard().get(0).get(0).getText());
    }

    @NotNull
    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        InlineKeyboardButton keyboardButton1 = new InlineKeyboardButton("faculty");

        InlineKeyboardRow inlineKeyboardButtons1 = new InlineKeyboardRow(keyboardButton1);

        return new InlineKeyboardMarkup(List.of(inlineKeyboardButtons1));
    }
}