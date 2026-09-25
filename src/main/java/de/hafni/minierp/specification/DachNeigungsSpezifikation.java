package de.hafni.minierp.specification;

import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.pruefung.PruefErgebnis;

public class DachNeigungsSpezifikation
        implements Spezifikation<LichtkuppelKonfiguration> {

    private static final double MAX_DACHNEIGUNG_GRAD = 25.0;

    @Override
    public PruefErgebnis pruefe(
            LichtkuppelKonfiguration konfiguration) {

        double dachNeigung =
                konfiguration
                        .getProjektAnforderungen()
                        .getDachNeigungGrad();

        if (dachNeigung > MAX_DACHNEIGUNG_GRAD) {

            return PruefErgebnis.nichtBestanden(
                    "TECH-DACH-001",
                    "Die Dachneigung darf maximal 25 Grad betragen.");
        }

        return PruefErgebnis.bestanden(
                "TECH-DACH-001",
                "Dachneigung liegt im zulässigen Bereich.");
    }
}