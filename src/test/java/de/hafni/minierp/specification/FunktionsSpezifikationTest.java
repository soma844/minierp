package de.hafni.minierp.specification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.hafni.minierp.TestObjekte;
import de.hafni.minierp.domain.konfiguration.LichtkuppelFunktion;
import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.pruefung.PruefErgebnis;
import de.hafni.minierp.pruefung.PruefStatus;

class FunktionsSpezifikationTest {

    private final FunktionsSpezifikation spezifikation =
            new FunktionsSpezifikation();

    @Test
    void festeLichtkuppelOhneAntriebSollBestandenSein() {

        LichtkuppelKonfiguration konfiguration =
                TestObjekte.fest();

        PruefErgebnis ergebnis =
                spezifikation.pruefe(konfiguration);

        assertThat(ergebnis.getStatus())
                .isEqualTo(PruefStatus.BESTANDEN);
    }

    @Test
    void lueftungOhneAntriebSollNichtBestandenSein() {

        LichtkuppelKonfiguration konfiguration =
                new LichtkuppelKonfiguration(
                        "LK-TEST-003",
                        TestObjekte.produktFamilie(),
                        LichtkuppelFunktion.LUEFTUNG,
                        TestObjekte.projektAnforderungen(10),
                        TestObjekte.aufsetzkranz(),
                        TestObjekte.festerRahmen(),
                        TestObjekte.fuellung(),
                        TestObjekte.oeffnungsRahmen(),
                        null);

        PruefErgebnis ergebnis =
                spezifikation.pruefe(konfiguration);

        assertThat(ergebnis.getStatus())
                .isEqualTo(PruefStatus.NICHT_BESTANDEN);

        assertThat(ergebnis.getRegelId())
                .isEqualTo("TECH-FKT-003");
    }
}