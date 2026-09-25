package de.hafni.minierp;

import java.math.BigDecimal;
import java.util.List;

import de.hafni.minierp.domain.konfiguration.LichtkuppelFunktion;
import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.domain.konfiguration.ProjektAnforderungen;
import de.hafni.minierp.domain.product.Antrieb;
import de.hafni.minierp.domain.product.AntriebsArt;
import de.hafni.minierp.domain.product.AntriebsSystem;
import de.hafni.minierp.domain.product.Aufsetzkranz;
import de.hafni.minierp.domain.product.AufsetzkranzTyp;
import de.hafni.minierp.domain.product.FesterRahmen;
import de.hafni.minierp.domain.product.Fuellung;
import de.hafni.minierp.domain.product.FuellungsArt;
import de.hafni.minierp.domain.product.FuellungsAusfuehrung;
import de.hafni.minierp.domain.product.Material;
import de.hafni.minierp.domain.product.OeffnungsRahmen;
import de.hafni.minierp.domain.product.ProduktFamilie;
import de.hafni.minierp.domain.product.Steuerung;
import de.hafni.minierp.domain.product.VersorgungsSpannung;

public final class TestObjekte {

    private TestObjekte() {
    }

    public static ProduktFamilie produktFamilie() {
        return new ProduktFamilie(
                "ECOLUX-PREMIUM-ALU",
                "ECOLUX Premium Alu");
    }

    public static ProjektAnforderungen projektAnforderungen(
            double dachNeigungGrad) {

        return new ProjektAnforderungen(
                1200,
                1500,
                dachNeigungGrad);
    }

    public static Aufsetzkranz aufsetzkranz() {
        return new Aufsetzkranz(
                AufsetzkranzTyp.STANDARD,
                Material.VERZINKTER_STAHL,
                400,
                50);
    }

    public static FesterRahmen festerRahmen() {
        return new FesterRahmen(
                Material.ALUMINIUM,
                true);
    }

    public static Fuellung fuellung() {
        return new Fuellung(
                FuellungsArt.PCA,
                FuellungsAusfuehrung.OPAL,
                32,
                new BigDecimal("1.3"),
                new BigDecimal("38"),
                new BigDecimal("40"));
    }

    public static OeffnungsRahmen oeffnungsRahmen() {
        return new OeffnungsRahmen(
                Material.ALUMINIUM,
                true,
                new BigDecimal("15.5"));
    }

    public static Antrieb antrieb() {
        return new Antrieb(
                AntriebsArt.KETTENANTRIEB,
                VersorgungsSpannung.AC_230_V,
                300,
                300,
                new BigDecimal("0.24"));
    }

    public static Steuerung steuerung(
            BigDecimal maxStrom) {

        return new Steuerung(
                VersorgungsSpannung.AC_230_V,
                maxStrom);
    }

    public static AntriebsSystem antriebsSystem(
            BigDecimal maxStrom) {

        return new AntriebsSystem(
                List.of(antrieb()),
                steuerung(maxStrom));
    }

    public static LichtkuppelKonfiguration lueftung(
            double dachNeigungGrad,
            BigDecimal maxStrom) {

        return new LichtkuppelKonfiguration(
                "LK-TEST-001",
                produktFamilie(),
                LichtkuppelFunktion.LUEFTUNG,
                projektAnforderungen(dachNeigungGrad),
                aufsetzkranz(),
                festerRahmen(),
                fuellung(),
                oeffnungsRahmen(),
                antriebsSystem(maxStrom));
    }

    public static LichtkuppelKonfiguration fest() {

        return new LichtkuppelKonfiguration(
                "LK-TEST-002",
                produktFamilie(),
                LichtkuppelFunktion.FEST,
                projektAnforderungen(10),
                aufsetzkranz(),
                festerRahmen(),
                fuellung(),
                null,
                null);
    }
}