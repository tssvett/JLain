package dev.tssvett.schedule_bot.backend.service;

import dev.tssvett.schedule_bot.backend.exception.database.FacultyNotExistException;
import dev.tssvett.schedule_bot.parsing.parser.FacultyParser;
import dev.tssvett.schedule_bot.persistence.model.tables.records.FacultyRecord;
import dev.tssvett.schedule_bot.persistence.repository.FacultyRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {
    FacultyService facultyService;
    @Mock
    FacultyRepository facultyRepository;
    @Mock
    FacultyParser facultyParser;

    @BeforeEach
    void setup() {
        facultyService = new FacultyService(facultyRepository, facultyParser);
    }

    @Test
    void findAllFaculties_listOfFaculties_returnsListOfFaculties() {
        // Arrange
        List<FacultyRecord> expectedListOfFaculties = getListFaculties1();
        when(facultyRepository.findAll()).thenReturn(expectedListOfFaculties);

        // Act && Assert
        assertEquals(expectedListOfFaculties, facultyService.findAllFaculties());
    }

    @Test
    void findAllFaculties_emptyList_returnsEmptyList() {
        // Arrange
        List<FacultyRecord> expectedListOfFaculties = new ArrayList();
        when(facultyRepository.findAll()).thenReturn(expectedListOfFaculties);

        // Act && Assert
        assertEquals(expectedListOfFaculties, facultyService.findAllFaculties());
    }


    @Test
    void getFacultyById_existingId_returnsFacultyRecord() {
        // Arrange
        Long facultyId = 3L;
        FacultyRecord expectedRecord = new FacultyRecord();
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(expectedRecord));

        // Act && Assert
        assertEquals(expectedRecord, facultyService.getFacultyById(facultyId));
    }

    @Test
    void getFacultyById_notExistingId_throwsFacultyNotExistException() {
        // Arrange
        Long facultyId = 4L;
        when(facultyRepository.findById(facultyId)).thenThrow(FacultyNotExistException.class);

        // Act && Assert
        assertThrows(FacultyNotExistException.class, () -> facultyService.getFacultyById(facultyId));
    }

    private List<FacultyRecord> getListFaculties1() {
        return List.of(new FacultyRecord(1L, "Faculty 1"), new FacultyRecord(2L, "Faculty 2"), new FacultyRecord(3L, "Faculty 3"));
    }
}