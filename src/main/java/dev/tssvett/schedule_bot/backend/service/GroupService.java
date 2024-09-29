package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.exception.database.StudentNotExistsException;
import dev.tssvett.schedule_bot.persistence.entity.Group;
import dev.tssvett.schedule_bot.persistence.entity.Student;
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

    public List<Group> getFilteredByCourseAndFacultyGroups(Long userId) {
        Student user = studentRepository.findById(userId)
                .orElseThrow(() -> new StudentNotExistsException("No student with id: " + userId));

        return groupRepository.findAll()
                .stream()
                .filter(group -> group.getCourse().equals(user.getCourse()))
                .filter(group -> group.getFaculty().getName().equals(user.getFaculty().getName()))
                .toList();

    }


    public void saveGroups(List<Group> groups) {
        groupRepository.saveAll(groups);
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }
}
