package de.hafni.minierp.specification;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import de.hafni.minierp.TestObjekte;
import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.pruefung.PruefErgebnis;
import de.hafni.minierp.pruefung.PruefStatus;

class DachNeigungsSpezifikationTest {

    private final DachNeigungsSpezifikation spezifikation =
            new DachNeigungsSpezifikation();

    @Test
    void dachneigungUnter25GradSollBestandenSein() {

        LichtkuppelKonfiguration konfiguration =
                TestObjekte.lueftung(
                        10,
                        new BigDecimal("1.0"));

        PruefErgebnis ergebnis =
                spezifikation.pruefe(konfiguration);

        assertThat(ergebnis.getStatus())
                .isEqualTo(PruefStatus.BESTANDEN);
    }

    @Test
    void dachneigungUeber25GradSollNichtBestandenSein() {

        LichtkuppelKonfiguration konfiguration =
                TestObjekte.lueftung(
                        30,
                        new BigDecimal("1.0"));

        PruefErgebnis ergebnis =
                spezifikation.pruefe(konfiguration);

        assertThat(ergebnis.getStatus())
                .isEqualTo(PruefStatus.NICHT_BESTANDEN);

        assertThat(ergebnis.getRegelId())
                .isEqualTo("TECH-DACH-001");
    }
}