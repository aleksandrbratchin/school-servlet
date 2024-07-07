package ru.bratchin.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bratchin.util.ObjectFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import ru.bratchin.dto.FacultyDto;
import ru.bratchin.entity.Faculty;
import ru.bratchin.mapper.Mapper;
import ru.bratchin.service.api.FacultyServiceApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/faculty/*")
public class FacultyServlet extends HttpServlet {
    private final FacultyServiceApi service = ObjectFactory.getInstance().createObject(FacultyServiceApi.class);
    private final Mapper<Faculty, FacultyDto> facultyMapper = ObjectFactory.getInstance().createObject(Mapper.class, "facultyMapper");
    private final ObjectMapper objectMapper = ObjectFactory.getInstance().createObject(ObjectMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || "/".equals(pathInfo)) { //find all
                List<Faculty> faculties = service.getAll();
                resp.setContentType("application/json");
                String json = objectMapper.writeValueAsString(faculties.stream().map(facultyMapper::toDto).toList());
                resp.getWriter().write(json);
            } else { //find by id
                UUID id = UUID.fromString(pathInfo.substring(1));
                Faculty faculty = service.getById(id);
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(facultyMapper.toDto(faculty)));
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
            FacultyDto facultyDto = objectMapper.readValue(jsonBody, FacultyDto.class);

            // Преобразование DTO в сущность Faculty
            Faculty faculty = facultyMapper.toEntity(facultyDto);

            // Сохранение факультета
            service.save(faculty);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(facultyMapper.toDto(faculty)));
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
            FacultyDto facultyDto = objectMapper.readValue(jsonBody, FacultyDto.class);

            // Преобразование DTO в сущность Faculty
            Faculty faculty = facultyMapper.toEntity(facultyDto);

            // Сохранение факультета
            service.update(faculty);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(facultyMapper.toDto(faculty)));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
