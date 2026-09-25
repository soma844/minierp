package de.hafni.minierp.domain.auftrag;

import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;

public class Kunde {

    private final String kundenNummer;
    private final String firmenName;

    public Kunde(
            String kundenNummer,
            String firmenName) {

        if (kundenNummer == null || kundenNummer.isBlank()) {
            throw new DomainValidationException(
                    FehlerCode.KUNDE_NUMMER_FEHLT,
                    "Kundennummer darf nicht leer sein.");
        }

        if (firmenName == null || firmenName.isBlank()) {
            throw new DomainValidationException(
                    FehlerCode.KUNDE_NAME_FEHLT,
                    "Firmenname darf nicht leer sein.");
        }

        this.kundenNummer = kundenNummer;
        this.firmenName = firmenName;
    }

    public String getKundenNummer() {
        return kundenNummer;
    }

    public String getFirmenName() {
        return firmenName;
    }
}