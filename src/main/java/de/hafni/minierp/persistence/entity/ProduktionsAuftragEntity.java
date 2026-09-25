package de.hafni.minierp.persistence.entity;

import java.time.LocalDate;

import de.hafni.minierp.domain.production.ProduktionsStatus;
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
import jakarta.persistence.Table;

@Entity
@Table(name = "produktionsauftraege")
public class ProduktionsAuftragEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "produktions_nummer",
            nullable = false,
            unique = true,
            length = 50)
    private String produktionsNummer;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(
            name = "auftrag_id",
            nullable = false)
    private AuftragEntity auftrag;

    @Column(
            name = "geplanter_start",
            nullable = false)
    private LocalDate geplanterStart;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30)
    private ProduktionsStatus status;

    protected ProduktionsAuftragEntity() {
        // JPA
    }

    public ProduktionsAuftragEntity(
            String produktionsNummer,
            AuftragEntity auftrag,
            LocalDate geplanterStart,
            ProduktionsStatus status) {

        this.produktionsNummer = produktionsNummer;
        this.auftrag = auftrag;
        this.geplanterStart = geplanterStart;
        this.status = status;
    }

    public void aktualisiereStatus(
            ProduktionsStatus status) {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Produktionsstatus darf nicht null sein.");
        }

        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getProduktionsNummer() {
        return produktionsNummer;
    }

    public AuftragEntity getAuftrag() {
        return auftrag;
    }

    public LocalDate getGeplanterStart() {
        return geplanterStart;
    }

    public ProduktionsStatus getStatus() {
        return status;
    }
}