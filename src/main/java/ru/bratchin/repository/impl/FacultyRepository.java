package ru.bratchin.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bratchin.entity.Faculty;
import ru.bratchin.entity.Student;
import ru.bratchin.repository.api.FacultyRepositoryApi;
import ru.bratchin.util.DatabaseConnectionManager;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;


public class FacultyRepository implements FacultyRepositoryApi {

    private static final Logger logger = LoggerFactory.getLogger(FacultyRepository.class);
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
                UUID facultyId = (UUID) rs.getObject("id");

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

                UUID studentId = (UUID) rs.getObject("student_id");
                if (studentId != null) {
                    Student student = mapResultSetToStudent(rs);
                    faculty.getStudents().add(student);
                }
            }
            return new ArrayList<>(facultyMap.values());
        } catch (SQLException e) {
            logger.error("Error finding all faculties", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Faculty> findById(UUID id) {
        String query = "SELECT f.*, s.id AS student_id, " +
                "s.first_name, s.last_name, s.course, s.admission_date, s.date_of_graduation, s.faculty_id " +
                "FROM faculty f LEFT JOIN student s ON f.id = s.faculty_id " +
                "WHERE f.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                Faculty faculty = null;
                while (rs.next()) {
                    if (faculty == null) {
                        faculty = new Faculty();
                        faculty.setId((UUID) rs.getObject("id"));
                        faculty.setName(rs.getString("name"));
                        faculty.setDescription(rs.getString("description"));
                        faculty.setStudents(new ArrayList<>());
                    }

                    UUID studentId = (UUID) rs.getObject("student_id");
                    if (studentId != null) {
                        Student student = mapResultSetToStudent(rs);
                        faculty.getStudents().add(student);
                    }
                }
                return Optional.ofNullable(faculty);
            }
        } catch (SQLException e) {
            logger.error("Error finding faculty by ID: " + id, e);
            return Optional.empty();
        }
    }

    @Override
    public void save(Faculty faculty) {
        String query = "INSERT INTO faculty (id, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            stmt.setObject(1, UUID.randomUUID());
            stmt.setString(2, faculty.getName());
            stmt.setString(3, faculty.getDescription());
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error saving faculty", e);
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Error during rollback", rollbackEx);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Error resetting auto commit", e);
            }
        }
    }

    @Override
    public void update(Faculty faculty) {
        String query = "UPDATE faculty SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            stmt.setString(1, faculty.getName());
            stmt.setString(2, faculty.getDescription());
            stmt.setObject(3, faculty.getId());
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error updating faculty", e);
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Error during rollback", rollbackEx);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Error resetting auto commit", e);
            }
        }
    }

    @Override
    public void deleteById(UUID id) {
        String deleteStudentsQuery = "DELETE FROM student WHERE faculty_id = ?";
        String deleteFacultyQuery = "DELETE FROM faculty WHERE id = ?";
        try (PreparedStatement deleteStudentsStmt = connection.prepareStatement(deleteStudentsQuery);
             PreparedStatement deleteFacultyStmt = connection.prepareStatement(deleteFacultyQuery)) {
            connection.setAutoCommit(false);

            deleteStudentsStmt.setObject(1, id);
            deleteStudentsStmt.executeUpdate();

            deleteFacultyStmt.setObject(1, id);
            deleteFacultyStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            logger.error("Error deleting faculty by ID: " + id, e);
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Error during rollback", rollbackEx);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Error resetting auto commit", e);
            }
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("student_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        int course = rs.getInt("course");
        LocalDate admissionDate = rs.getDate("admission_date").toLocalDate();
        Date dateOfGraduation = rs.getDate("date_of_graduation");
        LocalDate graduationDate = dateOfGraduation != null ? dateOfGraduation.toLocalDate() : null;
        UUID facultyId = (UUID) rs.getObject("faculty_id");

        return new Student(id, firstName, lastName, course, admissionDate, graduationDate, facultyId);
    }
}
