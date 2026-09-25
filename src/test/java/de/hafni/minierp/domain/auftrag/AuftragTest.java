package de.hafni.minierp.domain.auftrag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import de.hafni.minierp.TestObjekte;
import de.hafni.minierp.domain.konfiguration.TechnischerStatus;
import de.hafni.minierp.exception.FehlerCode;
import de.hafni.minierp.exception.GeschaeftsregelException;
import de.hafni.minierp.pruefung.TechnischePruefung;

class AuftragTest {

    @Test
    void auftragSollSummeAllerPositionenBerechnen() {

        Kunde kunde =
                new Kunde(
                        "K-1001",
                        "Société Demo SARL");

        Auftrag auftrag =
                new Auftrag(
                        "A-2026-001",
                        kunde);

        AuftragsPosition position10 =
                new AuftragsPosition(
                        10,
                        5,
                        new BigDecimal("1000.00"),
                        new BigDecimal("10"),
                        TestObjekte.fest());

        AuftragsPosition position20 =
                new AuftragsPosition(
                        20,
                        2,
                        new BigDecimal("1200.00"),
                        BigDecimal.ZERO,
                        TestObjekte.fest());

        auftrag.fuegePositionHinzu(position10);
        auftrag.fuegePositionHinzu(position20);

        assertThat(auftrag.berechneGesamtHt())
                .isEqualByComparingTo("6900.00");
    }

    @Test
    void auftragOhneTechnischeFreigabeSollNichtFreigegebenWerden() {

        Kunde kunde =
                new Kunde(
                        "K-1001",
                        "Société Demo SARL");

        Auftrag auftrag =
                new Auftrag(
                        "A-2026-002",
                        kunde);

        AuftragsPosition position =
                new AuftragsPosition(
                        10,
                        1,
                        new BigDecimal("1500.00"),
                        BigDecimal.ZERO,
                        TestObjekte.fest());

        auftrag.fuegePositionHinzu(position);

        assertThatThrownBy(auftrag::freigeben)
                .isInstanceOf(GeschaeftsregelException.class)
                .satisfies(exception -> {

                    GeschaeftsregelException e =
                            (GeschaeftsregelException) exception;

                    assertThat(e.getFehlerCode())
                            .isEqualTo(
                                    FehlerCode.AUFTRAG_TECHNISCH_NICHT_FREIGEGEBEN);
                });
    }

    @Test
    void technischFreigegebenerAuftragSollFreigegebenWerden() {

        var konfiguration =
                TestObjekte.fest();

        TechnischePruefung technischePruefung =
                new TechnischePruefung();

        technischePruefung.pruefe(konfiguration);

        assertThat(konfiguration.getTechnischerStatus())
                .isEqualTo(TechnischerStatus.FREIGABEBEREIT);

        konfiguration.freigeben();

        Kunde kunde =
                new Kunde(
                        "K-1001",
                        "Société Demo SARL");

        Auftrag auftrag =
                new Auftrag(
                        "A-2026-003",
                        kunde);

        auftrag.fuegePositionHinzu(
                new AuftragsPosition(
                        10,
                        2,
                        new BigDecimal("1500.00"),
                        new BigDecimal("5"),
                        konfiguration));

        auftrag.freigeben();

        assertThat(auftrag.getStatus())
                .isEqualTo(AuftragsStatus.FREIGEGEBEN);
    }
}