package de.hafni.minierp.exception;

public class RessourceNichtGefundenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RessourceNichtGefundenException(String message) {
        super(message);
    }
}