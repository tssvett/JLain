package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.exception.database.StudentNotExistsException;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.parsing.parser.GroupParser;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import dev.tssvett.schedule_bot.persistence.repository.GroupRepository;
import dev.tssvett.schedule_bot.persistence.repository.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupParser groupParser;
    private final FacultyService facultyService;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private static final List<Integer> COURSES = List.of(1, 2, 3, 4, 5);

    public List<EducationalGroupRecord> getFilteredByCourseAndFacultyGroups(Long userId) {
        StudentRecord student = studentRepository.findById(userId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + userId));

        return groupRepository.findAllByFacultyIdAndCourse(student.getFacultyId(), student.getCourse());

    }

    public void saveGroups(List<EducationalGroupRecord> educationalGroups) {
        groupRepository.saveAll(educationalGroups);
    }

    public List<EducationalGroupRecord> findAllGroups() {
        return groupRepository.findAll();
    }

    public EducationalGroupRecord getGroupById(Long aLong) {
        return groupRepository.findById(aLong)
                .orElseThrow(() -> new StudentNotExistsException("No group with id: " + aLong));
    }

    public List<EducationalGroupRecord> parseGroupsFromAllFaculties() {
        List<EducationalGroupRecord> groupsToSave = new ArrayList<>();
        List<FacultyRecord> facultyRecordList = facultyService.findAllFaculties();

        facultyRecordList.forEach(faculty -> {
            log.debug("Parsing groups from faculty {} with id {}", faculty.getName(), faculty.getFacultyId());
            groupsToSave.addAll(parseGroupsFromFaculty(faculty));
        });

        return groupsToSave;
    }

    private List<EducationalGroupRecord> parseGroupsFromFaculty(FacultyRecord faculty) {
        List<EducationalGroupRecord> educationalGroups = new ArrayList<>();

        for (Integer course : COURSES) {
            log.debug("Parsing groups from course {}", course);
            List<EducationalGroupRecord> parsedGroups = groupParser.parse(faculty.getFacultyId(), course)
                    .stream()
                    .map(Mapper::toEducationalGroupRecord)
                    .toList();

            setGroupRelations(faculty, course, parsedGroups);
            log.debug("Parsed {} groups", parsedGroups.size());
            educationalGroups.addAll(parsedGroups);
        }

        return educationalGroups;
    }

    private void setGroupRelations(FacultyRecord faculty, Integer course, List<EducationalGroupRecord> educationalGroups) {
        educationalGroups.forEach(group -> {
            group.setFacultyId(faculty.getFacultyId());
            group.setCourse(Long.valueOf(course));
        });
    }
}
