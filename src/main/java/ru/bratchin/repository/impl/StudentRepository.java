package ru.bratchin.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bratchin.entity.Student;
import ru.bratchin.exception.student.StudentDeleteException;
import ru.bratchin.exception.student.StudentNotFoundException;
import ru.bratchin.exception.student.StudentSaveException;
import ru.bratchin.exception.student.StudentUpdateException;
import ru.bratchin.repository.api.StudentRepositoryApi;
import ru.bratchin.util.DataSource;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class StudentRepository implements StudentRepositoryApi {

    private static final Logger logger = LoggerFactory.getLogger(StudentRepository.class);

    private static final String FIND_ALL_QUERY = "SELECT * FROM student ORDER BY last_name, first_name";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM student WHERE id = ?";
    private static final String SAVE_QUERY = "INSERT INTO student (id, first_name, last_name, course, admission_date, date_of_graduation, faculty_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE student SET first_name = ?, last_name = ?, course = ?, " +
            "admission_date = ?, date_of_graduation = ?, faculty_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM student WHERE id = ?";

    @Override
    public List<Student> findAll() {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_ALL_QUERY);
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
        try (Connection connection = DataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Student student = mapResultSetToStudent(rs);
                    return Optional.of(student);
                } else {
                    throw new StudentNotFoundException("Student with ID " + id + " not found");
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding student by ID: " + id, e);
            throw new StudentNotFoundException("Error finding student by ID: " + id, e);
        }
    }

    @Override
    public void save(Student student) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SAVE_QUERY)) {
            stmt.setObject(1, UUID.randomUUID());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setInt(4, student.getCourse());
            stmt.setDate(5, Date.valueOf(student.getAdmissionDate()));
            stmt.setDate(6, student.getDateOfGraduation() != null ? Date.valueOf(student.getDateOfGraduation()) : null);
            stmt.setObject(7, student.getFacultyId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error saving student", e);
            throw new StudentSaveException("Failed to save student", e);
        }
    }

    @Override
    public void update(Student student) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_QUERY)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setInt(3, student.getCourse());
            stmt.setDate(4, Date.valueOf(student.getAdmissionDate()));
            stmt.setDate(5, student.getDateOfGraduation() != null ? Date.valueOf(student.getDateOfGraduation()) : null);
            stmt.setObject(6, student.getFacultyId());
            stmt.setObject(7, student.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating student", e);
            throw new StudentUpdateException("Failed to update student", e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE_QUERY)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting student by ID: " + id, e);
            throw new StudentDeleteException("Failed to delete student", e);
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
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
