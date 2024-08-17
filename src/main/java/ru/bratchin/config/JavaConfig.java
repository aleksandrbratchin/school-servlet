package ru.bratchin.config;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JavaConfig implements Config {

    private final Reflections scanner;
    private final Map<Class<?>, Map<String, Class<?>>> ifcToImplClasses;

    public JavaConfig(String packageToScan, Map<Class<?>, Map<String, Class<?>>> ifcToImplClasses) {
        this.ifcToImplClasses = ifcToImplClasses;
        this.scanner = new Reflections(packageToScan);
    }

    private <T> void addImplClasses(Class<T> ifc) {
        Set<Class<? extends T>> classes = scanner.getSubTypesOf(ifc);
        Map<String, Class<?>> nameToClass = new HashMap<>();
        for (Class<? extends T> clazz : classes) {
            nameToClass.put(clazz.getSimpleName(), clazz);
        }
        ifcToImplClasses.put(ifc, nameToClass);
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> ifc) {
        Map<String, Class<?>> nameToClass = ifcToImplClasses.get(ifc);
        if (nameToClass == null || nameToClass.isEmpty()) {
            throw new RuntimeException(ifc + " has 0 implementations, please update your config");
        }
        // Возвращаем первую реализацию по умолчанию
        return (Class<? extends T>) nameToClass.values().iterator().next();
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> ifc, String implName) {
        Map<String, Class<?>> nameToClass = ifcToImplClasses.get(ifc);
        if (nameToClass == null || !nameToClass.containsKey(implName)) {
            throw new RuntimeException(ifc + " has no implementation with name " + implName);
        }
        return (Class<? extends T>) nameToClass.get(implName);
    }
}
