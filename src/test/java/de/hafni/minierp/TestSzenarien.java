package de.hafni.minierp;

import java.math.BigDecimal;

import de.hafni.minierp.domain.auftrag.Auftrag;
import de.hafni.minierp.domain.auftrag.AuftragsPosition;
import de.hafni.minierp.domain.auftrag.Kunde;
import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.pruefung.TechnischePruefung;

public final class TestSzenarien {

    private TestSzenarien() {
    }

    public static LichtkuppelKonfiguration
            freigegebeneFesteKonfiguration() {

        LichtkuppelKonfiguration konfiguration =
                TestObjekte.fest();

        new TechnischePruefung()
                .pruefe(konfiguration);

        konfiguration.freigeben();

        return konfiguration;
    }

    public static Auftrag freigegebenerAuftrag6900() {

        LichtkuppelKonfiguration konfiguration =
                freigegebeneFesteKonfiguration();

        Kunde kunde =
                new Kunde(
                        "K-1001",
                        "Société Demo SARL");

        Auftrag auftrag =
                new Auftrag(
                        "A-2026-900",
                        kunde);

        auftrag.fuegePositionHinzu(
                new AuftragsPosition(
                        10,
                        5,
                        new BigDecimal("1000.00"),
                        new BigDecimal("10"),
                        konfiguration));

        auftrag.fuegePositionHinzu(
                new AuftragsPosition(
                        20,
                        2,
                        new BigDecimal("1200.00"),
                        BigDecimal.ZERO,
                        konfiguration));

        auftrag.freigeben();

        return auftrag;
    }
}