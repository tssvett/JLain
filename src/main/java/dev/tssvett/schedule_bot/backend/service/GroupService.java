package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.exception.database.StudentNotExistsException;
import dev.tssvett.schedule_bot.persistence.model.tables.records.EducationalGroupRecord;
import dev.tssvett.schedule_bot.persistence.model.tables.records.StudentRecord;
import dev.tssvett.schedule_bot.persistence.repository.GroupRepository;
import dev.tssvett.schedule_bot.persistence.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

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
}
