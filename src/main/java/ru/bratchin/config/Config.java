package ru.bratchin.config;

public interface Config {
    <T> Class<? extends T> getImplClass(Class<T> ifc);

    <T> Class<? extends T> getImplClass(Class<T> ifc, String implName);
}
