package dev.tssvett.schedule_bot.scheduling.messages;

import dev.tssvett.schedule_bot.backend.client.TelegramClientService;
import dev.tssvett.schedule_bot.backend.service.MessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "scheduling.messages-sender.enabled", havingValue = "true")
public class MessagesSenderScheduler {
    private final MessageService messageService;
    private final TelegramClientService telegramClientService;

    @Scheduled(cron = "${scheduling.messages-sender.cron}")
    public void sendMessages() {
        log.info("Staring sending messages to users");
        List<SendMessage> allMessages = messageService.findAllMessages();
        telegramClientService.sendMessageList(allMessages);
        messageService.removeAllMessages();
        log.info("Sending messages to users finished");
    }
}
