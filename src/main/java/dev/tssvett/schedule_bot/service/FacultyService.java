package dev.tssvett.schedule_bot.service;

import dev.tssvett.schedule_bot.entity.Faculty;
import dev.tssvett.schedule_bot.exception.FacultyNotExistException;
import dev.tssvett.schedule_bot.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public Faculty findFacultyById(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotExistException("No faculty with id: " + id));
    }

    public List<Faculty> findAllFaculties() {
        return facultyRepository.findAll();
    }
}
