package de.hafni.minierp.persistence.entity;

import de.hafni.minierp.domain.konfiguration.LichtkuppelFunktion;
import de.hafni.minierp.domain.konfiguration.TechnischerStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
@Table(name = "lichtkuppel_konfigurationen")
public class LichtkuppelKonfigurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "konfigurations_nummer",
            nullable = false,
            unique = true,
            length = 50)
    private String konfigurationsNummer;

    @Column(
            name = "produkt_code",
            nullable = false,
            length = 100)
    private String produktCode;

    @Column(
            name = "produkt_bezeichnung",
            nullable = false,
            length = 200)
    private String produktBezeichnung;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private LichtkuppelFunktion funktion;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "technischer_status",
            nullable = false,
            length = 30)
    private TechnischerStatus technischerStatus;
    @Embedded
    private OeffnungsRahmenEmbeddable oeffnungsRahmen;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "antriebssystem_id")
    private AntriebsSystemEntity antriebsSystem;

    @Embedded
    private ProjektAnforderungenEmbeddable projektAnforderungen;

    @Embedded
    private AufsetzkranzEmbeddable aufsetzkranz;

    @Embedded
    private FesterRahmenEmbeddable festerRahmen;

    @Embedded
    private FuellungEmbeddable fuellung;

    protected LichtkuppelKonfigurationEntity() {
    }

    public LichtkuppelKonfigurationEntity(
            String konfigurationsNummer,
            String produktCode,
            String produktBezeichnung,
            LichtkuppelFunktion funktion,
            TechnischerStatus technischerStatus,
            ProjektAnforderungenEmbeddable projektAnforderungen,
            AufsetzkranzEmbeddable aufsetzkranz,
            FesterRahmenEmbeddable festerRahmen,
            FuellungEmbeddable fuellung,
            OeffnungsRahmenEmbeddable oeffnungsRahmen,
            AntriebsSystemEntity antriebsSystem) {

        this.konfigurationsNummer = konfigurationsNummer;
        this.produktCode = produktCode;
        this.produktBezeichnung = produktBezeichnung;
        this.funktion = funktion;
        this.technischerStatus = technischerStatus;
        this.projektAnforderungen = projektAnforderungen;
        this.aufsetzkranz = aufsetzkranz;
        this.festerRahmen = festerRahmen;
        this.fuellung = fuellung;
        this.oeffnungsRahmen = oeffnungsRahmen;
        this.antriebsSystem = antriebsSystem;
    }
    public void aktualisiereTechnischenStatus(
            TechnischerStatus technischerStatus) {

        if (technischerStatus == null) {
            throw new IllegalArgumentException(
                    "Technischer Status darf nicht null sein.");
        }

        this.technischerStatus = technischerStatus;
    }
    public void aktualisiereKonfigurationsDaten(
            String produktCode,
            String produktBezeichnung,
            LichtkuppelFunktion funktion,
            ProjektAnforderungenEmbeddable projektAnforderungen,
            AufsetzkranzEmbeddable aufsetzkranz,
            FesterRahmenEmbeddable festerRahmen,
            FuellungEmbeddable fuellung,
            OeffnungsRahmenEmbeddable oeffnungsRahmen,
            AntriebsSystemEntity antriebsSystem) {

        this.produktCode = produktCode;
        this.produktBezeichnung = produktBezeichnung;
        this.funktion = funktion;

        this.projektAnforderungen = projektAnforderungen;
        this.aufsetzkranz = aufsetzkranz;
        this.festerRahmen = festerRahmen;
        this.fuellung = fuellung;

        this.oeffnungsRahmen = oeffnungsRahmen;
        this.antriebsSystem = antriebsSystem;
    }

    public Long getId() {
        return id;
    }

    public String getKonfigurationsNummer() {
        return konfigurationsNummer;
    }

    public LichtkuppelFunktion getFunktion() {
        return funktion;
    }

    public TechnischerStatus getTechnischerStatus() {
        return technischerStatus;
    }

    public ProjektAnforderungenEmbeddable getProjektAnforderungen() {
        return projektAnforderungen;
    }

    public AufsetzkranzEmbeddable getAufsetzkranz() {
        return aufsetzkranz;
    }

    public FesterRahmenEmbeddable getFesterRahmen() {
        return festerRahmen;
    }

    public FuellungEmbeddable getFuellung() {
        return fuellung;
    }
    public OeffnungsRahmenEmbeddable getOeffnungsRahmen() {
        return oeffnungsRahmen;
    }

    public AntriebsSystemEntity getAntriebsSystem() {
        return antriebsSystem;
    }
    public String getProduktCode() {
        return produktCode;
    }

    public String getProduktBezeichnung() {
        return produktBezeichnung;
    }
    
}