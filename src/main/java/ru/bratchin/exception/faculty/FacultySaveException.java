package ru.bratchin.exception.faculty;

public class FacultySaveException extends RuntimeException {
    public FacultySaveException(String message) {
        super(message);
    }

    public FacultySaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
