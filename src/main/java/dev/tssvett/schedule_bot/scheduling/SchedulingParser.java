package dev.tssvett.schedule_bot.scheduling;

import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.backend.service.GroupService;
import dev.tssvett.schedule_bot.parsing.parser.FacultyParser;
import dev.tssvett.schedule_bot.parsing.parser.GroupParser;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.group.enabled", havingValue = "true")
public class SchedulingParser {
    private final GroupService groupService;
    private final FacultyService facultyService;
    private final GroupParser groupParser;
    private final FacultyParser facultyParser;

    @Scheduled(fixedDelayString = "${scheduling.group.delay}")
    public void updateGroupsInDatabase() {
        List<Integer> courses = List.of(1, 2, 3, 4, 5);
        List<FacultyRecord> faculties = facultyParser.parse()
                .stream()
                .map(Mapper::toFacultyRecord)
                .toList();
        saveAllFacultiesInDatabase(faculties);
        saveAllGroupsInDatabase(faculties, courses);
    }

    private void saveAllGroupsInDatabase(List<FacultyRecord> faculties, List<Integer> courses) {
        log.info("Staring parsing all groups in {} faculties", faculties.size());
        for (FacultyRecord faculty : faculties) {
            log.debug("Parsing groups from faculty " + faculty.getName() + " with id " + faculty.getFacultyId());
            for (Integer course : courses) {
                log.debug("Parsing groups from course " + course);
                List<EducationalGroupRecord> educationalGroups = groupParser.parse(faculty.getFacultyId(), course)
                        .stream()
                        .map(Mapper::toEducationalGroupRecord)
                        .toList();
                for (EducationalGroupRecord educationalGroup : educationalGroups) {
                    educationalGroup.setFacultyId(faculty.getFacultyId());
                    educationalGroup.setCourse(Long.valueOf(course));
                }
                log.debug("Parsed " + educationalGroups.size() + " groups");
                groupService.saveGroups(educationalGroups);
            }
        }
        log.info("Parsing completed successfully! Total groups in database: " + groupService.findAllGroups().size());
    }

    private void saveAllFacultiesInDatabase(List<FacultyRecord> faculties) {
        log.info("Staring parsing all faculties");
        facultyService.saveFaculties(faculties);
        log.info("Total faculties in database: {}", facultyService.findAllFaculties().size());
    }
}
