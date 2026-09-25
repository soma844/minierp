package de.hafni.minierp.domain.product;

import java.math.BigDecimal;

import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;

public class Fuellung {

    private final FuellungsArt art;
    private final FuellungsAusfuehrung ausfuehrung;
    private final int dickeMm;

    private final BigDecimal ugWert;
    private final BigDecimal lichtTransmissionProzent;
    private final BigDecimal solarFaktorProzent;

    public Fuellung(
            FuellungsArt art,
            FuellungsAusfuehrung ausfuehrung,
            int dickeMm,
            BigDecimal ugWert,
            BigDecimal lichtTransmissionProzent,
            BigDecimal solarFaktorProzent) {

        if (art == null) {
            throw new DomainValidationException(
                    FehlerCode.FUELLUNG_ART_FEHLT,
                    "Fuellungsart darf nicht null sein.");
        }

        if (ausfuehrung == null) {
            throw new DomainValidationException(
                    FehlerCode.FUELLUNG_AUSFUEHRUNG_FEHLT,
                    "Fuellungsausfuehrung darf nicht null sein.");
        }

        if (dickeMm <= 0) {
            throw new DomainValidationException(
                    FehlerCode.FUELLUNG_DICKE_UNGUELTIG,
                    "Fuellungsdicke muss groesser als 0 sein.");
        }

        if (ugWert == null || ugWert.signum() <= 0) {
            throw new DomainValidationException(
                    FehlerCode.FUELLUNG_UG_WERT_UNGUELTIG,
                    "Ug-Wert muss groesser als 0 sein.");
        }

        if (lichtTransmissionProzent == null
                || lichtTransmissionProzent.signum() < 0
                || lichtTransmissionProzent.compareTo(BigDecimal.valueOf(100)) > 0) {

            throw new DomainValidationException(
                    FehlerCode.FUELLUNG_LICHTTRANSMISSION_UNGUELTIG,
                    "Lichttransmission muss zwischen 0 und 100 Prozent liegen.");
        }

        if (solarFaktorProzent == null
                || solarFaktorProzent.signum() < 0
                || solarFaktorProzent.compareTo(BigDecimal.valueOf(100)) > 0) {

            throw new DomainValidationException(
                    FehlerCode.FUELLUNG_SOLARFAKTOR_UNGUELTIG,
                    "Solarfaktor muss zwischen 0 und 100 Prozent liegen.");
        }

        this.art = art;
        this.ausfuehrung = ausfuehrung;
        this.dickeMm = dickeMm;
        this.ugWert = ugWert;
        this.lichtTransmissionProzent = lichtTransmissionProzent;
        this.solarFaktorProzent = solarFaktorProzent;
    }

    public FuellungsArt getArt() {
        return art;
    }

    public FuellungsAusfuehrung getAusfuehrung() {
        return ausfuehrung;
    }

    public int getDickeMm() {
        return dickeMm;
    }

    public BigDecimal getUgWert() {
        return ugWert;
    }

    public BigDecimal getLichtTransmissionProzent() {
        return lichtTransmissionProzent;
    }

    public BigDecimal getSolarFaktorProzent() {
        return solarFaktorProzent;
    }
}