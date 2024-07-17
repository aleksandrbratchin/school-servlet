package ru.bratchin.exception.student;

public class StudentSaveException extends RuntimeException {
    public StudentSaveException(String message) {
        super(message);
    }

    public StudentSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}