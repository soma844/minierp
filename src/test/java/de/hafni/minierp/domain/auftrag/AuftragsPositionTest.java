package de.hafni.minierp.domain.auftrag;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import de.hafni.minierp.TestObjekte;

class AuftragsPositionTest {

    @Test
    void gesamtHtSollMengeUndRabattBeruecksichtigen() {

        AuftragsPosition position =
                new AuftragsPosition(
                        10,
                        5,
                        new BigDecimal("1000.00"),
                        new BigDecimal("10"),
                        TestObjekte.fest());

        assertThat(position.berechneBruttoHt())
                .isEqualByComparingTo("5000.00");

        assertThat(position.berechneRabattBetrag())
                .isEqualByComparingTo("500.00");

        assertThat(position.berechneGesamtHt())
                .isEqualByComparingTo("4500.00");
    }
}