package de.hafni.minierp.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProjektAnforderungenEmbeddable {

    @Column(name = "dach_oeffnung_breite_mm")
    private int dachOeffnungBreiteMm;

    @Column(name = "dach_oeffnung_laenge_mm")
    private int dachOeffnungLaengeMm;

    @Column(name = "dach_neigung_grad")
    private double dachNeigungGrad;

    protected ProjektAnforderungenEmbeddable() {
    }

    public ProjektAnforderungenEmbeddable(
            int dachOeffnungBreiteMm,
            int dachOeffnungLaengeMm,
            double dachNeigungGrad) {

        this.dachOeffnungBreiteMm = dachOeffnungBreiteMm;
        this.dachOeffnungLaengeMm = dachOeffnungLaengeMm;
        this.dachNeigungGrad = dachNeigungGrad;
    }

    public int getDachOeffnungBreiteMm() {
        return dachOeffnungBreiteMm;
    }

    public int getDachOeffnungLaengeMm() {
        return dachOeffnungLaengeMm;
    }

    public double getDachNeigungGrad() {
        return dachNeigungGrad;
    }
}