package de.hafni.minierp.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany; 	
@Entity
@Table(name = "kunden")
public class KundeEntity {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "kunden_nummer",
            nullable = false,
            unique = true,
            length = 50)
    private String kundenNummer;

    @Column(
            name = "firmen_name",
            nullable = false,
            length = 200)
    private String firmenName;
    @OneToMany(
            mappedBy = "kunde",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<AuftragEntity> auftraege =
            new ArrayList<>();
    public void fuegeAuftragHinzu(
            AuftragEntity auftrag) {

        auftraege.add(auftrag);
    }

    public List<AuftragEntity> getAuftraege() {
        return Collections.unmodifiableList(auftraege);
    }

    protected KundeEntity() {
        // Wird von JPA benötigt.
    }

    public KundeEntity(
            String kundenNummer,
            String firmenName) {

        this.kundenNummer = kundenNummer;
        this.firmenName = firmenName;
    }

    public Long getId() {
        return id;
    }

    public String getKundenNummer() {
        return kundenNummer;
    }

    public String getFirmenName() {
        return firmenName;
    }
    public void aktualisiereFirmenName(String firmenName) {

        if (firmenName == null || firmenName.isBlank()) {
            throw new IllegalArgumentException(
                    "Firmenname darf nicht leer sein."
            );
        }

        this.firmenName = firmenName;
    }
}