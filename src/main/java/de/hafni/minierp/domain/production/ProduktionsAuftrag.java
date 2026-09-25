package de.hafni.minierp.domain.production;

import java.time.LocalDate;

import de.hafni.minierp.domain.auftrag.Auftrag;
import de.hafni.minierp.domain.auftrag.AuftragsStatus;
import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;
import de.hafni.minierp.exception.GeschaeftsregelException;

public class ProduktionsAuftrag {

    private final String produktionsNummer;
    private final Auftrag auftrag;

    private final LocalDate geplanterStart;

    private ProduktionsStatus status;

    public ProduktionsAuftrag(
            String produktionsNummer,
            Auftrag auftrag,
            LocalDate geplanterStart) {

        if (produktionsNummer == null
                || produktionsNummer.isBlank()) {

            throw new DomainValidationException(
                    FehlerCode.PRODUKTION_NUMMER_FEHLT,
                    "Produktionsnummer darf nicht leer sein.");
        }

        if (auftrag == null) {
            throw new DomainValidationException(
                    FehlerCode.PRODUKTION_AUFTRAG_FEHLT,
                    "Produktionsauftrag benoetigt einen Auftrag.");
        }

        if (auftrag.getStatus()
                != AuftragsStatus.FREIGEGEBEN) {

            throw new GeschaeftsregelException(
                    FehlerCode.PRODUKTION_AUFTRAG_NICHT_FREIGEGEBEN,
                    "Nur ein freigegebener Auftrag darf produziert werden.");
        }

        this.produktionsNummer = produktionsNummer;
        this.auftrag = auftrag;
        this.geplanterStart = geplanterStart;
        this.status = ProduktionsStatus.GEPLANT;
    }
    private ProduktionsAuftrag(
            String produktionsNummer,
            Auftrag auftrag,
            LocalDate geplanterStart,
            ProduktionsStatus status) {

        if (produktionsNummer == null
                || produktionsNummer.isBlank()) {

            throw new DomainValidationException(
                    FehlerCode.PRODUKTION_NUMMER_FEHLT,
                    "Produktionsnummer darf nicht leer sein.");
        }

        if (auftrag == null) {
            throw new DomainValidationException(
                    FehlerCode.PRODUKTION_AUFTRAG_FEHLT,
                    "Produktionsauftrag benoetigt einen Auftrag.");
        }

        if (status == null) {
            throw new IllegalArgumentException(
                    "Produktionsstatus darf nicht null sein.");
        }

        this.produktionsNummer = produktionsNummer;
        this.auftrag = auftrag;
        this.geplanterStart = geplanterStart;
        this.status = status;
    }

    public static ProduktionsAuftrag rehydrieren(
            String produktionsNummer,
            Auftrag auftrag,
            LocalDate geplanterStart,
            ProduktionsStatus status) {

        return new ProduktionsAuftrag(
                produktionsNummer,
                auftrag,
                geplanterStart,
                status
        );
    }

    public void starteProduktion() {

        pruefeStatus(
                ProduktionsStatus.GEPLANT,
                "Produktion kann nur aus GEPLANT gestartet werden.");

        status = ProduktionsStatus.IN_PRODUKTION;
    }

    public void markiereProduziert() {

        pruefeStatus(
                ProduktionsStatus.IN_PRODUKTION,
                "Nur ein Auftrag IN_PRODUKTION kann als produziert markiert werden.");

        status = ProduktionsStatus.PRODUZIERT;
    }

    public void abschliessen() {

        pruefeStatus(
                ProduktionsStatus.PRODUZIERT,
                "Nur ein produzierter Auftrag kann abgeschlossen werden.");

        status = ProduktionsStatus.ABGESCHLOSSEN;
    }

    private void pruefeStatus(
            ProduktionsStatus erwarteterStatus,
            String meldung) {

        if (status != erwarteterStatus) {
            throw new GeschaeftsregelException(
                    FehlerCode.PRODUKTION_STATUSWECHSEL_UNGUELTIG,
                    meldung);
        }
    }

    public String getProduktionsNummer() {
        return produktionsNummer;
    }

    public Auftrag getAuftrag() {
        return auftrag;
    }

    public LocalDate getGeplanterStart() {
        return geplanterStart;
    }

    public ProduktionsStatus getStatus() {
        return status;
    }
}