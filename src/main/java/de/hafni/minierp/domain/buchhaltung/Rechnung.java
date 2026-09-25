package de.hafni.minierp.domain.buchhaltung;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hafni.minierp.domain.auftrag.Auftrag;
import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;
import de.hafni.minierp.exception.GeschaeftsregelException;

public class Rechnung {

    private final String rechnungsNummer;
    private final Auftrag auftrag;

    private final LocalDate rechnungsDatum;
    private final LocalDate faelligkeitsDatum;

    private final TvaSatz tvaSatz;

    private final List<Zahlung> zahlungen =
            new ArrayList<>();

    private RechnungsStatus status;

    public Rechnung(
            String rechnungsNummer,
            Auftrag auftrag,
            LocalDate rechnungsDatum,
            LocalDate faelligkeitsDatum,
            TvaSatz tvaSatz) {

        if (rechnungsNummer == null
                || rechnungsNummer.isBlank()) {

            throw new DomainValidationException(
                    FehlerCode.RECHNUNG_NUMMER_FEHLT,
                    "Rechnungsnummer darf nicht leer sein.");
        }

        if (auftrag == null) {
            throw new DomainValidationException(
                    FehlerCode.RECHNUNG_AUFTRAG_FEHLT,
                    "Eine Rechnung benoetigt einen Auftrag.");
        }

        if (rechnungsDatum == null) {
            throw new DomainValidationException(
                    FehlerCode.RECHNUNG_DATUM_FEHLT,
                    "Rechnungsdatum darf nicht null sein.");
        }

        if (faelligkeitsDatum == null
                || faelligkeitsDatum.isBefore(rechnungsDatum)) {

            throw new DomainValidationException(
                    FehlerCode.RECHNUNG_FAELLIGKEIT_UNGUELTIG,
                    "Faelligkeitsdatum darf nicht vor dem Rechnungsdatum liegen.");
        }

        if (tvaSatz == null) {
            throw new DomainValidationException(
                    FehlerCode.RECHNUNG_TVA_FEHLT,
                    "TVA-Satz darf nicht null sein.");
        }

        this.rechnungsNummer = rechnungsNummer;
        this.auftrag = auftrag;
        this.rechnungsDatum = rechnungsDatum;
        this.faelligkeitsDatum = faelligkeitsDatum;
        this.tvaSatz = tvaSatz;

        this.status = RechnungsStatus.OFFEN;
    }

    public BigDecimal berechneGesamtHt() {
        return auftrag
                .berechneGesamtHt()
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal berechneTva() {

        return berechneGesamtHt()
                .multiply(tvaSatz.getProzent())
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP);
    }

    public BigDecimal berechneGesamtTtc() {

        return berechneGesamtHt()
                .add(berechneTva())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal berechneBezahltenBetrag() {

        return zahlungen.stream()
                .map(Zahlung::getBetrag)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add);
    }

    public BigDecimal berechneRestBetrag() {

        return berechneGesamtTtc()
                .subtract(berechneBezahltenBetrag())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void erfasseZahlung(Zahlung zahlung) {

        if (zahlung == null) {
            throw new IllegalArgumentException(
                    "Zahlung darf nicht null sein.");
        }

        if (zahlung.getBetrag()
                .compareTo(berechneRestBetrag()) > 0) {

            throw new GeschaeftsregelException(
                    FehlerCode.ZAHLUNG_UEBERSTEIGT_RESTBETRAG,
                    "Zahlung darf den offenen Rechnungsbetrag nicht uebersteigen.");
        }

        zahlungen.add(zahlung);

        aktualisiereStatus();
    }

    private void aktualisiereStatus() {

        BigDecimal bezahlt =
                berechneBezahltenBetrag();

        if (bezahlt.signum() == 0) {
            status = RechnungsStatus.OFFEN;
            return;
        }

        if (berechneRestBetrag().signum() == 0) {
            status = RechnungsStatus.BEZAHLT;
        } else {
            status = RechnungsStatus.TEILWEISE_BEZAHLT;
        }
    }

    public String getRechnungsNummer() {
        return rechnungsNummer;
    }

    public Auftrag getAuftrag() {
        return auftrag;
    }

    public LocalDate getRechnungsDatum() {
        return rechnungsDatum;
    }

    public LocalDate getFaelligkeitsDatum() {
        return faelligkeitsDatum;
    }

    public TvaSatz getTvaSatz() {
        return tvaSatz;
    }

    public RechnungsStatus getStatus() {
        return status;
    }

    public List<Zahlung> getZahlungen() {
        return Collections.unmodifiableList(zahlungen);
    }
}