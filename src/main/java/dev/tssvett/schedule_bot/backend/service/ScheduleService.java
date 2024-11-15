package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.bot.utils.DateUtils;
import dev.tssvett.schedule_bot.parsing.SchoolWeekParser;
import dev.tssvett.schedule_bot.persistence.model.tables.records.LessonRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final StudentService studentService;
    private final SchoolWeekParser schoolWeekParser;
    private final DateUtils dateUtils;


    public List<LessonInfoDto> getWeekScheduleList(Long userId) {
        Long groupId = Mapper.toStudentInfoDto(studentService.getStudentInfoById(userId)).groupId();
        List<LessonRecord> lessonsInWeek = schoolWeekParser.parse(groupId, dateUtils.calculateCurrentUniversityEducationalWeek());

        return lessonsInWeek
                .stream()
                .map(Mapper::toLessonInfoDto)
                .toList();
    }

    public Map<String, List<LessonInfoDto>> getWeekScheduleMapByDate(Long userId) {
        Long groupId = Mapper.toStudentInfoDto(studentService.getStudentInfoById(userId)).groupId();
        List<LessonRecord> lessons = schoolWeekParser.parse(groupId, dateUtils.calculateCurrentUniversityEducationalWeek());

        return lessons.stream()
                .filter(this::isExist) // Исключаем окна(пары, которых нет) в расписании
                .map(Mapper::toLessonInfoDto)
                .collect(Collectors.groupingBy(LessonInfoDto::dateDay));
    }

    private boolean isExist(LessonRecord lesson) {
        return lesson.getName() != null && !lesson.getName().isEmpty()
                && lesson.getType() != null && !lesson.getType().isEmpty();
    }
}
