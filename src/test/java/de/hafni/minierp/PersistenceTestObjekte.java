package de.hafni.minierp;

import java.math.BigDecimal;

import de.hafni.minierp.domain.konfiguration.LichtkuppelFunktion;
import de.hafni.minierp.domain.konfiguration.TechnischerStatus;
import de.hafni.minierp.domain.product.AntriebsArt;
import de.hafni.minierp.domain.product.AufsetzkranzTyp;
import de.hafni.minierp.domain.product.FuellungsArt;
import de.hafni.minierp.domain.product.FuellungsAusfuehrung;
import de.hafni.minierp.domain.product.Material;
import de.hafni.minierp.domain.product.VersorgungsSpannung;
import de.hafni.minierp.persistence.entity.AntriebEntity;
import de.hafni.minierp.persistence.entity.AntriebsSystemEntity;
import de.hafni.minierp.persistence.entity.AufsetzkranzEmbeddable;
import de.hafni.minierp.persistence.entity.FesterRahmenEmbeddable;
import de.hafni.minierp.persistence.entity.FuellungEmbeddable;
import de.hafni.minierp.persistence.entity.LichtkuppelKonfigurationEntity;
import de.hafni.minierp.persistence.entity.OeffnungsRahmenEmbeddable;
import de.hafni.minierp.persistence.entity.ProjektAnforderungenEmbeddable;
import de.hafni.minierp.persistence.entity.SteuerungEmbeddable;

public final class PersistenceTestObjekte {

    private PersistenceTestObjekte() {
    }

    public static LichtkuppelKonfigurationEntity festeKonfiguration() {

        return new LichtkuppelKonfigurationEntity(
                "LK-DB-001",
                "ECOLUX-PREMIUM-ALU",
                "ECOLUX Premium Alu",
                LichtkuppelFunktion.FEST,
                TechnischerStatus.FREIGEGEBEN,

                new ProjektAnforderungenEmbeddable(
                        1200,
                        1500,
                        10),

                new AufsetzkranzEmbeddable(
                        AufsetzkranzTyp.STANDARD,
                        Material.VERZINKTER_STAHL,
                        400,
                        50),

                new FesterRahmenEmbeddable(
                        Material.ALUMINIUM,
                        true),

                new FuellungEmbeddable(
                        FuellungsArt.PCA,
                        FuellungsAusfuehrung.OPAL,
                        32,
                        new BigDecimal("1.3"),
                        new BigDecimal("38"),
                        new BigDecimal("40")),

                null,
                null);
    }

    public static LichtkuppelKonfigurationEntity lueftungsKonfiguration() {

        AntriebsSystemEntity antriebsSystem =
                new AntriebsSystemEntity(
                        new SteuerungEmbeddable(
                                VersorgungsSpannung.AC_230_V,
                                new BigDecimal("1.00")));

        antriebsSystem.fuegeAntriebHinzu(
                neuerKettenAntrieb());

        antriebsSystem.fuegeAntriebHinzu(
                neuerKettenAntrieb());

        return new LichtkuppelKonfigurationEntity(
                "LK-DB-002",
                "ECOLUX-PREMIUM-ALU",
                "ECOLUX Premium Alu",
                LichtkuppelFunktion.LUEFTUNG,
                TechnischerStatus.FREIGEGEBEN,

                new ProjektAnforderungenEmbeddable(
                        1500,
                        2000,
                        10),

                new AufsetzkranzEmbeddable(
                        AufsetzkranzTyp.STANDARD,
                        Material.VERZINKTER_STAHL,
                        400,
                        50),

                new FesterRahmenEmbeddable(
                        Material.ALUMINIUM,
                        true),

                new FuellungEmbeddable(
                        FuellungsArt.PCA,
                        FuellungsAusfuehrung.OPAL,
                        32,
                        new BigDecimal("1.3"),
                        new BigDecimal("38"),
                        new BigDecimal("40")),

                new OeffnungsRahmenEmbeddable(
                        Material.ALUMINIUM,
                        true,
                        new BigDecimal("15.50")),

                antriebsSystem);
    }

    private static AntriebEntity neuerKettenAntrieb() {

        return new AntriebEntity(
                AntriebsArt.KETTENANTRIEB,
                VersorgungsSpannung.AC_230_V,
                300,
                300,
                new BigDecimal("0.24"));
    }
}