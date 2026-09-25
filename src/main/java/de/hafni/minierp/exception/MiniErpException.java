package de.hafni.minierp.exception;

public abstract class MiniErpException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final FehlerCode fehlerCode;

    protected MiniErpException(
            FehlerCode fehlerCode,
            String message) {

        super(message);
        this.fehlerCode = fehlerCode;
    }

    public FehlerCode getFehlerCode() {
        return fehlerCode;
    }
}