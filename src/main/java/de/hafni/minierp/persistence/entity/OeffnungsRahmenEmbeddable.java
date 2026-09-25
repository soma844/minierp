package de.hafni.minierp.persistence.entity;

import java.math.BigDecimal;

import de.hafni.minierp.domain.product.Material;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class OeffnungsRahmenEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "oeffnungsrahmen_material")
    private Material material;

    @Column(name = "oeffnungsrahmen_thermisch_getrennt")
    private boolean thermischGetrennt;

    @Column(
            name = "oeffnungsrahmen_gewicht_kg",
            precision = 8,
            scale = 2)
    private BigDecimal gewichtKg;

    protected OeffnungsRahmenEmbeddable() {
    }

    public OeffnungsRahmenEmbeddable(
            Material material,
            boolean thermischGetrennt,
            BigDecimal gewichtKg) {

        this.material = material;
        this.thermischGetrennt = thermischGetrennt;
        this.gewichtKg = gewichtKg;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isThermischGetrennt() {
        return thermischGetrennt;
    }

    public BigDecimal getGewichtKg() {
        return gewichtKg;
    }
}