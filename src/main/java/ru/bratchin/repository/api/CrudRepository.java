package ru.bratchin.repository.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, K> {
    List<T> findAll() throws SQLException;
    Optional<T> findById(K id) throws SQLException;
    void save(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void deleteById(K id) throws SQLException;
}
