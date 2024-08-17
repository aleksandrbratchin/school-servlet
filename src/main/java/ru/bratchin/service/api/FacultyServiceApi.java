package ru.bratchin.service.api;

import ru.bratchin.dto.FacultyDto;

import java.util.List;
import java.util.UUID;

public interface FacultyServiceApi {


    List<FacultyDto> getAll();

    FacultyDto getById(UUID id);

    void save(FacultyDto faculty);

    void update(FacultyDto faculty);

    void deleteById(UUID id);
}
