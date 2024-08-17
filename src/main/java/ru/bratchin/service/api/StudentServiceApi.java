package ru.bratchin.service.api;

import ru.bratchin.dto.StudentDto;

import java.util.List;
import java.util.UUID;

public interface StudentServiceApi {
    List<StudentDto> getAll();

    StudentDto getById(UUID id);

    void save(StudentDto student);

    void update(StudentDto student);

    void deleteById(UUID id);
}
