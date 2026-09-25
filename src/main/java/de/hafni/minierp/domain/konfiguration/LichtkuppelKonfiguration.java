package de.hafni.minierp.domain.konfiguration;

import de.hafni.minierp.domain.product.AntriebsSystem;
import de.hafni.minierp.domain.product.Aufsetzkranz;
import de.hafni.minierp.domain.product.FesterRahmen;
import de.hafni.minierp.domain.product.Fuellung;
import de.hafni.minierp.domain.product.OeffnungsRahmen;
import de.hafni.minierp.domain.product.ProduktFamilie;
import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;

public class LichtkuppelKonfiguration {

    private final String konfigurationsNummer;

    private final ProduktFamilie produktFamilie;
    private final LichtkuppelFunktion funktion;

    private final ProjektAnforderungen projektAnforderungen;

    private final Aufsetzkranz aufsetzkranz;
    private final FesterRahmen festerRahmen;
    private final Fuellung fuellung;

    private final OeffnungsRahmen oeffnungsRahmen;
    private final AntriebsSystem antriebsSystem;

    private TechnischerStatus technischerStatus;

    public LichtkuppelKonfiguration(
            String konfigurationsNummer,
            ProduktFamilie produktFamilie,
            LichtkuppelFunktion funktion,
            ProjektAnforderungen projektAnforderungen,
            Aufsetzkranz aufsetzkranz,
            FesterRahmen festerRahmen,
            Fuellung fuellung,
            OeffnungsRahmen oeffnungsRahmen,
            AntriebsSystem antriebsSystem) {

        if (konfigurationsNummer == null
                || konfigurationsNummer.isBlank()) {

            throw new DomainValidationException(
                    FehlerCode.KONFIGURATION_NUMMER_FEHLT,
                    "Konfigurationsnummer darf nicht leer sein.");
        }

        if (produktFamilie == null) {
            throw new DomainValidationException(
                    FehlerCode.KONFIGURATION_PRODUKTFAMILIE_FEHLT,
                    "Produktfamilie darf nicht null sein.");
        }

        if (funktion == null) {
            throw new DomainValidationException(
                    FehlerCode.KONFIGURATION_FUNKTION_FEHLT,
                    "Lichtkuppelfunktion darf nicht null sein.");
        }

        if (projektAnforderungen == null) {
            throw new DomainValidationException(
                    FehlerCode.KONFIGURATION_PROJEKTANFORDERUNGEN_FEHLEN,
                    "Projektanforderungen duerfen nicht null sein.");
        }

        if (aufsetzkranz == null) {
            throw new DomainValidationException(
                    FehlerCode.KONFIGURATION_AUFSETZKRANZ_FEHLT,
                    "Aufsetzkranz darf nicht null sein.");
        }

        if (festerRahmen == null) {
            throw new DomainValidationException(
                    FehlerCode.KONFIGURATION_FESTER_RAHMEN_FEHLT,
                    "Fester Rahmen darf nicht null sein.");
        }

        if (fuellung == null) {
            throw new DomainValidationException(
                    FehlerCode.KONFIGURATION_FUELLUNG_FEHLT,
                    "Fuellung darf nicht null sein.");
        }
        

        this.konfigurationsNummer = konfigurationsNummer;
        this.produktFamilie = produktFamilie;
        this.funktion = funktion;
        this.projektAnforderungen = projektAnforderungen;
        this.aufsetzkranz = aufsetzkranz;
        this.festerRahmen = festerRahmen;
        this.fuellung = fuellung;
        this.oeffnungsRahmen = oeffnungsRahmen;
        this.antriebsSystem = antriebsSystem;

        this.technischerStatus = TechnischerStatus.ENTWURF;
    }
    private LichtkuppelKonfiguration(
            String konfigurationsNummer,
            ProduktFamilie produktFamilie,
            LichtkuppelFunktion funktion,
            ProjektAnforderungen projektAnforderungen,
            Aufsetzkranz aufsetzkranz,
            FesterRahmen festerRahmen,
            Fuellung fuellung,
            OeffnungsRahmen oeffnungsRahmen,
            AntriebsSystem antriebsSystem,
            TechnischerStatus technischerStatus) {

        this(
                konfigurationsNummer,
                produktFamilie,
                funktion,
                projektAnforderungen,
                aufsetzkranz,
                festerRahmen,
                fuellung,
                oeffnungsRahmen,
                antriebsSystem);

        if (technischerStatus == null) {
            throw new IllegalArgumentException(
                    "Technischer Status darf beim Rehydrieren nicht null sein.");
        }

        this.technischerStatus = technischerStatus;
    }
    public static LichtkuppelKonfiguration rehydrieren(
            String konfigurationsNummer,
            ProduktFamilie produktFamilie,
            LichtkuppelFunktion funktion,
            ProjektAnforderungen projektAnforderungen,
            Aufsetzkranz aufsetzkranz,
            FesterRahmen festerRahmen,
            Fuellung fuellung,
            OeffnungsRahmen oeffnungsRahmen,
            AntriebsSystem antriebsSystem,
            TechnischerStatus technischerStatus) {

        return new LichtkuppelKonfiguration(
                konfigurationsNummer,
                produktFamilie,
                funktion,
                projektAnforderungen,
                aufsetzkranz,
                festerRahmen,
                fuellung,
                oeffnungsRahmen,
                antriebsSystem,
                technischerStatus);
    }

    public String getKonfigurationsNummer() {
        return konfigurationsNummer;
    }

    public ProduktFamilie getProduktFamilie() {
        return produktFamilie;
    }

    public LichtkuppelFunktion getFunktion() {
        return funktion;
    }

    public ProjektAnforderungen getProjektAnforderungen() {
        return projektAnforderungen;
    }

    public Aufsetzkranz getAufsetzkranz() {
        return aufsetzkranz;
    }

    public FesterRahmen getFesterRahmen() {
        return festerRahmen;
    }

    public Fuellung getFuellung() {
        return fuellung;
    }

    public OeffnungsRahmen getOeffnungsRahmen() {
        return oeffnungsRahmen;
    }

    public AntriebsSystem getAntriebsSystem() {
        return antriebsSystem;
    }

    public TechnischerStatus getTechnischerStatus() {
        return technischerStatus;
    }

    public boolean hatOeffnungsRahmen() {
        return oeffnungsRahmen != null;
    }

    public boolean hatAntriebsSystem() {
        return antriebsSystem != null;
    }

    public void startePruefung() {
        technischerStatus = TechnischerStatus.IN_PRUEFUNG;
    }

    public void markiereFreigabebereit() {
        technischerStatus = TechnischerStatus.FREIGABEBEREIT;
    }

    public void sperre() {
        technischerStatus = TechnischerStatus.GESPERRT;
    }

    public void freigeben() {
        if (technischerStatus != TechnischerStatus.FREIGABEBEREIT) {
            throw new IllegalStateException(
                    "Nur eine freigabebereite Konfiguration kann freigegeben werden.");
        }

        technischerStatus = TechnischerStatus.FREIGEGEBEN;
    }
}