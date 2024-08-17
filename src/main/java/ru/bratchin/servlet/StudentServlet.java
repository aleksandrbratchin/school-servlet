package ru.bratchin.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.bratchin.dto.StudentDto;
import ru.bratchin.service.api.StudentServiceApi;
import ru.bratchin.util.ObjectFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/student/*")
@RequiredArgsConstructor
public class StudentServlet extends HttpServlet {

    private final StudentServiceApi service;

    private final ObjectMapper objectMapper;

    public StudentServlet() {
        service = ObjectFactory.getInstance().createObject(StudentServiceApi.class);
        objectMapper = ObjectFactory.getInstance().createObject(ObjectMapper.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || "/".equals(pathInfo)) {
                List<StudentDto> students = service.getAll();
                resp.setContentType("application/json");
                String json = objectMapper.writeValueAsString(students);
                resp.getWriter().write(json);
            } else {
                UUID id = UUID.fromString(pathInfo.substring(1));
                StudentDto student = service.getById(id);
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(student));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String jsonBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            StudentDto studentDto = objectMapper.readValue(jsonBody, StudentDto.class);

            service.save(studentDto);

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String jsonBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            StudentDto studentDto = objectMapper.readValue(jsonBody, StudentDto.class);

            service.update(studentDto);

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
