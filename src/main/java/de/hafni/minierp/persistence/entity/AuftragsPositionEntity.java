package de.hafni.minierp.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "auftrags_positionen")
public class AuftragsPositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "positions_nummer",
            nullable = false)
    private int positionsNummer;

    @Column(nullable = false)
    private int menge;

    @Column(
            name = "einzel_preis_ht",
            nullable = false,
            precision = 12,
            scale = 2)
    private BigDecimal einzelPreisHt;

    @Column(
            name = "rabatt_prozent",
            nullable = false,
            precision = 5,
            scale = 2)
    private BigDecimal rabattProzent;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            optional = false)
    @JoinColumn(
            name = "konfiguration_id",
            nullable = false,
            unique = true)
    private LichtkuppelKonfigurationEntity konfiguration;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(
            name = "auftrag_id",
            nullable = false)
    private AuftragEntity auftrag;

    protected AuftragsPositionEntity() {
        // JPA
    }

    public AuftragsPositionEntity(
            int positionsNummer,
            int menge,
            BigDecimal einzelPreisHt,
            BigDecimal rabattProzent,
            LichtkuppelKonfigurationEntity konfiguration) {

        this.positionsNummer = positionsNummer;
        this.menge = menge;
        this.einzelPreisHt = einzelPreisHt;
        this.rabattProzent = rabattProzent;
        this.konfiguration = konfiguration;
    }

    void setAuftrag(
            AuftragEntity auftrag) {

        this.auftrag = auftrag;
    }

    public Long getId() {
        return id;
    }

    public LichtkuppelKonfigurationEntity getKonfiguration() {
        return konfiguration;
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

    public AuftragEntity getAuftrag() {
        return auftrag;
    }
}