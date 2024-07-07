package ru.bratchin.service.impl;

import ru.bratchin.util.ObjectFactory;
import ru.bratchin.entity.Student;
import ru.bratchin.repository.api.StudentRepositoryApi;
import ru.bratchin.service.api.StudentServiceApi;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class StudentService implements StudentServiceApi {

    private final StudentRepositoryApi studentRepository = ObjectFactory.getInstance().createObject(StudentRepositoryApi.class);

    @Override
    public List<Student> getAll() throws SQLException {
        return studentRepository.findAll();
    }

    @Override
    public Student getById(UUID id) throws SQLException {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found with id: " + id));
    }

    @Override
    public void save(Student student) throws SQLException {
        studentRepository.save(student);
    }

    @Override
    public void update(Student student) throws SQLException {
        studentRepository.update(student);
    }

    @Override
    public void deleteById(UUID id) throws SQLException {
        studentRepository.deleteById(id);
    }
}
