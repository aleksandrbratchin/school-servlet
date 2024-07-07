package ru.bratchin.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bratchin.util.ObjectFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import ru.bratchin.dto.StudentDto;
import ru.bratchin.entity.Student;
import ru.bratchin.mapper.Mapper;
import ru.bratchin.service.api.StudentServiceApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/student/*")
public class StudentServlet extends HttpServlet {
    private final StudentServiceApi service = ObjectFactory.getInstance().createObject(StudentServiceApi.class);
    private final Mapper<Student, StudentDto> studentMapper = ObjectFactory.getInstance().createObject(Mapper.class, "studentMapper");
    private final ObjectMapper objectMapper = ObjectFactory.getInstance().createObject(ObjectMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || "/".equals(pathInfo)) { //find all
                List<Student> students = service.getAll();
                resp.setContentType("application/json");
                String json = objectMapper.writeValueAsString(students.stream().map(studentMapper::toDto).toList());
                resp.getWriter().write(json);
            } else { //find by id
                UUID id = UUID.fromString(pathInfo.substring(1));
                Student student = service.getById(id);
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(studentMapper.toDto(student)));
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Чтение данных из запроса
            String jsonBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            StudentDto studentDto = objectMapper.readValue(jsonBody, StudentDto.class);

            // Преобразование DTO в сущность Student
            Student student = studentMapper.toEntity(studentDto);

            // Сохранение студента
            service.save(student);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(studentMapper.toDto(student)));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String idStr = req.getPathInfo().substring(1);
        try {
            UUID id = UUID.fromString(idStr);
            service.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Чтение данных из запроса
            String jsonBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            StudentDto studentDto = objectMapper.readValue(jsonBody, StudentDto.class);

            // Преобразование DTO в сущность Student
            Student student = studentMapper.toEntity(studentDto);

            // Сохранение студента
            service.update(student);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(studentMapper.toDto(student)));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
