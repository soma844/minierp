package de.hafni.minierp.domain.controlling;

import java.math.BigDecimal;
import java.math.RoundingMode;

import de.hafni.minierp.domain.auftrag.Auftrag;
import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;

public class KostenKalkulation {

    private final Auftrag auftrag;

    private final BigDecimal materialKosten;
    private final BigDecimal produktionsKosten;

    public KostenKalkulation(
            Auftrag auftrag,
            BigDecimal materialKosten,
            BigDecimal produktionsKosten) {

        if (auftrag == null) {
            throw new DomainValidationException(
                    FehlerCode.KALKULATION_AUFTRAG_FEHLT,
                    "Auftrag darf nicht null sein.");
        }

        if (materialKosten == null
                || materialKosten.signum() < 0) {

            throw new DomainValidationException(
                    FehlerCode.KALKULATION_MATERIALKOSTEN_UNGUELTIG,
                    "Materialkosten duerfen nicht negativ sein.");
        }

        if (produktionsKosten == null
                || produktionsKosten.signum() < 0) {

            throw new DomainValidationException(
                    FehlerCode.KALKULATION_PRODUKTIONSKOSTEN_UNGUELTIG,
                    "Produktionskosten duerfen nicht negativ sein.");
        }

        this.auftrag = auftrag;
        this.materialKosten = materialKosten;
        this.produktionsKosten = produktionsKosten;
    }

    public BigDecimal berechneGesamtKosten() {
        return materialKosten
                .add(produktionsKosten)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal berechneDeckungsBeitrag() {

        return auftrag
                .berechneGesamtHt()
                .subtract(berechneGesamtKosten())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal berechneMargeProzent() {

        BigDecimal verkaufsPreis =
                auftrag.berechneGesamtHt();

        if (verkaufsPreis.signum() == 0) {
            return BigDecimal.ZERO;
        }

        return berechneDeckungsBeitrag()
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        verkaufsPreis,
                        2,
                        RoundingMode.HALF_UP);
    }

    public Auftrag getAuftrag() {
        return auftrag;
    }

    public BigDecimal getMaterialKosten() {
        return materialKosten;
    }

    public BigDecimal getProduktionsKosten() {
        return produktionsKosten;
    }
}