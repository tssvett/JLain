package dev.tssvett.schedule_bot.service;

import dev.tssvett.schedule_bot.entity.Group;
import dev.tssvett.schedule_bot.exception.GroupNotExistException;
import dev.tssvett.schedule_bot.repository.GroupRepository;
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
}
