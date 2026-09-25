package de.hafni.minierp.domain.product;

import java.math.BigDecimal;

import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;

public class Steuerung {

    private final VersorgungsSpannung versorgungsSpannung;
    private final BigDecimal maximalerAusgangsStromAmpere;

    public Steuerung(
            VersorgungsSpannung versorgungsSpannung,
            BigDecimal maximalerAusgangsStromAmpere) {

        if (versorgungsSpannung == null) {
            throw new DomainValidationException(
                    FehlerCode.STEUERUNG_SPANNUNG_FEHLT,
                    "Versorgungsspannung der Steuerung darf nicht null sein.");
        }

        if (maximalerAusgangsStromAmpere == null
                || maximalerAusgangsStromAmpere.signum() <= 0) {

            throw new DomainValidationException(
                    FehlerCode.STEUERUNG_STROM_UNGUELTIG,
                    "Maximaler Ausgangsstrom muss groesser als 0 sein.");
        }

        this.versorgungsSpannung = versorgungsSpannung;
        this.maximalerAusgangsStromAmpere = maximalerAusgangsStromAmpere;
    }

    public VersorgungsSpannung getVersorgungsSpannung() {
        return versorgungsSpannung;
    }

    public BigDecimal getMaximalerAusgangsStromAmpere() {
        return maximalerAusgangsStromAmpere;
    }
}