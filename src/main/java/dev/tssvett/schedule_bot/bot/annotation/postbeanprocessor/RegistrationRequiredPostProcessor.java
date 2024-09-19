package dev.tssvett.schedule_bot.bot.annotation.postbeanprocessor;

import dev.tssvett.schedule_bot.backend.exception.PostBeanProcessorException;
import dev.tssvett.schedule_bot.backend.service.UserService;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
@Component
public class RegistrationRequiredPostProcessor implements BeanPostProcessor {
    private final UserService userService;
    private final TransactionTemplate transactionTemplate;

    public RegistrationRequiredPostProcessor(UserService userService, PlatformTransactionManager transactionManager) {
        this.userService = userService;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

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
        if (!isInterface(bean)) {
            return bean;
        }

        return Proxy.newProxyInstance(
                bean.getClass().getClassLoader(),
                bean.getClass().getInterfaces(),
                (proxy, method, args) -> isExecuteMethod(method) ? handleExecuteMethod(bean, method, args) : method.invoke(bean, args)
        );
    }

    private SendMessage handleExecuteMethod(Object bean, Method method, Object[] args) throws Throwable {
        Long userId = castToLong(args[0]);
        Long chatId = castToLong(args[1]);
        log.info("Check registration with postBeanProcessor for userId: {} and chatId: {}", userId, chatId);
        //Проксирование теряет транзакцию, используем transactionTemplate
        return transactionTemplate.execute(status -> {
            try {
                return userService.isRegistered(userId) ? (SendMessage) method.invoke(bean, args) : sendNeedToRegisterMessage(chatId);
            } catch (Exception e) {
                log.error("Error invoking method {}: {}", method.getName(), e.getMessage());
                status.setRollbackOnly();
                throw new PostBeanProcessorException(e.getMessage());
            }
        });
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