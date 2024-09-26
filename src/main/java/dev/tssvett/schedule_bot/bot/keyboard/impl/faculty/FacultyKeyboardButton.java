package dev.tssvett.schedule_bot.bot.keyboard.impl.faculty;

import dev.tssvett.schedule_bot.persistence.entity.Student;
import dev.tssvett.schedule_bot.persistence.entity.Faculty;
import dev.tssvett.schedule_bot.backend.exception.registration.NotValidRegistrationStateException;
import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.actions.keyboard.impl.details.CallbackDetails;
import dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants;
import dev.tssvett.schedule_bot.bot.keyboard.KeyboardButton;
import dev.tssvett.schedule_bot.bot.keyboard.impl.course.CourseKeyboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.tssvett.schedule_bot.bot.enums.Action.COURSE_CHOOSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacultyKeyboardButton implements KeyboardButton {
    private final FacultyService facultyService;
    private final StudentService studentService;
    private final CourseKeyboard courseKeyboard;

    @Override
    public SendMessage click(Update update) {
        Long facultyId = Long.parseLong(CallbackDetails.fromString(update.getCallbackQuery().getData()).getCallbackInformation());
        Faculty faculty = facultyService.findFacultyById(facultyId);
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();

        return createFacultyChooseMessage(userId, chatId, faculty);
    }

    public SendMessage createFacultyChooseMessage(Long userId, Long chatId, Faculty faculty) {
        try {
            Student userWithChosenFaculty = studentService.updateStudentFaculty(userId, faculty);
            log.info("User {} successfully choose faculty {}", userWithChosenFaculty.getUserId(), faculty.getName());

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MessageConstants.REGISTER_CHOOSE_COURSE_MESSAGE)
                    .replyMarkup(courseKeyboard.createInlineKeyboard(COURSE_CHOOSE, userId))
                    .build();
        } catch (NotValidRegistrationStateException e) {
            log.warn("User {} try to choose faculty {} but it's already chosen", userId, faculty.getName());

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MessageConstants.FACULTY_CLICK_WITH_ERROR_STATE)
                    .build();
        }
    }
}
