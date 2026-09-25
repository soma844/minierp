package de.hafni.minierp.domain.buchhaltung;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import de.hafni.minierp.TestSzenarien;
import de.hafni.minierp.domain.auftrag.Auftrag;
import de.hafni.minierp.exception.GeschaeftsregelException;

class RechnungTest {

    private Rechnung erstelleRechnung() {

        Auftrag auftrag =
                TestSzenarien.freigegebenerAuftrag6900();

        return new Rechnung(
                "R-2026-001",
                auftrag,
                LocalDate.of(2026, 9, 25),
                LocalDate.of(2026, 10, 25),
                TvaSatz.STANDARD_20);
    }

    @Test
    void rechnungSollHtTvaUndTtcBerechnen() {

        Rechnung rechnung =
                erstelleRechnung();

        assertThat(rechnung.berechneGesamtHt())
                .isEqualByComparingTo("6900.00");

        assertThat(rechnung.berechneTva())
                .isEqualByComparingTo("1380.00");

        assertThat(rechnung.berechneGesamtTtc())
                .isEqualByComparingTo("8280.00");
    }

    @Test
    void teilzahlungSollStatusTeilweiseBezahltSetzen() {

        Rechnung rechnung =
                erstelleRechnung();

        rechnung.erfasseZahlung(
                new Zahlung(
                        LocalDate.of(2026, 10, 1),
                        new BigDecimal("3000.00")));

        assertThat(rechnung.getStatus())
                .isEqualTo(RechnungsStatus.TEILWEISE_BEZAHLT);

        assertThat(rechnung.berechneRestBetrag())
                .isEqualByComparingTo("5280.00");
    }

    @Test
    void vollzahlungSollRechnungAlsBezahltMarkieren() {

        Rechnung rechnung =
                erstelleRechnung();

        rechnung.erfasseZahlung(
                new Zahlung(
                        LocalDate.of(2026, 10, 1),
                        new BigDecimal("3000.00")));

        rechnung.erfasseZahlung(
                new Zahlung(
                        LocalDate.of(2026, 10, 10),
                        new BigDecimal("5280.00")));

        assertThat(rechnung.getStatus())
                .isEqualTo(RechnungsStatus.BEZAHLT);

        assertThat(rechnung.berechneRestBetrag())
                .isEqualByComparingTo("0.00");
    }

    @Test
    void ueberzahlungSollAbgelehntWerden() {

        Rechnung rechnung =
                erstelleRechnung();

        assertThatThrownBy(() ->
                rechnung.erfasseZahlung(
                        new Zahlung(
                                LocalDate.of(2026, 10, 1),
                                new BigDecimal("9000.00"))))
                .isInstanceOf(
                        GeschaeftsregelException.class);
    }
}