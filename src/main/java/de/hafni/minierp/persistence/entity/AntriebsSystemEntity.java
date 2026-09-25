package de.hafni.minierp.persistence.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "antriebssysteme")
public class AntriebsSystemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private SteuerungEmbeddable steuerung;

    @OneToMany(
            mappedBy = "antriebsSystem",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<AntriebEntity> antriebe =
            new ArrayList<>();

    protected AntriebsSystemEntity() {
    }

    public AntriebsSystemEntity(
            SteuerungEmbeddable steuerung) {

        this.steuerung = steuerung;
    }

    public void fuegeAntriebHinzu(
            AntriebEntity antrieb) {

        antriebe.add(antrieb);
        antrieb.setAntriebsSystem(this);
    }

    public Long getId() {
        return id;
    }

    public SteuerungEmbeddable getSteuerung() {
        return steuerung;
    }

    public List<AntriebEntity> getAntriebe() {
        return Collections.unmodifiableList(antriebe);
    }
}