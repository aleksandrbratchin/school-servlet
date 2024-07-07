package ru.bratchin.service.api;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface CrudService<T> {
    List<T> getAll() throws SQLException;
    T getById(UUID id) throws SQLException;
    void save(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void deleteById(UUID id) throws SQLException;
}
