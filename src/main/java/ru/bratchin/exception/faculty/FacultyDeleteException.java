package ru.bratchin.exception.faculty;

public class FacultyDeleteException extends RuntimeException {
    public FacultyDeleteException(String message) {
        super(message);
    }

    public FacultyDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
