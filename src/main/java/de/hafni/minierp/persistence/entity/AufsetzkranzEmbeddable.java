package de.hafni.minierp.persistence.entity;

import de.hafni.minierp.domain.product.AufsetzkranzTyp;
import de.hafni.minierp.domain.product.Material;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class AufsetzkranzEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "kranz_typ")
    private AufsetzkranzTyp typ;

    @Enumerated(EnumType.STRING)
    @Column(name = "kranz_material")
    private Material material;

    @Column(name = "kranz_hoehe_mm")
    private int hoeheMm;

    @Column(name = "kranz_daemmstaerke_mm")
    private int daemmStaerkeMm;

    protected AufsetzkranzEmbeddable() {
    }

    public AufsetzkranzEmbeddable(
            AufsetzkranzTyp typ,
            Material material,
            int hoeheMm,
            int daemmStaerkeMm) {

        this.typ = typ;
        this.material = material;
        this.hoeheMm = hoeheMm;
        this.daemmStaerkeMm = daemmStaerkeMm;
    }

    public AufsetzkranzTyp getTyp() {
        return typ;
    }

    public Material getMaterial() {
        return material;
    }

    public int getHoeheMm() {
        return hoeheMm;
    }

    public int getDaemmStaerkeMm() {
        return daemmStaerkeMm;
    }
}