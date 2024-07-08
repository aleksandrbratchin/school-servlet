package ru.bratchin.repository.impl;

import ru.bratchin.util.DatabaseConnectionManager;
import ru.bratchin.entity.Student;
import ru.bratchin.repository.api.StudentRepositoryApi;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StudentRepository implements StudentRepositoryApi {

    private final Connection connection = DatabaseConnectionManager.getConnection();

    @Override
    public List<Student> findAll() throws SQLException {
        String query = "SELECT * FROM student ORDER BY last_name, first_name";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery(query)) {
            List<Student> students = new ArrayList<>();
            while (rs.next()) {
                Student student = mapResultSetToStudent(rs);
                students.add(student);
            }
            return students;
        }
    }

    @Override
    public Optional<Student> findById(UUID id) throws SQLException {
        String query = "SELECT * FROM student WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Student student = mapResultSetToStudent(rs);
                    return Optional.of(student);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void save(Student student) throws SQLException {
        String query = "INSERT INTO student (id, first_name, last_name, course, admission_date, date_of_graduation, faculty_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getId().toString());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setInt(4, student.getCourse());
            stmt.setDate(5, Date.valueOf(student.getAdmissionDate()));
            stmt.setDate(6, student.getDateOfGraduation() != null ? Date.valueOf(student.getDateOfGraduation()) : null);
            stmt.setString(7, student.getFacultyId().toString());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Student student) throws SQLException {
        String query = "UPDATE student SET first_name = ?, last_name = ?, course = ?, " +
                "admission_date = ?, date_of_graduation = ?, faculty_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setInt(3, student.getCourse());
            stmt.setDate(4, Date.valueOf(student.getAdmissionDate()));
            stmt.setDate(5, student.getDateOfGraduation() != null ? Date.valueOf(student.getDateOfGraduation()) : null);
            stmt.setString(6, student.getFacultyId().toString());
            stmt.setString(7, student.getId().toString());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(UUID id) throws SQLException {
        String query = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
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
