package de.hafni.minierp.domain.product;

import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;

public class ProduktFamilie {

    private final String code;
    private final String bezeichnung;

    public ProduktFamilie(
            String code,
            String bezeichnung) {

        if (code == null || code.isBlank()) {
            throw new DomainValidationException(
                    FehlerCode.PRODUKTFAMILIE_CODE_FEHLT,
                    "Produktfamilien-Code darf nicht leer sein.");
        }

        if (bezeichnung == null || bezeichnung.isBlank()) {
            throw new DomainValidationException(
                    FehlerCode.PRODUKTFAMILIE_BEZEICHNUNG_FEHLT,
                    "Produktfamilien-Bezeichnung darf nicht leer sein.");
        }

        this.code = code;
        this.bezeichnung = bezeichnung;
    }

    public String getCode() {
        return code;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
}