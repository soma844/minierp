package de.hafni.minierp.specification;

import de.hafni.minierp.domain.konfiguration.LichtkuppelFunktion;
import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.domain.product.AntriebsSystem;
import de.hafni.minierp.domain.product.Antrieb;
import de.hafni.minierp.domain.product.Steuerung;
import de.hafni.minierp.pruefung.PruefErgebnis;

public class ElektrikSpezifikation
        implements Spezifikation<LichtkuppelKonfiguration> {

    @Override
    public PruefErgebnis pruefe(
            LichtkuppelKonfiguration konfiguration) {

        if (konfiguration.getFunktion()
                == LichtkuppelFunktion.FEST) {

            return PruefErgebnis.bestanden(
                    "TECH-EL-002",
                    "Für eine feste Lichtkuppel ist keine Antriebsversorgung erforderlich.");
        }

        AntriebsSystem system =
                konfiguration.getAntriebsSystem();

        if (system == null) {
            return PruefErgebnis.nichtBestanden(
                    "TECH-EL-001",
                    "Antriebssystem fehlt.");
        }

        Steuerung steuerung = system.getSteuerung();

        if (steuerung == null) {
            return PruefErgebnis.nichtBestanden(
                    "TECH-EL-003",
                    "Steuerung für das Antriebssystem fehlt.");
        }

        for (Antrieb antrieb : system.getAntriebe()) {

            if (antrieb.getVersorgungsSpannung()
                    != steuerung.getVersorgungsSpannung()) {

                return PruefErgebnis.nichtBestanden(
                        "TECH-EL-003",
                        "Versorgungsspannung von Steuerung und Antrieb ist nicht kompatibel.");
            }
        }

        if (!system.istSteuerungAusreichend()) {

            return PruefErgebnis.nichtBestanden(
                    "TECH-EL-004",
                    "Der maximale Ausgangsstrom der Steuerung ist zu gering.");
        }

        return PruefErgebnis.bestanden(
                "TECH-EL-004",
                "Elektrische Auslegung ist ausreichend.");
    }
}