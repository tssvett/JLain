package dev.tssvett.schedule_bot.bot.annotation.postprocessor;

import dev.tssvett.schedule_bot.backend.exception.annotation.PostBeanProcessorException;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.command.impl.general.PictureBotCommand;
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
class RegistrationRequiredPostProcessorTest {

    @Mock
    StudentService studentService;

    @InjectMocks
    RegistrationRequiredPostProcessor registrationRequiredPostProcessor;

    @Mock
    PictureBotCommand pictureBotCommand;

    @Test
    void testPostProcessBeforeInitializationWithRegistrationRequired() {
        // Arrange
        String beanName = "testBean";

        // Act
        Object result = registrationRequiredPostProcessor.postProcessBeforeInitialization(pictureBotCommand, beanName);

        // Assert
        assertTrue(Proxy.isProxyClass(result.getClass()));
    }

    @Test
    void testPostProcessBeforeInitializationWithoutRegistrationRequired() {
        // Arrange
        Object bean = new Object();
        String beanName = "testBean";

        // Act
        Object result = registrationRequiredPostProcessor.postProcessBeforeInitialization(bean, beanName);

        // Assert
        assertEquals(bean, result);
    }

    @Test
    void testHandleExecuteMethodAsRegistered() throws Throwable {
        // Arrange
        when(studentService.isRegistered(1L)).thenReturn(true);
        when(pictureBotCommand.execute(1L, 1L)).thenReturn(SendMessage.builder().chatId(1L).text("Success").build());

        Method method = pictureBotCommand.getClass().getMethod("execute", Long.class, Long.class);

        // Act
        SendMessage result = (SendMessage) registrationRequiredPostProcessor.handleExecuteMethod(pictureBotCommand, method, new Object[]{1L, 1L});

        // Assert
        assertEquals("Success", result.getText());
    }

    @Test
    void testHandleExecuteMethodAsNotRegistered() throws Throwable {
        // Arrange
        when(studentService.isRegistered(1L)).thenReturn(false);

        Method method = pictureBotCommand.getClass().getMethod("execute", Long.class, Long.class);

        // Act
        SendMessage result = (SendMessage) registrationRequiredPostProcessor.handleExecuteMethod(pictureBotCommand, method, new Object[]{1L, 2L});

        // Assert
        assertEquals("У вас нет доступа к этой команде. Сначала пройдите регистрацию.", result.getText());
    }

    @Test
    void testHandleExecuteMethodThrowsException() throws Exception {
        // Arrange
        when(studentService.isRegistered(1L)).thenReturn(true);

        Method method = pictureBotCommand.getClass().getMethod("execute", Long.class, Long.class);

        when(method.invoke(pictureBotCommand, 1L, 1L)).thenThrow(new RuntimeException("Error"));

        // Act & Assert
        assertThrows(PostBeanProcessorException.class, () -> {
            registrationRequiredPostProcessor.handleExecuteMethod(pictureBotCommand, method, new Object[]{1L, 1L});
        });
    }
}
