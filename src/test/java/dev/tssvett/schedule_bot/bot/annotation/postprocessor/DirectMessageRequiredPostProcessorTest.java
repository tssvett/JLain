package dev.tssvett.schedule_bot.bot.annotation.postprocessor;

import dev.tssvett.schedule_bot.bot.command.impl.settings.DifferenceScheduleNotificationSettingsCommand;
import java.lang.reflect.InvocationTargetException;
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
class DirectMessageRequiredPostProcessorTest {

    @Mock
    DifferenceScheduleNotificationSettingsCommand command;

    @InjectMocks
    DirectMessageRequiredPostProcessor directMessageRequiredPostProcessor;

    @Test
    void testPostProcessBeforeInitializationWithDirectMessageRequired() {
        // Arrange
        String beanName = "testBean";

        // Act
        Object result = directMessageRequiredPostProcessor.postProcessBeforeInitialization(command, beanName);

        // Assert
        // Проверяем, что возвращаемый объект является прокси
        assertTrue(Proxy.isProxyClass(result.getClass()));
    }

    @Test
    void testPostProcessBeforeInitializationWithoutDirectMessageRequired() {
        // Arrange
        Object bean = new Object();
        String beanName = "testBean";

        // Act
        Object result = directMessageRequiredPostProcessor.postProcessBeforeInitialization(bean, beanName);

        // Assert
        assertEquals(bean, result);
    }

    @Test
    void testHandleExecuteMethodAsDirectMessage() throws Throwable {
        // Arrange
        when(command.execute(1L, 1L)).thenReturn(SendMessage.builder().chatId(1L).text("Success").build());

        Method method = command.getClass().getMethod("execute", Long.class, Long.class);

        // Act
        SendMessage result = (SendMessage) directMessageRequiredPostProcessor.handleExecuteMethod(command, method, new Object[]{1L, 1L});

        // Assert
        assertEquals("Success", result.getText());
    }

    @Test
    void testHandleExecuteMethodAsNotDirectMessage() throws Throwable {
        // Arrange
        Method method = command.getClass().getMethod("execute", Long.class, Long.class);

        // Act
        SendMessage result = (SendMessage) directMessageRequiredPostProcessor.handleExecuteMethod(command, method, new Object[]{1L, 2L});

        // Assert
        assertEquals("Эта команда должна вызываться внутри @JLainbot бота. Перейдите и используйте там.", result.getText());
    }

    @Test
    void testHandleExecuteMethodThrowsException() throws Exception {
        // Arrange
        Method method = command.getClass().getMethod("execute", Long.class, Long.class);

        when(method.invoke(command, 1L, 1L)).thenThrow(new RuntimeException("Error"));

        // Act & Assert
        assertThrows(InvocationTargetException.class, () -> {
            directMessageRequiredPostProcessor.handleExecuteMethod(command, method, new Object[]{1L, 1L});
        });
    }
}
