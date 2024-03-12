package ru.hogwarts.school.exception;

public class IllegalParameterException extends RuntimeException {
    private final String message;
    public IllegalParameterException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
