package dev.tssvett.schedule_bot.annotation.aspect;

import dev.tssvett.schedule_bot.enums.RegistrationState;
import dev.tssvett.schedule_bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RegistrationRequiredAspect {
    private final UserRepository userRepository;

    @Around(value = "@annotation(dev.tssvett.schedule_bot.annotation.RegistrationRequired) && args(userId, chatId)")
    public Object checkRegistration(ProceedingJoinPoint joinPoint, Long userId, Long chatId) {
        log.info("Check registration for userId: {} and chatId: {}", userId, chatId);
        return userRepository.findById(userId)
                .map(user -> {
                    if (!user.getRegistrationState().equals(RegistrationState.SUCCESSFUL_REGISTRATION)) {
                        log.info("isRegistered check for user {} failed.", userId);
                        return createRegistrationMessage(chatId);
                    }
                    try {
                        return joinPoint.proceed();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseGet(() -> createRegistrationMessage(chatId));
    }

    private SendMessage createRegistrationMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("У вас нет доступа к этой команде. Сначала пройдите регистрацию.")
                .build();
    }
}
