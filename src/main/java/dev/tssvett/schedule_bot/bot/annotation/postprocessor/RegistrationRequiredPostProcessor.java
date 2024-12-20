package dev.tssvett.schedule_bot.bot.annotation.postprocessor;

import dev.tssvett.schedule_bot.backend.exception.annotation.PostBeanProcessorException;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationRequiredPostProcessor implements BeanPostProcessor {
    private final StudentService studentService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(RegistrationRequired.class)) {
                return createProxy(bean);
            }
        }

        return bean;
    }

    private Object createProxy(Object bean) {
        Class<?> beanClass = bean.getClass();

        if (!isInterface(bean)) {
            return bean;
        }

        return Proxy.newProxyInstance(
                beanClass.getClassLoader(),
                beanClass.getInterfaces(),
                (proxy, method, args) -> isExecuteMethod(method)
                        ? handleExecuteMethod(bean, method, args)
                        : method.invoke(bean, args)
        );
    }

    SendMessage handleExecuteMethod(Object bean, Method method, Object[] args) throws Throwable {
        Long userId = castToLong(args[0]);
        Long chatId = castToLong(args[1]);
        log.info("Check registration with postBeanProcessor for userId: {} and chatId: {}", userId, chatId);
        try {
            return studentService.isRegistered(userId)
                    ? (SendMessage) method.invoke(bean, args)
                    : sendNeedToRegisterMessage(chatId);
        } catch (Exception e) {
            log.error("Error invoking method {}: {}", method.getName(), e.getMessage());
            throw new PostBeanProcessorException(e.getMessage());
        }
    }

    private SendMessage sendNeedToRegisterMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("У вас нет доступа к этой команде. Сначала пройдите регистрацию.")
                .build();
    }

    private boolean isExecuteMethod(Method method) {
        return "execute".equals(method.getName()) && method.getParameterCount() == 2;
    }

    private boolean isInterface(Object bean) {
        return bean.getClass().getInterfaces().length > 0;
    }

    private Long castToLong(Object arg) {
        if (arg instanceof Long) {
            return (Long) arg;
        }
        throw new IllegalArgumentException("Expected Long argument");
    }
}