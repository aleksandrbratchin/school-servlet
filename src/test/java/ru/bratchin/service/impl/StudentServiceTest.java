package ru.bratchin.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.bratchin.dto.StudentDto;
import ru.bratchin.entity.Student;
import ru.bratchin.mapper.Mapper;
import ru.bratchin.mapper.StudentMapper;
import ru.bratchin.repository.api.StudentRepositoryApi;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    @Mock
    private StudentRepositoryApi studentRepository;

    private Mapper<Student, StudentDto> studentMapper;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentMapper = new StudentMapper();
        studentService = new StudentService(studentRepository, studentMapper);
    }

    @Nested
    class GetAllStudentsTests {

        @Test
        void shouldReturnAllStudents() {
            Student student = new Student(
                    UUID.fromString(
                            "123e4567-e89b-12d3-a456-556642440000"),
                    "Гарри",
                    "Поттер",
                    1,
                    LocalDate.of(1991, 9, 1),
                    LocalDate.of(1997, 6, 30),
                    UUID.randomUUID()
            );
            when(studentRepository.findAll()).thenReturn(List.of(student));

            List<StudentDto> result = studentService.getAll();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getFirstName()).isEqualTo("Гарри");
            assertThat(result.get(0).getLastName()).isEqualTo("Поттер");
            verify(studentRepository, times(1)).findAll();
        }

        @Test
        void shouldReturnEmptyListWhenNoStudentsFound() {
            when(studentRepository.findAll()).thenReturn(List.of());

            List<StudentDto> result = studentService.getAll();

            assertThat(result).isEmpty();
            verify(studentRepository, times(1)).findAll();
        }
    }

    @Nested
    class GetStudentByIdTests {

        @Test
        void shouldReturnStudentById() {
            UUID id = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
            Student student = new Student(
                    id,
                    "Гарри",
                    "Поттер",
                    1,
                    LocalDate.of(1991, 9, 1),
                    LocalDate.of(1997, 6, 30),
                    UUID.randomUUID()
            );
            when(studentRepository.findById(id)).thenReturn(Optional.of(student));

            StudentDto result = studentService.getById(id);

            assertThat(result.getFirstName()).isEqualTo("Гарри");
            assertThat(result.getLastName()).isEqualTo("Поттер");
            verify(studentRepository, times(1)).findById(id);
        }

        @Test
        void shouldThrowExceptionWhenStudentNotFound() {
            UUID id = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
            when(studentRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> studentService.getById(id))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("Student not found with id: " + id);
        }
    }

    @Nested
    class SaveStudentTests {

        @Test
        void shouldSaveStudent() {
            StudentDto studentDto = new StudentDto(
                    UUID.fromString("123e4567-e89b-12d3-a456-556642440000"),
                    "Гарри",
                    "Поттер",
                    1,
                    "01.09.1991",
                    "30.06.1997",
                    UUID.randomUUID()
            );

            studentService.save(studentDto);

            verify(studentRepository, times(1)).save(any(Student.class));
        }
    }

    @Nested
    class UpdateStudentTests {

        @Test
        void shouldUpdateStudent() {
            StudentDto studentDto = new StudentDto(
                    UUID.fromString("123e4567-e89b-12d3-a456-556642440000"),
                    "Гарри",
                    "Поттер",
                    1,
                    "01.09.1991",
                    "30.06.1997",
                    UUID.randomUUID()
            );

            studentService.update(studentDto);

            verify(studentRepository, times(1)).update(any(Student.class));
        }
    }

    @Nested
    class DeleteStudentByIdTests {

        @Test
        void shouldDeleteStudentById() {
            UUID id = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");

            studentService.deleteById(id);

            verify(studentRepository, times(1)).deleteById(id);
        }
    }
}
