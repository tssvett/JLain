package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.dto.LessonInfoDto;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.bot.utils.DateUtils;
import dev.tssvett.schedule_bot.parsing.dto.LessonParserDto;
import dev.tssvett.schedule_bot.parsing.parser.LessonParser;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.LessonRecord;
import dev.tssvett.schedule_bot.persistence.repository.LessonRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonService {
    private final StudentService studentService;
    private final GroupService groupService;
    private final LessonParser lessonParser;
    private final LessonRepository lessonRepository;
    private final DateUtils dateUtils;


    public List<LessonInfoDto> getWeekScheduleList(Long userId) {
        Long groupId = Mapper.toStudentInfoDto(studentService.getStudentInfoById(userId)).groupId();

        /*
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

         */
        return new ArrayList<>();
    }

    public Map<String, List<LessonInfoDto>> getWeekScheduleMapByDate(Long userId) {
        Long groupId = Mapper.toStudentInfoDto(studentService.getStudentInfoById(userId)).groupId();

        /*
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


         */

        return new HashMap<>();
    }

    /*
    public List<LessonRecord> parseLessonsFromAllGroups() {
        int currentGroup = 0;
        int groupsCount = groupService.findAllGroups().size();

        List<LessonRecord> lessonsToSave = new ArrayList<>();
        int currentEducationalWeek = dateUtils.calculateCurrentUniversityEducationalWeek();

        for (EducationalGroupRecord educationalGroupRecord : groupService.findAllGroups()) {
            List<LessonRecord> list = getCurrentWeekLessonsFromGroup(educationalGroupRecord, currentEducationalWeek);
            lessonsToSave.addAll(list);
            log.info("[{}] Parsed {} lessons from group {}", String.format("%d/%d", ++currentGroup, groupsCount),
                    list.size(), educationalGroupRecord.getName());
        }
        log.info("Total lessons to save: {}", lessonsToSave.size());

        return lessonsToSave;
    }

     */

    public void parseAndSaveLessonsFromAllGroupsCompletableFuture() {
        AtomicInteger currentGroupCounter = new AtomicInteger();
        List<LessonRecord> lessonsToSave = new ArrayList<>();
        List<EducationalGroupRecord> allGroups = groupService.findAllGroups();
        int groupsCount = allGroups.size();
        int currentEducationalWeek = dateUtils.calculateCurrentUniversityEducationalWeek();

        List<CompletableFuture<List<LessonRecord>>> futures = new ArrayList<>();
        for (EducationalGroupRecord educationalGroupRecord : allGroups) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                List<LessonRecord> list = getCurrentWeekLessonsFromGroup(educationalGroupRecord, currentEducationalWeek);
                log.info("[{}] Parsed {} lessons from group {}", String.format("%d/%d", currentGroupCounter.incrementAndGet(), groupsCount)
                , list.size(), educationalGroupRecord.getName());
                return list;
            }));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        for (CompletableFuture<List<LessonRecord>> future : futures) {
            try {
                List<LessonRecord> list = future.get();
                if (list != null) {
                    lessonsToSave.addAll(list);
                    log.info("Total lessons to save in future: {}", list.size());
                }
            } catch (Exception e) {
                log.error("Error while parsing lessons: {}", e.getMessage());
            }
        }

        this.saveLessons(lessonsToSave);
        log.info("Lessons saved successfully");
    }

    private List<LessonRecord>getCurrentWeekLessonsFromGroup(EducationalGroupRecord educationalGroupRecord, int currentEducationalWeek) {
        List<LessonParserDto> list = lessonParser.parse(educationalGroupRecord.getGroupId(), currentEducationalWeek);
        List<LessonRecord> weekLessonsList = list
                .stream()
                .map(Mapper::toLessonRecord)
                .toList();
        setLessonsRelations(educationalGroupRecord, weekLessonsList);

        return weekLessonsList;
    }

    private static void setLessonsRelations(EducationalGroupRecord educationalGroupRecord, List<LessonRecord> list) {
        list.forEach(lesson ->
                lesson.setGroupId(educationalGroupRecord.getGroupId()));
    }

    public void saveLessons(List<LessonRecord> lessonsToSave) {
        lessonRepository.saveAll(lessonsToSave);
    }


    private boolean isExist(LessonRecord lesson) {
        return !(StringUtils.isAllEmpty(lesson.getName(), lesson.getType()) &&
                StringUtils.isAllBlank(lesson.getName(), lesson.getType()));
    }
}
