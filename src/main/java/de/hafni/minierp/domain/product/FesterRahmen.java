package de.hafni.minierp.domain.product;

public class FesterRahmen {

    private final Material material;
    private final boolean thermischGetrennt;

    public FesterRahmen(
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