package de.hafni.minierp.specification;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import de.hafni.minierp.TestObjekte;
import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.pruefung.PruefErgebnis;
import de.hafni.minierp.pruefung.PruefStatus;

class ElektrikSpezifikationTest {

    private final ElektrikSpezifikation spezifikation =
            new ElektrikSpezifikation();

    @Test
    void ausreichenderSteuerungsstromSollBestandenSein() {

        LichtkuppelKonfiguration konfiguration =
                TestObjekte.lueftung(
                        10,
                        new BigDecimal("1.00"));

        PruefErgebnis ergebnis =
                spezifikation.pruefe(konfiguration);

        assertThat(ergebnis.getStatus())
                .isEqualTo(PruefStatus.BESTANDEN);
    }

    @Test
    void zuKleineSteuerungSollNichtBestandenSein() {

        LichtkuppelKonfiguration konfiguration =
                TestObjekte.lueftung(
                        10,
                        new BigDecimal("0.10"));

        PruefErgebnis ergebnis =
                spezifikation.pruefe(konfiguration);

        assertThat(ergebnis.getStatus())
                .isEqualTo(PruefStatus.NICHT_BESTANDEN);

        assertThat(ergebnis.getRegelId())
                .isEqualTo("TECH-EL-004");
    }
}