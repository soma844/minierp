package de.hafni.minierp.domain.auftrag;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hafni.minierp.domain.konfiguration.TechnischerStatus;
import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;
import de.hafni.minierp.exception.GeschaeftsregelException;

public class Auftrag {

    private final String auftragsNummer;
    private final Kunde kunde;

    private final List<AuftragsPosition> positionen =
            new ArrayList<>();

    private AuftragsStatus status;

    public Auftrag(
            String auftragsNummer,
            Kunde kunde) {

        if (auftragsNummer == null
                || auftragsNummer.isBlank()) {

            throw new DomainValidationException(
                    FehlerCode.AUFTRAG_NUMMER_FEHLT,
                    "Auftragsnummer darf nicht leer sein.");
        }

        if (kunde == null) {
            throw new DomainValidationException(
                    FehlerCode.AUFTRAG_KUNDE_FEHLT,
                    "Ein Auftrag benoetigt einen Kunden.");
        }

        this.auftragsNummer = auftragsNummer;
        this.kunde = kunde;
        this.status = AuftragsStatus.ENTWURF;
    }
    private Auftrag(
            String auftragsNummer,
            Kunde kunde,
            AuftragsStatus status) {

        this(auftragsNummer, kunde);

        if (status == null) {
            throw new IllegalArgumentException(
                    "Auftragsstatus darf nicht null sein."
            );
        }

        this.status = status;
    }

    public static Auftrag rehydrieren(
            String auftragsNummer,
            Kunde kunde,
            List<AuftragsPosition> positionen,
            AuftragsStatus status) {

        Auftrag auftrag =
                new Auftrag(
                        auftragsNummer,
                        kunde,
                        status
                );

        if (positionen != null) {
            positionen.forEach(
                    auftrag::fuegePositionHinzu
            );
        }

        return auftrag;
    }

    public void fuegePositionHinzu(
            AuftragsPosition position) {

        if (position == null) {
            throw new IllegalArgumentException(
                    "Auftragsposition darf nicht null sein.");
        }

        positionen.add(position);
    }

    public BigDecimal berechneGesamtHt() {

        return positionen.stream()
                .map(AuftragsPosition::berechneGesamtHt)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add);
    }

    public void freigeben() {

        if (positionen.isEmpty()) {
            throw new GeschaeftsregelException(
                    FehlerCode.AUFTRAG_KEINE_POSITIONEN,
                    "Ein Auftrag ohne Positionen kann nicht freigegeben werden.");
        }

        boolean alleKonfigurationenFreigegeben =
                positionen.stream()
                        .map(AuftragsPosition::getKonfiguration)
                        .allMatch(konfiguration ->
                                konfiguration.getTechnischerStatus()
                                        == TechnischerStatus.FREIGEGEBEN);

        if (!alleKonfigurationenFreigegeben) {
            throw new GeschaeftsregelException(
                    FehlerCode.AUFTRAG_TECHNISCH_NICHT_FREIGEGEBEN,
                    "Alle technischen Konfigurationen muessen freigegeben sein.");
        }

        status = AuftragsStatus.FREIGEGEBEN;
    }

    public String getAuftragsNummer() {
        return auftragsNummer;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public List<AuftragsPosition> getPositionen() {
        return Collections.unmodifiableList(positionen);
    }

    public AuftragsStatus getStatus() {
        return status;
    }
}