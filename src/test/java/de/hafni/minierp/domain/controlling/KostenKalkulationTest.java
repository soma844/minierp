package de.hafni.minierp.domain.controlling;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import de.hafni.minierp.TestSzenarien;
import de.hafni.minierp.domain.auftrag.Auftrag;

class KostenKalkulationTest {

    @Test
    void deckungsbeitragSollKorrektBerechnetWerden() {

        Auftrag auftrag =
                TestSzenarien.freigegebenerAuftrag6900();

        KostenKalkulation kalkulation =
                new KostenKalkulation(
                        auftrag,
                        new BigDecimal("3000.00"),
                        new BigDecimal("900.00"));

        assertThat(kalkulation.berechneGesamtKosten())
                .isEqualByComparingTo("3900.00");

        assertThat(kalkulation.berechneDeckungsBeitrag())
                .isEqualByComparingTo("3000.00");
    }

    @Test
    void margeSollKorrektBerechnetWerden() {

        Auftrag auftrag =
                TestSzenarien.freigegebenerAuftrag6900();

        KostenKalkulation kalkulation =
                new KostenKalkulation(
                        auftrag,
                        new BigDecimal("3000.00"),
                        new BigDecimal("900.00"));

        assertThat(kalkulation.berechneMargeProzent())
                .isEqualByComparingTo("43.48");
    }
}