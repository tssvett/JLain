package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.entity.Group;
import dev.tssvett.schedule_bot.backend.exception.database.GroupNotExistException;
import dev.tssvett.schedule_bot.backend.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    public Group findGroupById(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new GroupNotExistException("No group with id: " + id));
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public void saveGroup(Group group) {
        groupRepository.save(group);
    }

    public void saveGroups(List<Group> groups) {
        groupRepository.saveAll(groups);
    }
}
