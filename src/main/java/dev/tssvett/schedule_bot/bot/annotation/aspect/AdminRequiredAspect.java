package dev.tssvett.schedule_bot.annotation.aspect;

import dev.tssvett.schedule_bot.properties.AdminProperties;
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
public class AdminRequiredAspect {
    private final UserRepository userRepository;
    private final AdminProperties adminProperties;

    @Around(value = "@annotation(dev.tssvett.schedule_bot.annotation.AdminRequired) && args(userId, chatId)")
    public Object checkAdmin(ProceedingJoinPoint joinPoint, Long userId, Long chatId) {
        log.info("Check admin for userId: {}", userId);
        return userRepository.findById(userId)
                .map(user -> {
                    if (!user.getUserId().equals(adminProperties.getId())) {
                        log.info("isAdmin check for user {} failed.", userId);
                        return createNotAdminMessage(chatId);
                    }
                    try {
                        return joinPoint.proceed();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseGet(() -> createNotAdminMessage(chatId));
    }

    private SendMessage createNotAdminMessage(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Вы не администратор. не трогай.....")
                .build();
    }
}
