package ru.bratchin.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ru.bratchin.config.Config;
import ru.bratchin.config.JavaConfig;
import ru.bratchin.mapper.FacultyMapper;
import ru.bratchin.mapper.Mapper;
import ru.bratchin.mapper.StudentMapper;
import ru.bratchin.repository.api.FacultyRepositoryApi;
import ru.bratchin.repository.api.StudentRepositoryApi;
import ru.bratchin.repository.impl.FacultyRepository;
import ru.bratchin.repository.impl.StudentRepository;
import ru.bratchin.service.api.FacultyServiceApi;
import ru.bratchin.service.api.StudentServiceApi;
import ru.bratchin.service.impl.FacultyService;
import ru.bratchin.service.impl.StudentService;

import java.util.HashMap;
import java.util.Map;

public class ObjectFactory {
    private static final ObjectFactory instance = new ObjectFactory();
    private final Config config;

    public static ObjectFactory getInstance() {
        return instance;
    }

    private ObjectFactory() {
        Map<Class<?>, Map<String, Class<?>>> ifcToImplClass = new HashMap<>();

        ifcToImplClass.put(FacultyServiceApi.class, Map.of(
                "default", FacultyService.class
        ));

        ifcToImplClass.put(ObjectMapper.class, Map.of(
                "default", ObjectMapper.class
        ));

        ifcToImplClass.put(FacultyRepositoryApi.class, Map.of(
                "default", FacultyRepository.class
        ));

        ifcToImplClass.put(StudentServiceApi.class, Map.of(
                "default", StudentService.class
        ));

        ifcToImplClass.put(StudentRepositoryApi.class, Map.of(
                "default", StudentRepository.class
        ));

        ifcToImplClass.put(Mapper.class, Map.of(
                "facultyMapper", FacultyMapper.class,
                "studentMapper", StudentMapper.class
        ));

        config = new JavaConfig("ru.bratchin", ifcToImplClass);
    }

    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        return createObject(type, null);
    }

    @SneakyThrows
    public <T> T createObject(Class<T> type, String implName) {
        Class<? extends T> implClass;
        if (type.isInterface()) {
            if (implName == null) {
                implClass = config.getImplClass(type);
            } else {
                implClass = config.getImplClass(type, implName);
            }
        } else {
            implClass = type;
        }

        return implClass.getDeclaredConstructor().newInstance();
    }

}
