package ru.bratchin.repository.impl;

import ru.bratchin.entity.Faculty;
import ru.bratchin.entity.Student;
import ru.bratchin.repository.api.FacultyRepositoryApi;
import ru.bratchin.util.DatabaseConnectionManager;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;


public class FacultyRepository implements FacultyRepositoryApi {

    private final Connection connection = DatabaseConnectionManager.getConnection();

    @Override
    public List<Faculty> findAll() {
        String query = "SELECT f.*, s.id AS student_id, " +
                "s.first_name, s.last_name, s.course, s.admission_date, s.date_of_graduation, s.faculty_id " +
                "FROM faculty f LEFT JOIN student s ON f.id = s.faculty_id ORDER BY f.name";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            Map<UUID, Faculty> facultyMap = new LinkedHashMap<>();
            while (rs.next()) {
                UUID facultyId = UUID.fromString(rs.getString("id"));

                Faculty faculty = facultyMap.computeIfAbsent(facultyId, id -> {
                    try {
                        Faculty f = new Faculty();
                        f.setId(facultyId);
                        f.setName(rs.getString("name"));
                        f.setDescription(rs.getString("description"));
                        f.setStudents(new ArrayList<>());
                        return f;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                UUID studentId = Optional.ofNullable(rs.getString("student_id"))
                        .map(UUID::fromString).orElse(null);
                if (studentId != null) {
                    Student student = mapResultSetToStudent(rs);
                    faculty.getStudents().add(student);
                }

            }
            return new ArrayList<>(facultyMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Faculty> findById(UUID id) {
        String query = "SELECT f.*, s.id AS student_id, " +
                "s.first_name, s.last_name, s.course, s.admission_date, s.date_of_graduation, s.faculty_id " +
                "FROM faculty f LEFT JOIN student s ON CAST(f.id AS text) = CAST(s.faculty_id AS text) " +
                "WHERE CAST(f.id AS text) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                Faculty faculty = null;
                while (rs.next()) {
                    if (faculty == null) {
                        faculty = new Faculty();
                        faculty.setId(UUID.fromString(rs.getString("id")));
                        faculty.setName(rs.getString("name"));
                        faculty.setDescription(rs.getString("description"));
                        faculty.setStudents(new ArrayList<>());
                    }

                    UUID studentId = Optional.ofNullable(rs.getString("student_id"))
                            .map(UUID::fromString).orElse(null);
                    if (studentId != null) {
                        Student student = mapResultSetToStudent(rs);
                        faculty.getStudents().add(student);
                    }
                }
                return Optional.ofNullable(faculty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void save(Faculty faculty) {
        String query = "INSERT INTO faculty (id, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, UUID.randomUUID());
            stmt.setString(2, faculty.getName());
            stmt.setString(3, faculty.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Faculty faculty) {
        String query = "UPDATE faculty SET name = ?, description = ? WHERE CAST(id AS text) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, faculty.getName());
            stmt.setString(2, faculty.getDescription());
            stmt.setString(3, faculty.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(UUID id) {
        String deleteStudentsQuery = "DELETE FROM student WHERE CAST(faculty_id AS text) = ?";
        String deleteFacultyQuery = "DELETE FROM faculty WHERE CAST(id AS text) = ?";
        try (PreparedStatement deleteStudentsStmt = connection.prepareStatement(deleteStudentsQuery);
             PreparedStatement deleteFacultyStmt = connection.prepareStatement(deleteFacultyQuery)) {
            connection.setAutoCommit(false);

            deleteStudentsStmt.setString(1, id.toString());
            deleteStudentsStmt.executeUpdate();

            deleteFacultyStmt.setString(1, id.toString());
            deleteFacultyStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("student_id"));
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        int course = rs.getInt("course");
        LocalDate admissionDate = rs.getDate("admission_date").toLocalDate();
        Date dateOfGraduation = rs.getDate("date_of_graduation");
        LocalDate graduationDate = dateOfGraduation != null ? dateOfGraduation.toLocalDate() : null;
        UUID facultyId = UUID.fromString(rs.getString("faculty_id"));

        return new Student(id, firstName, lastName, course, admissionDate, graduationDate, facultyId);
    }

}
