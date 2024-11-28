package dev.tssvett.schedule_bot.bot.command.impl.admin;

import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.AdminRequired;
import dev.tssvett.schedule_bot.bot.utils.message.MessageCreateUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShowRegisteredStudentsCommand implements BotCommand {
    private final StudentService studentService;

    @Override
    @AdminRequired
    public SendMessage execute(Long userId, Long chatId) {
        List<StudentInfoDto> studentsInfoList = studentService.findAll()
                .stream()
                .map(Mapper::toStudentInfoDto)
                .toList();

        return SendMessage.builder()
                .chatId(chatId)
                .text(MessageCreateUtils.createRegisteredStudentsMessage(studentsInfoList))
                .build();
    }
}
