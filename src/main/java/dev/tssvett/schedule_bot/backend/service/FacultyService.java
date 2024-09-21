package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.entity.Faculty;
import dev.tssvett.schedule_bot.backend.exception.database.FacultyNotExistException;
import dev.tssvett.schedule_bot.backend.repository.FacultyRepository;
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

    public void saveFaculties(List<Faculty> faculties) {
        facultyRepository.saveAll(faculties);
    }
}
