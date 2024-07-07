package ru.bratchin.repository.api;

import ru.bratchin.entity.Student;

import java.util.UUID;

public interface StudentRepositoryApi extends CrudRepository<Student, UUID> {

}
