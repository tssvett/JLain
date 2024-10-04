package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.persistence.entity.Faculty;
import dev.tssvett.schedule_bot.persistence.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public List<Faculty> findAllFaculties() {
        return facultyRepository.findAll();
    }

    public void saveFaculties(List<Faculty> faculties) {
        facultyRepository.saveAll(faculties);
    }
}
