package de.hafni.minierp.persistence.entity;

import java.math.BigDecimal;

import de.hafni.minierp.domain.product.VersorgungsSpannung;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class SteuerungEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "steuerung_spannung")
    private VersorgungsSpannung versorgungsSpannung;

    @Column(
            name = "steuerung_max_strom_ampere",
            precision = 8,
            scale = 3)
    private BigDecimal maximalerAusgangsStromAmpere;

    protected SteuerungEmbeddable() {
    }

    public SteuerungEmbeddable(
            VersorgungsSpannung versorgungsSpannung,
            BigDecimal maximalerAusgangsStromAmpere) {

        this.versorgungsSpannung = versorgungsSpannung;
        this.maximalerAusgangsStromAmpere =
                maximalerAusgangsStromAmpere;
    }

    public VersorgungsSpannung getVersorgungsSpannung() {
        return versorgungsSpannung;
    }

    public BigDecimal getMaximalerAusgangsStromAmpere() {
        return maximalerAusgangsStromAmpere;
    }
}