package ru.bratchin.repository.api;

import ru.bratchin.entity.Faculty;


import java.util.UUID;

public interface FacultyRepositoryApi extends CrudRepository<Faculty, UUID> {
}
