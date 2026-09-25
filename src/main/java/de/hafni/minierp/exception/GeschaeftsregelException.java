package de.hafni.minierp.exception;

public class GeschaeftsregelException extends MiniErpException {

    private static final long serialVersionUID = 1L;

    public GeschaeftsregelException(
            FehlerCode fehlerCode,
            String message) {

        super(fehlerCode, message);
    }
}