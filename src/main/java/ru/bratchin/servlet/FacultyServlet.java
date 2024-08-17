package ru.bratchin.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.bratchin.dto.FacultyDto;
import ru.bratchin.service.api.FacultyServiceApi;
import ru.bratchin.util.ObjectFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/faculty/*")
@RequiredArgsConstructor
public class FacultyServlet extends HttpServlet {

    private final FacultyServiceApi service;

    private final ObjectMapper objectMapper;

    public FacultyServlet() {
        this.service = ObjectFactory.getInstance().createObject(FacultyServiceApi.class);
        this.objectMapper = ObjectFactory.getInstance().createObject(ObjectMapper.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || "/".equals(pathInfo)) {
                List<FacultyDto> faculties = service.getAll();
                resp.setContentType("application/json");
                String json = objectMapper.writeValueAsString(faculties);
                resp.getWriter().write(json);
            } else {
                UUID id = UUID.fromString(pathInfo.substring(1));
                FacultyDto faculty = service.getById(id);
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(faculty));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String jsonBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            FacultyDto facultyDto = objectMapper.readValue(jsonBody, FacultyDto.class);

            service.save(facultyDto);

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
            FacultyDto facultyDto = objectMapper.readValue(jsonBody, FacultyDto.class);

            service.update(facultyDto);

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
