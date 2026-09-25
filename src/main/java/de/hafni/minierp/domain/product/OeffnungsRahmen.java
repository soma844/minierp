package de.hafni.minierp.domain.product;

import java.math.BigDecimal;

public class OeffnungsRahmen {

    private final Material material;
    private final boolean thermischGetrennt;
    private final BigDecimal gewichtKg;

    public OeffnungsRahmen(
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