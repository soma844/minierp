package de.hafni.minierp.persistence.entity;

import java.math.BigDecimal;

import de.hafni.minierp.domain.product.AntriebsArt;
import de.hafni.minierp.domain.product.VersorgungsSpannung;
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
@Table(name = "antriebe")
public class AntriebEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AntriebsArt art;

    @Enumerated(EnumType.STRING)
    @Column(name = "versorgungs_spannung", nullable = false)
    private VersorgungsSpannung versorgungsSpannung;

    @Column(name = "hub_mm", nullable = false)
    private int hubMm;

    @Column(name = "kraft_newton", nullable = false)
    private int kraftNewton;

    @Column(
            name = "nennstrom_ampere",
            nullable = false,
            precision = 8,
            scale = 3)
    private BigDecimal nennStromAmpere;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "antriebssystem_id", nullable = false)
    private AntriebsSystemEntity antriebsSystem;

    protected AntriebEntity() {
    }

    public AntriebEntity(
            AntriebsArt art,
            VersorgungsSpannung versorgungsSpannung,
            int hubMm,
            int kraftNewton,
            BigDecimal nennStromAmpere) {

        this.art = art;
        this.versorgungsSpannung = versorgungsSpannung;
        this.hubMm = hubMm;
        this.kraftNewton = kraftNewton;
        this.nennStromAmpere = nennStromAmpere;
    }

    void setAntriebsSystem(
            AntriebsSystemEntity antriebsSystem) {

        this.antriebsSystem = antriebsSystem;
    }

    public Long getId() {
        return id;
    }

    public AntriebsArt getArt() {
        return art;
    }

    public VersorgungsSpannung getVersorgungsSpannung() {
        return versorgungsSpannung;
    }

    public int getHubMm() {
        return hubMm;
    }

    public int getKraftNewton() {
        return kraftNewton;
    }

    public BigDecimal getNennStromAmpere() {
        return nennStromAmpere;
    }
}