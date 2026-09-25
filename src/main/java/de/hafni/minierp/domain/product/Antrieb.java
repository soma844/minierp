package de.hafni.minierp.domain.product;

import java.math.BigDecimal;

import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;

public class Antrieb {

    private final AntriebsArt art;
    private final VersorgungsSpannung versorgungsSpannung;
    private final int hubMm;
    private final int kraftNewton;
    private final BigDecimal nennStromAmpere;

    public Antrieb(
            AntriebsArt art,
            VersorgungsSpannung versorgungsSpannung,
            int hubMm,
            int kraftNewton,
            BigDecimal nennStromAmpere) {

        if (art == null) {
            throw new DomainValidationException(
                    FehlerCode.ANTRIEB_ART_FEHLT,
                    "Antriebsart darf nicht null sein.");
        }

        if (versorgungsSpannung == null) {
            throw new DomainValidationException(
                    FehlerCode.ANTRIEB_SPANNUNG_FEHLT,
                    "Versorgungsspannung darf nicht null sein.");
        }

        if (hubMm <= 0) {
            throw new DomainValidationException(
                    FehlerCode.ANTRIEB_HUB_UNGUELTIG,
                    "Hub muss groesser als 0 sein.");
        }

        if (kraftNewton <= 0) {
            throw new DomainValidationException(
                    FehlerCode.ANTRIEB_KRAFT_UNGUELTIG,
                    "Kraft muss groesser als 0 sein.");
        }

        if (nennStromAmpere == null
                || nennStromAmpere.signum() <= 0) {

            throw new DomainValidationException(
                    FehlerCode.ANTRIEB_STROM_UNGUELTIG,
                    "Nennstrom muss groesser als 0 sein.");
        }

        this.art = art;
        this.versorgungsSpannung = versorgungsSpannung;
        this.hubMm = hubMm;
        this.kraftNewton = kraftNewton;
        this.nennStromAmpere = nennStromAmpere;
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