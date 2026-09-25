package de.hafni.minierp.persistence.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hafni.minierp.domain.auftrag.AuftragsStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "auftraege")
public class AuftragEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "auftrags_nummer",
            nullable = false,
            unique = true,
            length = 50)
    private String auftragsNummer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AuftragsStatus status;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(
            name = "kunde_id",
            nullable = false)
    private KundeEntity kunde;

    @OneToMany(
            mappedBy = "auftrag",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<AuftragsPositionEntity> positionen =
            new ArrayList<>();

    protected AuftragEntity() {
        // JPA
    }

    public AuftragEntity(
            String auftragsNummer,
            AuftragsStatus status,
            KundeEntity kunde) {

        this.auftragsNummer = auftragsNummer;
        this.status = status;
        this.kunde = kunde;
    }

    public void fuegePositionHinzu(
            AuftragsPositionEntity position) {

        positionen.add(position);
        position.setAuftrag(this);
    }

    public Long getId() {
        return id;
    }

    public String getAuftragsNummer() {
        return auftragsNummer;
    }

    public AuftragsStatus getStatus() {
        return status;
    }

    public KundeEntity getKunde() {
        return kunde;
    }

    public List<AuftragsPositionEntity> getPositionen() {
        return Collections.unmodifiableList(positionen);
    }
    public void aktualisiereStatus(
            AuftragsStatus status) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Auftragsstatus darf nicht null sein."
            );
        }

        this.status = status;
    }
}