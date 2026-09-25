package de.hafni.minierp.persistence.entity;

import de.hafni.minierp.domain.product.Material;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class FesterRahmenEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "fester_rahmen_material")
    private Material material;

    @Column(name = "fester_rahmen_thermisch_getrennt")
    private boolean thermischGetrennt;

    protected FesterRahmenEmbeddable() {
    }

    public FesterRahmenEmbeddable(
            Material material,
            boolean thermischGetrennt) {

        this.material = material;
        this.thermischGetrennt = thermischGetrennt;
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isThermischGetrennt() {
        return thermischGetrennt;
    }
}