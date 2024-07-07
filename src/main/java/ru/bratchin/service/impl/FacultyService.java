package ru.bratchin.service.impl;

import ru.bratchin.util.ObjectFactory;
import ru.bratchin.entity.Faculty;
import ru.bratchin.repository.api.FacultyRepositoryApi;
import ru.bratchin.service.api.FacultyServiceApi;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FacultyService implements FacultyServiceApi {

    private final FacultyRepositoryApi facultyRepository = ObjectFactory.getInstance().createObject(FacultyRepositoryApi.class);

    @Override
    public List<Faculty> getAll() throws SQLException {
        return facultyRepository.findAll();
    }

    @Override
    public Faculty getById(UUID id) throws SQLException {
        return facultyRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Faculty not found with id: " + id));
    }

    @Override
    public void save(Faculty faculty) throws SQLException {
        facultyRepository.save(faculty);
    }

    @Override
    public void update(Faculty faculty) throws SQLException {
        facultyRepository.update(faculty);
    }

    @Override
    public void deleteById(UUID id) throws SQLException {
        facultyRepository.deleteById(id);
    }
}
