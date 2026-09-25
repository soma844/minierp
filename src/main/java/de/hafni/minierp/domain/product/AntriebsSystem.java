package de.hafni.minierp.domain.product;

import java.math.BigDecimal;
import java.util.List;

import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;

public class AntriebsSystem {

    private final List<Antrieb> antriebe;
    private final Steuerung steuerung;

    public AntriebsSystem(
            List<Antrieb> antriebe,
            Steuerung steuerung) {

        if (antriebe == null || antriebe.isEmpty()) {
            throw new DomainValidationException(
                    FehlerCode.ANTRIEBSSYSTEM_ANTRIEBE_FEHLEN,
                    "Mindestens ein Antrieb muss vorhanden sein.");
        }

        if (antriebe.size() > 2) {
            throw new DomainValidationException(
                    FehlerCode.ANTRIEBSSYSTEM_ANZAHL_UNGUELTIG,
                    "Ein Antriebssystem darf maximal zwei Antriebe enthalten.");
        }

        this.antriebe = List.copyOf(antriebe);
        this.steuerung = steuerung;
    }

    public BigDecimal berechneGesamtStrom() {

        return antriebe.stream()
                .map(Antrieb::getNennStromAmpere)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add);
    }

    public boolean istSteuerungAusreichend() {

        if (steuerung == null) {
            return false;
        }

        return steuerung
                .getMaximalerAusgangsStromAmpere()
                .compareTo(berechneGesamtStrom()) >= 0;
    }

    public List<Antrieb> getAntriebe() {
        return antriebe;
    }

    public Steuerung getSteuerung() {
        return steuerung;
    }
}