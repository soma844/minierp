package de.hafni.minierp.domain.konfiguration;

public class ProjektAnforderungen {

    private int dachOeffnungBreiteMm;
    private int dachOeffnungLaengeMm;
    private double dachNeigungGrad;

    public ProjektAnforderungen(
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