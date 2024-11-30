package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.persistence.model.tables.records.MessageRecord;
import dev.tssvett.schedule_bot.persistence.repository.MessageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<SendMessage> findAllMessages() {
        return messageRepository.findAll()
                .stream()
                .map(Mapper::toSendMessage)
                .toList();
    }

    public void saveMessagesToDatabase(List<Long> studentIds, String message) {
        List<MessageRecord> sendMessages = studentIds
                .stream()
                .map(id -> new MessageRecord(null, id, message))
                .toList();

        messageRepository.saveAll(sendMessages);
    }
}
