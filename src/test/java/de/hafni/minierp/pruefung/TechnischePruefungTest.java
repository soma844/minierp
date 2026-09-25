package de.hafni.minierp.pruefung;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.hafni.minierp.TestObjekte;
import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.domain.konfiguration.TechnischerStatus;

class TechnischePruefungTest {

    private final TechnischePruefung technischePruefung =
            new TechnischePruefung();

    @Test
    void gueltigeKonfigurationSollFreigabebereitWerden() {

        LichtkuppelKonfiguration konfiguration =
                TestObjekte.lueftung(
                        10,
                        new BigDecimal("1.00"));

        List<PruefErgebnis> ergebnisse =
                technischePruefung.pruefe(konfiguration);

        assertThat(ergebnisse)
                .allMatch(ergebnis -> !ergebnis.istFehler());

        assertThat(konfiguration.getTechnischerStatus())
                .isEqualTo(TechnischerStatus.FREIGABEBEREIT);
    }

    @Test
    void fehlerhafteKonfigurationSollGesperrtWerden() {

        LichtkuppelKonfiguration konfiguration =
                TestObjekte.lueftung(
                        30,
                        new BigDecimal("0.10"));

        List<PruefErgebnis> ergebnisse =
                technischePruefung.pruefe(konfiguration);

        assertThat(ergebnisse)
                .anyMatch(PruefErgebnis::istFehler);

        assertThat(konfiguration.getTechnischerStatus())
                .isEqualTo(TechnischerStatus.GESPERRT);
    }
}