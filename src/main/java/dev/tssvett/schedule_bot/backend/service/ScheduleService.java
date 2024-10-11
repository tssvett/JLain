package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.exception.database.StudentNotExistsException;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.bot.utils.DateUtils;
import dev.tssvett.schedule_bot.parsing.SchoolWeekParser;
import dev.tssvett.schedule_bot.persistence.entity.Lesson;
import dev.tssvett.schedule_bot.persistence.entity.Student;
import dev.tssvett.schedule_bot.persistence.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final StudentRepository studentRepository;
    private final SchoolWeekParser schoolWeekParser;


    public List<LessonInfoDto> getWeekSchedule(Long userId) {
        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + userId));
        List<Lesson> lessonsInWeek = schoolWeekParser.parse(student.getGroup().getGroupId(), DateUtils.calculateWeekNumber());

        return lessonsInWeek
                .stream()
                .map(Mapper::toLessonInfoDto)
                .toList();
    }
}
