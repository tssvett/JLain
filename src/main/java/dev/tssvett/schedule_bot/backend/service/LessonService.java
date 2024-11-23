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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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

    public List<LessonRecord> parseLessonsFromAllGroups() {
        AtomicInteger currentGroup = new AtomicInteger();
        int groupsCount = groupService.findAllGroups().size();

        List<LessonRecord> lessonsToSave = new ArrayList<>();
        int currentEducationalWeek = dateUtils.calculateCurrentUniversityEducationalWeek();

        groupService.findAllGroups().forEach(educationalGroupRecord -> {
            List<LessonRecord> list = getCurrentWeekLessonsFromGroup(educationalGroupRecord, currentEducationalWeek);
            lessonsToSave.addAll(list);
            log.info("[{}] Parsed {} lessons from group {}",
                    String.format("%d/%d", currentGroup.incrementAndGet(), groupsCount), list.size(),
                    educationalGroupRecord.getName());
        });
        log.info("Total lessons to save: {}", lessonsToSave.size());

        return lessonsToSave;
    }

    public void parseAndSaveLessonsFromAllGroupsCompletableFuture() {
        AtomicInteger currentGroupCounter = new AtomicInteger();
        List<LessonRecord> lessonsToSave = new ArrayList<>();
        List<EducationalGroupRecord> allGroups = groupService.findAllGroups();
        int groupsCount = allGroups.size();
        int currentEducationalWeek = dateUtils.calculateCurrentUniversityEducationalWeek();

        List<CompletableFuture<List<LessonRecord>>> futures = new ArrayList<>();
        allGroups.forEach(educationalGroupRecord ->
                futures.add(CompletableFuture.supplyAsync(() -> {
                    List<LessonRecord> list = getCurrentWeekLessonsFromGroup(educationalGroupRecord, currentEducationalWeek);
                    log.info("[{}] Parsed {} lessons from group {}",
                            String.format("%d/%d", currentGroupCounter.incrementAndGet(), groupsCount), list.size(),
                            educationalGroupRecord.getName());
                    return list;
                }))
        );

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        futures.forEach(future -> {
            try {
                List<LessonRecord> list = future.get();
                if (list != null) {
                    lessonsToSave.addAll(list);
                    log.info("Total lessons to save in future: {}", list.size());
                }
            } catch (Exception e) {
                log.error("Error while parsing lessons: {}", e.getMessage());
            }
        });

        this.saveLessonsWithoutDuplication(lessonsToSave);
        log.info("Lessons saved successfully");
    }

    private List<LessonRecord> getCurrentWeekLessonsFromGroup(EducationalGroupRecord educationalGroupRecord, int currentEducationalWeek) {
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

    public void saveAllLessons(List<LessonRecord> lessonsToSave) {
        lessonRepository.saveAll(lessonsToSave);
    }

    public void saveLessonsWithoutDuplication(List<LessonRecord> lessonsToSave) {
        List<LessonRecord> currentLessons = lessonRepository.findAllLessons();
        for (LessonRecord lesson : currentLessons) {
            lessonsToSave.removeIf(lessonToSave -> isNewLesson(lesson, lessonToSave));
        }
        log.info("Total NEW lessons to save: {}", lessonsToSave.size());
        lessonRepository.saveAll(lessonsToSave);
    }

    private static boolean isNewLesson(LessonRecord lesson, LessonRecord lessonToSave) {
        return lessonToSave.getName().equals(lesson.getName())
                && lessonToSave.getDateNumber().equals(lesson.getDateNumber())
                && lessonToSave.getTime().equals(lesson.getTime())
                && lessonToSave.getType().equals(lesson.getType())
                && lessonToSave.getGroupId().equals(lesson.getGroupId());
    }


    private boolean isExist(LessonRecord lesson) {
        return !StringUtils.isAllBlank(lesson.getName(), lesson.getType());
    }
}
