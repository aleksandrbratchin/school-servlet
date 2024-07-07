package ru.bratchin.mapper;

import ru.bratchin.dto.FacultyDto;
import ru.bratchin.entity.Faculty;

public class FacultyMapper implements Mapper<Faculty, FacultyDto> {

    @Override
    public FacultyDto toDto(Faculty faculty) {
        return new FacultyDto(
                faculty.getId(),
                faculty.getName(),
                faculty.getDescription()
        );
    }

    @Override
    public Faculty toEntity(FacultyDto facultyDto) {
        return new Faculty(
                facultyDto.getId(),
                facultyDto.getName(),
                facultyDto.getDescription()
        );
    }
}
