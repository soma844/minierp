package de.hafni.minierp.persistence.entity;

import java.math.BigDecimal;

import de.hafni.minierp.domain.product.FuellungsArt;
import de.hafni.minierp.domain.product.FuellungsAusfuehrung;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class FuellungEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "fuellung_art")
    private FuellungsArt art;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuellung_ausfuehrung")
    private FuellungsAusfuehrung ausfuehrung;

    @Column(name = "fuellung_dicke_mm")
    private int dickeMm;

    @Column(name = "fuellung_ug_wert", precision = 8, scale = 3)
    private BigDecimal ugWert;

    @Column(name = "fuellung_lichttransmission", precision = 5, scale = 2)
    private BigDecimal lichtTransmissionProzent;

    @Column(name = "fuellung_solarfaktor", precision = 5, scale = 2)
    private BigDecimal solarFaktorProzent;

    protected FuellungEmbeddable() {
    }

    public FuellungEmbeddable(
            FuellungsArt art,
            FuellungsAusfuehrung ausfuehrung,
            int dickeMm,
            BigDecimal ugWert,
            BigDecimal lichtTransmissionProzent,
            BigDecimal solarFaktorProzent) {

        this.art = art;
        this.ausfuehrung = ausfuehrung;
        this.dickeMm = dickeMm;
        this.ugWert = ugWert;
        this.lichtTransmissionProzent = lichtTransmissionProzent;
        this.solarFaktorProzent = solarFaktorProzent;
    }

    public FuellungsArt getArt() {
        return art;
    }

    public FuellungsAusfuehrung getAusfuehrung() {
        return ausfuehrung;
    }

    public int getDickeMm() {
        return dickeMm;
    }

    public BigDecimal getUgWert() {
        return ugWert;
    }

    public BigDecimal getLichtTransmissionProzent() {
        return lichtTransmissionProzent;
    }

    public BigDecimal getSolarFaktorProzent() {
        return solarFaktorProzent;
    }
}