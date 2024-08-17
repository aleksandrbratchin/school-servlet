package ru.bratchin.mapper;

import ru.bratchin.dto.StudentDto;
import ru.bratchin.entity.Student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StudentMapper implements Mapper<Student, StudentDto> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public StudentDto toDto(Student entity) {
        return new StudentDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getCourse(),
                entity.getAdmissionDate().format(FORMATTER),
                entity.getDateOfGraduation() != null ? entity.getDateOfGraduation().format(FORMATTER) : null,
                entity.getFacultyId()
        );
    }

    @Override
    public Student toEntity(StudentDto dto) {
        return new Student(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getCourse(),
                LocalDate.parse(dto.getAdmissionDate(), FORMATTER),
                dto.getDateOfGraduation() != null ? LocalDate.parse(dto.getDateOfGraduation(), FORMATTER) : null,
                dto.getFacultyId()
        );
    }
}