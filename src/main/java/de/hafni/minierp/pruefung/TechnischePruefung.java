package de.hafni.minierp.pruefung;

import java.util.ArrayList;
import java.util.List;

import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.specification.DachNeigungsSpezifikation;
import de.hafni.minierp.specification.ElektrikSpezifikation;
import de.hafni.minierp.specification.FunktionsSpezifikation;
import de.hafni.minierp.specification.Spezifikation;

public class TechnischePruefung {

    private final List<Spezifikation<LichtkuppelKonfiguration>>
            spezifikationen;

    public TechnischePruefung() {

        this.spezifikationen = List.of(
                new FunktionsSpezifikation(),
                new DachNeigungsSpezifikation(),
                new ElektrikSpezifikation()
        );
    }

    public List<PruefErgebnis> pruefe(
            LichtkuppelKonfiguration konfiguration) {

        konfiguration.startePruefung();

        List<PruefErgebnis> ergebnisse =
                new ArrayList<>();

        for (Spezifikation<LichtkuppelKonfiguration> spezifikation
                : spezifikationen) {

            PruefErgebnis ergebnis =
                    spezifikation.pruefe(konfiguration);

            ergebnisse.add(ergebnis);
        }

        boolean hatFehler =
                ergebnisse.stream()
                        .anyMatch(PruefErgebnis::istFehler);

        if (hatFehler) {
            konfiguration.sperre();
        } else {
            konfiguration.markiereFreigabebereit();
        }

        return ergebnisse;
    }
}