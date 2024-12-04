package dev.tssvett.schedule_bot.bot.annotation.postprocessor;

import dev.tssvett.schedule_bot.backend.exception.annotation.PostBeanProcessorException;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.command.impl.admin.ShowRegisteredStudentsCommand;
import static dev.tssvett.schedule_bot.bot.utils.message.MessageTextConstantsUtils.NOT_ADMIN_MESSAGE;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@ExtendWith(MockitoExtension.class)
class AdminRequiredPostProcessorTest {

    @Mock
    StudentService studentService;

    @InjectMocks
    AdminRequiredPostProcessor adminRequiredPostProcessor;

    @Test
    void testPostProcessBeforeInitialization() {
        // Arrange
        ShowRegisteredStudentsCommand bean = new ShowRegisteredStudentsCommand(studentService);
        String beanName = "testBean";

        /// Act
        Object result = adminRequiredPostProcessor.postProcessBeforeInitialization(bean, beanName);

        // Assert
        assertTrue(Proxy.isProxyClass(result.getClass()));
        assertEquals(bean.hashCode(), result.hashCode());
    }

    @Test
    void testPostProcessBeforeInitializationWithoutAdminRequired() {
        // Arrange
        Object bean = new Object();
        String beanName = "testBean";

        // Act
        Object result = adminRequiredPostProcessor.postProcessBeforeInitialization(bean, beanName);

        // Assert
        assertEquals(bean, result);
    }

    @Test
    void testHandleExecuteMethodAsAdmin() throws Exception {
        // Arrange
        ShowRegisteredStudentsCommand command = new ShowRegisteredStudentsCommand(studentService);
        when(studentService.isAdmin(1L)).thenReturn(true);

        Method method = command.getClass().getMethod("execute", Long.class, Long.class);

        // Act
        SendMessage result = (SendMessage) adminRequiredPostProcessor.handleExecuteMethod(command, method, new Object[]{1L, 2L});

        // Assert
        assertEquals("\uD83C\uDF40 Количество зарегестрированных пользователей: 0\n", result.getText());
    }

    @Test
    void testHandleExecuteMethodAsNonAdmin() throws Exception {
        // Arrange
        ShowRegisteredStudentsCommand command = new ShowRegisteredStudentsCommand(studentService);
        when(studentService.isAdmin(1L)).thenReturn(false);

        Method method = command.getClass().getMethod("execute", Long.class, Long.class);

        // Act
        SendMessage result = (SendMessage) adminRequiredPostProcessor.handleExecuteMethod(command, method, new Object[]{1L, 2L});

        // Assert
        assertEquals(NOT_ADMIN_MESSAGE, result.getText());
    }

    @Test
    void testHandleExecuteMethodThrowsException() throws Exception {
        // Arrange
        ShowRegisteredStudentsCommand command = new ShowRegisteredStudentsCommand(studentService);
        when(studentService.isAdmin(1L)).thenReturn(true);

        Method method = command.getClass().getMethod("execute", Long.class, Long.class);

        when(method.invoke(command, 1L, 2L)).thenThrow(new RuntimeException("Error"));

        // Act & Assert
        assertThrows(PostBeanProcessorException.class, () -> {
            adminRequiredPostProcessor.handleExecuteMethod(command, method, new Object[]{1L, 2L});
        });
    }
}