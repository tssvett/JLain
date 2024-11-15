package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.bot.utils.DateUtils;
import dev.tssvett.schedule_bot.parsing.parser.LessonParser;
import dev.tssvett.schedule_bot.persistence.model.tables.records.LessonRecord;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final StudentService studentService;
    private final LessonParser lessonParser;
    private final DateUtils dateUtils;


    public List<LessonInfoDto> getWeekScheduleList(Long userId) {
        Long groupId = Mapper.toStudentInfoDto(studentService.getStudentInfoById(userId)).groupId();

        List<LessonRecord> lessonsInWeek = lessonParser.parse(
                        groupId, dateUtils.calculateCurrentUniversityEducationalWeek()
                )
                .stream()
                .map(Mapper::toLessonRecord)
                .toList();

        return lessonsInWeek
                .stream()
                .map(Mapper::toLessonInfoDto)
                .toList();
    }

    public Map<String, List<LessonInfoDto>> getWeekScheduleMapByDate(Long userId) {
        Long groupId = Mapper.toStudentInfoDto(studentService.getStudentInfoById(userId)).groupId();

        List<LessonRecord> lessons = lessonParser.parse(
                        groupId, dateUtils.calculateCurrentUniversityEducationalWeek()
                )
                .stream()
                .map(Mapper::toLessonRecord)
                .toList();

        return lessons.stream()
                .filter(this::isExist)
                .map(Mapper::toLessonInfoDto)
                .collect(Collectors.groupingBy(LessonInfoDto::dateDay));
    }

    private boolean isExist(LessonRecord lesson) {
        return !(StringUtils.isAllEmpty(lesson.getName(), lesson.getType()) &&
                StringUtils.isAllBlank(lesson.getName(), lesson.getType()));
    }
}
