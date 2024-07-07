package ru.bratchin.repository.impl;

import ru.bratchin.config.DatabaseConnectionManager;
import ru.bratchin.entity.Faculty;
import ru.bratchin.repository.api.FacultyRepositoryApi;

import java.sql.*;
import java.util.*;

public class FacultyRepository implements FacultyRepositoryApi {

    private final Connection connection = DatabaseConnectionManager.getConnection();

    @Override
    public List<Faculty> findAll() throws SQLException {
        String query = "SELECT * FROM faculty ORDER BY name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            List<Faculty> faculties = new ArrayList<>();
            while (rs.next()) {
                Faculty faculty = new Faculty();
                faculty.setId(UUID.fromString(rs.getString("id")));
                faculty.setName(rs.getString("name"));
                faculty.setDescription(rs.getString("description"));
                faculties.add(faculty);
            }
            return faculties;
        }
    }

    @Override
    public Optional<Faculty> findById(UUID id) throws SQLException {
        String query = "SELECT * FROM faculty WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Faculty faculty = new Faculty();
                    faculty.setId(UUID.fromString(rs.getString("id")));
                    faculty.setName(rs.getString("name"));
                    faculty.setDescription(rs.getString("description"));
                    return Optional.of(faculty);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void save(Faculty faculty) throws SQLException {
        String query = "INSERT INTO faculty (id, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, faculty.getId().toString());
            stmt.setString(2, faculty.getName());
            stmt.setString(3, faculty.getDescription());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Faculty faculty) throws SQLException {
        String query = "UPDATE faculty SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, faculty.getName());
            stmt.setString(2, faculty.getDescription());
            stmt.setString(3, faculty.getId().toString());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(UUID id) throws SQLException {
        String query = "DELETE FROM faculty WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        }
    }

}
