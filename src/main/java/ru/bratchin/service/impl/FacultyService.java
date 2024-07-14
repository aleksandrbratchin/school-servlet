package ru.bratchin.service.impl;

import ru.bratchin.dto.FacultyDto;
import ru.bratchin.entity.Faculty;
import ru.bratchin.mapper.Mapper;
import ru.bratchin.repository.api.FacultyRepositoryApi;
import ru.bratchin.service.api.FacultyServiceApi;
import ru.bratchin.util.ObjectFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FacultyService implements FacultyServiceApi {

    private final FacultyRepositoryApi facultyRepository = ObjectFactory.getInstance().createObject(FacultyRepositoryApi.class);

    private final Mapper<Faculty, FacultyDto> facultyMapper = ObjectFactory.getInstance().createObject(Mapper.class, "facultyMapper");

    @Override
    public List<FacultyDto> getAll() {
        return facultyRepository.findAll().stream().map(facultyMapper::toDto).toList();
    }

    @Override
    public FacultyDto getById(UUID id) {
        return facultyMapper.toDto(
                facultyRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Faculty not found with id: " + id))
        );
    }

    @Override
    public void save(FacultyDto faculty) {
        facultyRepository.save(
                facultyMapper.toEntity(faculty)
        );
    }

    @Override
    public void update(FacultyDto faculty) {
        facultyRepository.update(
                facultyMapper.toEntity(faculty)
        );
    }

    @Override
    public void deleteById(UUID id) {
        facultyRepository.deleteById(id);
    }
}
