package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.exception.database.FacultyNotExistException;
import dev.tssvett.schedule_bot.backend.mapper.Mapper;
import dev.tssvett.schedule_bot.parsing.parser.FacultyParser;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
import dev.tssvett.schedule_bot.persistence.repository.FacultyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final FacultyParser facultyParser;

    public List<FacultyRecord> findAllFaculties() {
        return facultyRepository.findAll();
    }

    public FacultyRecord getFacultyById(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new FacultyNotExistException("No faculty with id: " + facultyId));
    }

    public void saveFaculties(List<FacultyRecord> faculties) {
        facultyRepository.saveAll(faculties);
    }

    public void parseAndSaveFaculties() {
        this.saveFaculties(
                facultyParser.parse()
                        .stream()
                        .map(Mapper::toFacultyRecord)
                        .toList()
        );
    }
}
