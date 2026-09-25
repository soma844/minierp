package de.hafni.minierp.exception;

public class DomainValidationException extends MiniErpException {

    private static final long serialVersionUID = 1L;

    public DomainValidationException(
            FehlerCode fehlerCode,
            String message) {

        super(fehlerCode, message);
    }
}