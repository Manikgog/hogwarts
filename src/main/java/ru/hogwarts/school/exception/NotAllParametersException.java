package ru.hogwarts.school.exception;

public class NotAllParametersException extends RuntimeException {
    private final String parameters;
    public NotAllParametersException(String parameters) {
        super();
        this.parameters = parameters;
    }

    @Override
    public String getMessage(){
        return "Не введены параметры: " + parameters;
    }
}
