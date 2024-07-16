package ru.bratchin.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bratchin.entity.Student;
import ru.bratchin.repository.api.StudentRepositoryApi;
import ru.bratchin.util.DatabaseConnectionManager;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class StudentRepository implements StudentRepositoryApi {

    private static final Logger logger = LoggerFactory.getLogger(StudentRepository.class);
    private final Connection connection = DatabaseConnectionManager.getConnection();

    @Override
    public List<Student> findAll() {
        String query = "SELECT * FROM student ORDER BY last_name, first_name";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            List<Student> students = new ArrayList<>();
            while (rs.next()) {
                Student student = mapResultSetToStudent(rs);
                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            logger.error("Error finding all students", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Student> findById(UUID id) {
        String query = "SELECT * FROM student WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Student student = mapResultSetToStudent(rs);
                    return Optional.of(student);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding student by ID: " + id, e);
            return Optional.empty();
        }
    }

    @Override
    public void save(Student student) {
        String query = "INSERT INTO student (id, first_name, last_name, course, admission_date, date_of_graduation, faculty_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            stmt.setObject(1, UUID.randomUUID());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setInt(4, student.getCourse());
            stmt.setDate(5, Date.valueOf(student.getAdmissionDate()));
            stmt.setDate(6, student.getDateOfGraduation() != null ? Date.valueOf(student.getDateOfGraduation()) : null);
            stmt.setObject(7, student.getFacultyId());
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error saving student", e);
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
    public void update(Student student) {
        String query = "UPDATE student SET first_name = ?, last_name = ?, course = ?, " +
                "admission_date = ?, date_of_graduation = ?, faculty_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setInt(3, student.getCourse());
            stmt.setDate(4, Date.valueOf(student.getAdmissionDate()));
            stmt.setDate(5, student.getDateOfGraduation() != null ? Date.valueOf(student.getDateOfGraduation()) : null);
            stmt.setObject(6, student.getFacultyId());
            stmt.setObject(7, student.getId());
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error updating student", e);
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
        String query = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            stmt.setObject(1, id);
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error deleting student by ID: " + id, e);
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

    private static Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
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
