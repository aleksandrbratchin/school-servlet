package ru.bratchin.repository.api;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, K> {
    List<T> findAll();
    Optional<T> findById(K id);
    void save(T entity);
    void update(T entity);
    void deleteById(K id);
}
