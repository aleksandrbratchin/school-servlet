package ru.bratchin.service.impl;

import ru.bratchin.dto.StudentDto;
import ru.bratchin.entity.Student;
import ru.bratchin.mapper.Mapper;
import ru.bratchin.repository.api.StudentRepositoryApi;
import ru.bratchin.service.api.StudentServiceApi;
import ru.bratchin.util.ObjectFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class StudentService implements StudentServiceApi {

    private final StudentRepositoryApi studentRepository;

    private final Mapper<Student, StudentDto> studentMapper;

    public StudentService() {
        studentRepository = ObjectFactory.getInstance().createObject(StudentRepositoryApi.class);
        studentMapper = ObjectFactory.getInstance().createObject(Mapper.class, "studentMapper");
    }

    public StudentService(StudentRepositoryApi studentRepository, Mapper<Student, StudentDto> studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<StudentDto> getAll() {
        return studentRepository.findAll().stream().map(studentMapper::toDto).toList();
    }

    @Override
    public StudentDto getById(UUID id) {
        return studentMapper.toDto(studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found with id: " + id)));
    }

    @Override
    public void save(StudentDto student) {
        studentRepository.save(studentMapper.toEntity(student));
    }

    @Override
    public void update(StudentDto student) {
        studentRepository.update(studentMapper.toEntity(student));
    }

    @Override
    public void deleteById(UUID id) {
        studentRepository.deleteById(id);
    }
}
