package dev.tssvett.schedule_bot.bot.actions.command.impl;

import dev.tssvett.schedule_bot.backend.dto.StudentInfoDto;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.backend.service.StudentService;
import dev.tssvett.schedule_bot.bot.actions.command.BotCommand;
import dev.tssvett.schedule_bot.bot.annotation.RegistrationRequired;
import dev.tssvett.schedule_bot.bot.enums.Role;
import dev.tssvett.schedule_bot.bot.utils.message.MessageCreateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class InfoBotCommand implements BotCommand {
    private final StudentService studentService;
    private final FacultyService facultyService;
    private final GroupService groupService;

    @Override
    @Transactional
    @RegistrationRequired
    public SendMessage execute(Long userId, Long chatId) {
        String messageText = getMessageText(userId);
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();
    }

    private String getMessageText(Long userId) {
        StudentInfoDto studentInfoDto = Mapper.toStudentInfoDto(studentService.getStudentInfoById(userId));
        String facultyName = facultyService.getFacultyById(studentInfoDto.facultyId()).getName();
        String groupName = groupService.getGroupById(studentInfoDto.groupId()).getName();
        boolean tomorrowScheduleNotificationStatus = studentService.isTomorrowScheduleNotificationEnabled(userId);
        boolean scheduleDifferenceNotificationStatus = studentService.isScheduleDifferenceNotificationEnabled(userId);
        Role role = studentInfoDto.role();

        return MessageCreateUtils.createInfoCommandMessageText(studentInfoDto, facultyName,
                groupName, tomorrowScheduleNotificationStatus, scheduleDifferenceNotificationStatus, role);
    }
}