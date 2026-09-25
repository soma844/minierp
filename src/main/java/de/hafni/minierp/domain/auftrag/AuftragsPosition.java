package de.hafni.minierp.domain.auftrag;

import java.math.BigDecimal;
import java.math.RoundingMode;

import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;

public class AuftragsPosition {

    private final int positionsNummer;
    private final int menge;

    private final BigDecimal einzelPreisHt;
    private final BigDecimal rabattProzent;

    private final LichtkuppelKonfiguration konfiguration;

    public AuftragsPosition(
            int positionsNummer,
            int menge,
            BigDecimal einzelPreisHt,
            BigDecimal rabattProzent,
            LichtkuppelKonfiguration konfiguration) {

        if (positionsNummer <= 0) {
            throw new DomainValidationException(
                    FehlerCode.POSITION_NUMMER_UNGUELTIG,
                    "Positionsnummer muss groesser als 0 sein.");
        }

        if (menge <= 0) {
            throw new DomainValidationException(
                    FehlerCode.POSITION_MENGE_UNGUELTIG,
                    "Menge muss groesser als 0 sein.");
        }

        if (einzelPreisHt == null
                || einzelPreisHt.signum() < 0) {

            throw new DomainValidationException(
                    FehlerCode.POSITION_PREIS_UNGUELTIG,
                    "Einzelpreis darf nicht negativ sein.");
        }

        if (rabattProzent == null
                || rabattProzent.signum() < 0
                || rabattProzent.compareTo(
                        BigDecimal.valueOf(100)) > 0) {

            throw new DomainValidationException(
                    FehlerCode.POSITION_RABATT_UNGUELTIG,
                    "Rabatt muss zwischen 0 und 100 Prozent liegen.");
        }

        if (konfiguration == null) {
            throw new DomainValidationException(
                    FehlerCode.POSITION_KONFIGURATION_FEHLT,
                    "Lichtkuppelkonfiguration darf nicht fehlen.");
        }

        this.positionsNummer = positionsNummer;
        this.menge = menge;
        this.einzelPreisHt = einzelPreisHt;
        this.rabattProzent = rabattProzent;
        this.konfiguration = konfiguration;
    }

    public BigDecimal berechneBruttoHt() {

        return einzelPreisHt
                .multiply(BigDecimal.valueOf(menge))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal berechneRabattBetrag() {

        return berechneBruttoHt()
                .multiply(rabattProzent)
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP);
    }

    public BigDecimal berechneGesamtHt() {

        return berechneBruttoHt()
                .subtract(berechneRabattBetrag())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public int getPositionsNummer() {
        return positionsNummer;
    }

    public int getMenge() {
        return menge;
    }

    public BigDecimal getEinzelPreisHt() {
        return einzelPreisHt;
    }

    public BigDecimal getRabattProzent() {
        return rabattProzent;
    }

    public LichtkuppelKonfiguration getKonfiguration() {
        return konfiguration;
    }
}