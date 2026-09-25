package de.hafni.minierp.specification;

import de.hafni.minierp.domain.konfiguration.LichtkuppelFunktion;
import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.pruefung.PruefErgebnis;

public class FunktionsSpezifikation
        implements Spezifikation<LichtkuppelKonfiguration> {

    @Override
    public PruefErgebnis pruefe(
            LichtkuppelKonfiguration konfiguration) {

        LichtkuppelFunktion funktion =
                konfiguration.getFunktion();

        if (funktion == LichtkuppelFunktion.FEST) {

            if (konfiguration.hatAntriebsSystem()) {
                return PruefErgebnis.nichtBestanden(
                        "TECH-FKT-002",
                        "Eine feste Lichtkuppel darf kein Antriebssystem besitzen.");
            }

            if (konfiguration.hatOeffnungsRahmen()) {
                return PruefErgebnis.nichtBestanden(
                        "TECH-FKT-002",
                        "Eine feste Lichtkuppel darf keinen Öffnungsrahmen besitzen.");
            }

            return PruefErgebnis.bestanden(
                    "TECH-FKT-002",
                    "Feste Lichtkuppel ist korrekt konfiguriert.");
        }

        if (funktion == LichtkuppelFunktion.LUEFTUNG) {

            if (!konfiguration.hatOeffnungsRahmen()) {
                return PruefErgebnis.nichtBestanden(
                        "TECH-FKT-003",
                        "Eine Lüftungslichtkuppel benötigt einen Öffnungsrahmen.");
            }

            if (!konfiguration.hatAntriebsSystem()) {
                return PruefErgebnis.nichtBestanden(
                        "TECH-FKT-003",
                        "Eine Lüftungslichtkuppel benötigt ein Antriebssystem.");
            }

            return PruefErgebnis.bestanden(
                    "TECH-FKT-003",
                    "Lüftungsfunktion ist vollständig konfiguriert.");
        }

        return PruefErgebnis.nichtBestanden(
                "TECH-FKT-004",
                "Rauchabzug ist im aktuellen MVP noch nicht implementiert.");
    }
}