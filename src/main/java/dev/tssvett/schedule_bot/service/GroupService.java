package dev.tssvett.schedule_bot.service;

import dev.tssvett.schedule_bot.entity.Faculty;
import dev.tssvett.schedule_bot.entity.Group;
import dev.tssvett.schedule_bot.repository.FacultyRepository;
import dev.tssvett.schedule_bot.repository.GroupRepository;
import dev.tssvett.schedule_bot.schedule.parser.FacultyParser;
import dev.tssvett.schedule_bot.schedule.parser.GroupParser;
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
public class GroupService {

    private final GroupRepository groupRepository;
    private final FacultyRepository facultyRepository;
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
                // Устанавливаем факультет для каждой группы
                for (Group group : groups) {
                    group.setFaculty(faculty); // Устанавливаем факультет
                    group.setCourse(Long.valueOf(course));
                }
                log.info("Parsed " + groups.size() + " groups");
                groupRepository.saveAll(groups);
            }
        }
        List<Group> groups = groupRepository.findAll();
        log.info("Total groups in database: " + groups.size());
    }

    private void saveAllFacultiesInDatabase(List<Faculty> faculties) {
        log.info("Staring parsing all faculties");
        facultyRepository.saveAll(faculties);
        log.info("Total faculties in database: " + facultyRepository.findAll().size());
    }

    /*
    @Scheduled(fixedDelayString = "${scheduling.group.delay}")
    public void showGroups() {
        List<Group> groups = groupRepository.findAll();
        groups.stream().map(this::showGroup).forEach(log::info);
    }

    private String showGroup(Group group) {
        return group.getGroupId() + " " + group.getName();
    }

     */

}
