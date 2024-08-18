package dev.tssvett.schedule_bot.annotation.aspect;

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
public class DirectMessageRequiredAspect {

    @Around(value = "@annotation(dev.tssvett.schedule_bot.annotation.DirectMessageRequired) && args(userId, chatId)")
    public Object checkDirectMessage(ProceedingJoinPoint joinPoint, Long userId, Long chatId) throws Throwable {
        log.info("Check isDirectMessage for userId: {} and chatId: {}", userId, chatId);
        return isDirectMessage(userId, chatId) ? joinPoint.proceed() : createNotDirectMessage(chatId);
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
}
