package dev.tssvett.schedule_bot.bot.annotation.postbeanprocessor;

import dev.tssvett.schedule_bot.bot.annotation.DirectMessageRequired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
@Component
public class DirectMessageRequiredPostBeanProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(DirectMessageRequired.class)) {
                return createProxy(bean);
            }
        }
        return bean;
    }

    private Object createProxy(Object bean) {
        if (!isInterface(bean)) {
            return bean;
        }

        Class<?> beanClass = bean.getClass();
        return Proxy.newProxyInstance(
                beanClass.getClassLoader(),
                beanClass.getInterfaces(),
                (proxy, method, args) -> isExecuteMethod(method) ? handleExecuteMethod(bean, method, args) : method.invoke(bean, args));
    }

    private SendMessage handleExecuteMethod(Object bean, Method method, Object[] args) throws Throwable {
        Long userId = (Long) args[0];
        Long chatId = (Long) args[1];

        log.info("Check isDirectMessage with postBeanProcessor for userId: {} and chatId: {}", userId, chatId);
        return isDirectMessage(userId, chatId) ? (SendMessage) method.invoke(bean, args) : createNotDirectMessage(chatId);
    }

    private boolean isInterface(Object bean) {
        return bean.getClass().getInterfaces().length > 0; // Проверяем наличие интерфейсов у бина
    }

    private SendMessage createNotDirectMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Эта команда должна вызываться внутри @JLainbot бота. Перейдите и используйте там.")
                .build();
    }

    private boolean isDirectMessage(Long userId, Long chatId) {
        return userId.equals(chatId);
    }

    private boolean isExecuteMethod(Method method) {
        return "execute".equals(method.getName()) && method.getParameterCount() == 2;
    }
}