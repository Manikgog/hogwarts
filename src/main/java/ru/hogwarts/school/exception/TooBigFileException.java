package ru.hogwarts.school.exception;

public class TooBigFileException extends RuntimeException {
    private String message;
    public TooBigFileException() {
    }
    public TooBigFileException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage() {
        return "Файл " + message + " слишком большой";
    }
}
