package ru.bratchin.exception.faculty;

public class FacultyUpdateException extends RuntimeException {
    public FacultyUpdateException(String message) {
        super(message);
    }

    public FacultyUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
