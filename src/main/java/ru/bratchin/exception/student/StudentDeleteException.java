package ru.bratchin.exception.student;

public class StudentDeleteException extends RuntimeException {
    public StudentDeleteException(String message) {
        super(message);
    }

    public StudentDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}