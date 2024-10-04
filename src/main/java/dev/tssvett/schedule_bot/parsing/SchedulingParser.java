package dev.tssvett.schedule_bot.parsing;

import dev.tssvett.schedule_bot.persistence.entity.Faculty;
import dev.tssvett.schedule_bot.persistence.entity.Group;
import dev.tssvett.schedule_bot.backend.service.FacultyService;
import dev.tssvett.schedule_bot.backend.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<Faculty> faculties = facultyParser.parse();
        saveAllFacultiesInDatabase(faculties);
        saveAllGroupsInDatabase(faculties, courses);
    }

    private void saveAllGroupsInDatabase(List<Faculty> faculties, List<Integer> courses) {
        log.info("Staring parsing all groups in all faculties");
        log.info("Founded " + faculties.size() + " faculties");
        for (Faculty faculty : faculties) {
            log.info("Parsing groups from faculty " + faculty.getName() + " with id " + faculty.getFacultyId());
            for (Integer course : courses) {
                log.info("Parsing groups from course " + course);
                List<Group> groups = groupParser.parse(faculty.getFacultyId(), course);
                for (Group group : groups) {
                    group.setFaculty(faculty);
                    group.setCourse(Long.valueOf(course));
                }
                log.info("Parsed " + groups.size() + " groups");
                groupService.saveGroups(groups);
            }
        }
        log.info("Total groups in database: " + groupService.findAllGroups().size());
    }

    private void saveAllFacultiesInDatabase(List<Faculty> faculties) {
        log.info("Staring parsing all faculties");
        facultyService.saveFaculties(faculties);
        log.info("Total faculties in database: {}", facultyService.findAllFaculties().size());
    }
}
