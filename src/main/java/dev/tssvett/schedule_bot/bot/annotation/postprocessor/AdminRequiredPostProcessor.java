package dev.tssvett.schedule_bot.bot.annotation.postprocessor;

import dev.tssvett.schedule_bot.backend.exception.PostBeanProcessorException;
import dev.tssvett.schedule_bot.bot.annotation.AdminRequired;
import dev.tssvett.schedule_bot.bot.properties.AdminProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static dev.tssvett.schedule_bot.bot.formatter.message.MessageConstants.NOT_ADMIN_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminRequiredPostProcessor implements BeanPostProcessor {
    private final AdminProperties adminProperties;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(AdminRequired.class)) {
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
                (proxy, method, args) -> isExecuteMethod(method) ? handleExecuteMethod(bean, method, args) : method.invoke(bean, args));
    }

    private SendMessage handleExecuteMethod(Object bean, Method method, Object[] args) {
        Long userId = castToLong(args[0]);
        Long chatId = castToLong(args[1]);
        log.info("Check registration with postBeanProcessor for userId: {} and chatId: {}", userId, chatId);
        try {
            return isAdmin(userId) ? (SendMessage) method.invoke(bean, args) : sendNotAdminMessage(chatId);
        } catch (Exception e) {
            log.error("Error invoking method {}: {}", method.getName(), e.getMessage());
            throw new PostBeanProcessorException(e.getMessage());
        }
    }

    private Long castToLong(Object arg) {
        if (arg instanceof Long) {
            return (Long) arg;
        }
        throw new IllegalArgumentException("Expected Long argument");
    }

    private SendMessage sendNotAdminMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(NOT_ADMIN_MESSAGE)
                .build();
    }

    private boolean isExecuteMethod(Method method) {
        return "execute".equals(method.getName()) && method.getParameterCount() == 2;
    }

    private boolean isInterface(Object bean) {
        return bean.getClass().getInterfaces().length > 0;
    }

    private boolean isAdmin(Long userId) {
        return adminProperties.getId().equals(userId);
    }
}