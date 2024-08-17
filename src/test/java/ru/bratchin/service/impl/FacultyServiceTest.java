package ru.bratchin.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.bratchin.dto.FacultyDto;
import ru.bratchin.dto.StudentDto;
import ru.bratchin.entity.Faculty;
import ru.bratchin.entity.Student;
import ru.bratchin.mapper.FacultyMapper;
import ru.bratchin.mapper.Mapper;
import ru.bratchin.mapper.StudentMapper;
import ru.bratchin.repository.api.FacultyRepositoryApi;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class FacultyServiceTest {

    @Mock
    private FacultyRepositoryApi facultyRepository;

    private Mapper<Faculty, FacultyDto> facultyMapper;
    private Mapper<Student, StudentDto> studentMapper;

    @InjectMocks
    private FacultyService facultyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentMapper = new StudentMapper();
        facultyMapper = new FacultyMapper(studentMapper);
        facultyService = new FacultyService(facultyRepository, facultyMapper);
    }

    @Nested
    class GetAllFacultiesTests {

        @Test
        void shouldReturnAllFaculties() {
            Faculty faculty = new Faculty(
                    UUID.fromString("123e4567-e89b-12d3-a456-556642440000"),
                    "Гриффиндор",
                    "Факультет отваги и смелости",
                    List.of());
            when(facultyRepository.findAll()).thenReturn(List.of(faculty));

            List<FacultyDto> result = facultyService.getAll();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Гриффиндор");
            verify(facultyRepository, times(1)).findAll();
        }

        @Test
        void shouldReturnEmptyListWhenNoFacultiesFound() {
            when(facultyRepository.findAll()).thenReturn(List.of());

            List<FacultyDto> result = facultyService.getAll();

            assertThat(result).isEmpty();
            verify(facultyRepository, times(1)).findAll();
        }
    }

    @Nested
    class GetFacultyByIdTests {

        @Test
        void shouldReturnFacultyById() {
            UUID id = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
            Faculty faculty = new Faculty(
                    id,
                    "Гриффиндор",
                    "Факультет отваги и смелости",
                    List.of()
            );
            when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));

            FacultyDto result = facultyService.getById(id);

            assertThat(result.getName()).isEqualTo("Гриффиндор");
            verify(facultyRepository, times(1)).findById(id);
        }

        @Test
        void shouldThrowExceptionWhenFacultyNotFound() {
            UUID id = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
            when(facultyRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> facultyService.getById(id))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("Faculty not found with id: " + id);
        }
    }

    @Nested
    class SaveFacultyTests {

        @Test
        void shouldSaveFaculty() {
            FacultyDto facultyDto = new FacultyDto(
                    UUID.fromString("123e4567-e89b-12d3-a456-556642440000"),
                    "Гриффиндор",
                    "Факультет отваги и смелости",
                    List.of()
            );
            Faculty faculty = facultyMapper.toEntity(facultyDto);

            facultyService.save(facultyDto);

            verify(facultyRepository, times(1)).save(any(Faculty.class));
        }
    }

    @Nested
    class UpdateFacultyTests {

        @Test
        void shouldUpdateFaculty() {
            FacultyDto facultyDto = new FacultyDto(
                    UUID.fromString("123e4567-e89b-12d3-a456-556642440000"),
                    "Гриффиндор",
                    "Факультет отваги и смелости",
                    List.of()
            );
            Faculty faculty = facultyMapper.toEntity(facultyDto);

            facultyService.update(facultyDto);

            verify(facultyRepository, times(1)).update(any(Faculty.class));
        }
    }

    @Nested
    class DeleteFacultyByIdTests {

        @Test
        void shouldDeleteFacultyById() {
            UUID id = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");

            facultyService.deleteById(id);

            verify(facultyRepository, times(1)).deleteById(id);
        }
    }
}