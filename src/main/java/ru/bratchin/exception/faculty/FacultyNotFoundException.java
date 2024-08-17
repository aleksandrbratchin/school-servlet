package ru.bratchin.exception.faculty;

public class FacultyNotFoundException extends RuntimeException {
    public FacultyNotFoundException(String message) {
        super(message);
    }

    public FacultyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
