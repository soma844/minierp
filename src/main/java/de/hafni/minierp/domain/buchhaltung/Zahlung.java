package de.hafni.minierp.domain.buchhaltung;

import java.math.BigDecimal;
import java.time.LocalDate;

import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;

public class Zahlung {

    private final LocalDate zahlungsDatum;
    private final BigDecimal betrag;

    public Zahlung(
            LocalDate zahlungsDatum,
            BigDecimal betrag) {

        if (zahlungsDatum == null) {
            throw new DomainValidationException(
                    FehlerCode.ZAHLUNG_DATUM_FEHLT,
                    "Zahlungsdatum darf nicht null sein.");
        }

        if (betrag == null || betrag.signum() <= 0) {
            throw new DomainValidationException(
                    FehlerCode.ZAHLUNG_BETRAG_UNGUELTIG,
                    "Zahlungsbetrag muss groesser als 0 sein.");
        }

        this.zahlungsDatum = zahlungsDatum;
        this.betrag = betrag;
    }

    public LocalDate getZahlungsDatum() {
        return zahlungsDatum;
    }

    public BigDecimal getBetrag() {
        return betrag;
    }
}