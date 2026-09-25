package de.hafni.minierp.dto;

import de.hafni.minierp.pruefung.PruefErgebnis;
import de.hafni.minierp.pruefung.PruefStatus;

public record PruefErgebnisResponse(
        String regelId,
        PruefStatus status,
        String meldung) {

    public static PruefErgebnisResponse from(
            PruefErgebnis ergebnis) {

        return new PruefErgebnisResponse(
                ergebnis.getRegelId(),
                ergebnis.getStatus(),
                ergebnis.getMeldung());
    }
}