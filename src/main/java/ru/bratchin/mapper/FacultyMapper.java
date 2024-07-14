package ru.bratchin.mapper;

import ru.bratchin.dto.FacultyDto;
import ru.bratchin.dto.StudentDto;
import ru.bratchin.entity.Faculty;
import ru.bratchin.entity.Student;
import ru.bratchin.util.ObjectFactory;

import java.util.ArrayList;

public class FacultyMapper implements Mapper<Faculty, FacultyDto> {

    private final Mapper<Student, StudentDto> studentMapper = ObjectFactory.getInstance().createObject(Mapper.class, "studentMapper");

    @Override
    public FacultyDto toDto(Faculty faculty) {
        return new FacultyDto(
                faculty.getId(),
                faculty.getName(),
                faculty.getDescription(),
                faculty.getStudents() == null ? new ArrayList<>() : faculty.getStudents().stream()
                        .map(studentMapper::toDto)
                        .toList()
        );
    }

    @Override
    public Faculty toEntity(FacultyDto facultyDto) {
        return new Faculty(
                facultyDto.getId(),
                facultyDto.getName(),
                facultyDto.getDescription(),
                facultyDto.getStudents()==null ? new ArrayList<>() : facultyDto.getStudents().stream()
                        .map(studentMapper::toEntity)
                        .toList()
        );
    }
}
