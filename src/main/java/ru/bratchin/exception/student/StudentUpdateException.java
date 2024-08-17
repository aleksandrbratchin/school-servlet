package ru.bratchin.exception.student;

public class StudentUpdateException extends RuntimeException {
    public StudentUpdateException(String message) {
        super(message);
    }

    public StudentUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}