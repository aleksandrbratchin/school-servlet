package ru.bratchin.mapper;

public interface Mapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}