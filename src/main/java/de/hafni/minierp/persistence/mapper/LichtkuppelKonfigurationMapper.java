package de.hafni.minierp.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.domain.konfiguration.ProjektAnforderungen;
import de.hafni.minierp.domain.product.Antrieb;
import de.hafni.minierp.domain.product.AntriebsSystem;
import de.hafni.minierp.domain.product.Aufsetzkranz;
import de.hafni.minierp.domain.product.FesterRahmen;
import de.hafni.minierp.domain.product.Fuellung;
import de.hafni.minierp.domain.product.OeffnungsRahmen;
import de.hafni.minierp.domain.product.ProduktFamilie;
import de.hafni.minierp.domain.product.Steuerung;
import de.hafni.minierp.persistence.entity.AntriebEntity;
import de.hafni.minierp.persistence.entity.AntriebsSystemEntity;
import de.hafni.minierp.persistence.entity.AufsetzkranzEmbeddable;
import de.hafni.minierp.persistence.entity.FesterRahmenEmbeddable;
import de.hafni.minierp.persistence.entity.FuellungEmbeddable;
import de.hafni.minierp.persistence.entity.LichtkuppelKonfigurationEntity;
import de.hafni.minierp.persistence.entity.OeffnungsRahmenEmbeddable;
import de.hafni.minierp.persistence.entity.ProjektAnforderungenEmbeddable;
import de.hafni.minierp.persistence.entity.SteuerungEmbeddable;

public final class LichtkuppelKonfigurationMapper {

    private LichtkuppelKonfigurationMapper() {
    }

    // =========================================================
    // DOMAIN -> NEUE ENTITY
    // =========================================================

    public static LichtkuppelKonfigurationEntity toEntity(
            LichtkuppelKonfiguration domain) {

        if (domain == null) {
            throw new IllegalArgumentException(
                    "Domain-Konfiguration darf nicht null sein.");
        }

        OeffnungsRahmenEmbeddable oeffnungsRahmenEntity =
                toEntityOeffnungsRahmen(
                        domain.getOeffnungsRahmen());

        AntriebsSystemEntity antriebsSystemEntity =
                toEntityAntriebsSystem(
                        domain.getAntriebsSystem());

        return new LichtkuppelKonfigurationEntity(
                domain.getKonfigurationsNummer(),

                domain.getProduktFamilie().getCode(),
                domain.getProduktFamilie().getBezeichnung(),

                domain.getFunktion(),
                domain.getTechnischerStatus(),

                new ProjektAnforderungenEmbeddable(
                        domain.getProjektAnforderungen()
                                .getDachOeffnungBreiteMm(),
                        domain.getProjektAnforderungen()
                                .getDachOeffnungLaengeMm(),
                        domain.getProjektAnforderungen()
                                .getDachNeigungGrad()),

                new AufsetzkranzEmbeddable(
                        domain.getAufsetzkranz().getTyp(),
                        domain.getAufsetzkranz().getMaterial(),
                        domain.getAufsetzkranz().getHoeheMm(),
                        domain.getAufsetzkranz().getDaemmStaerkeMm()),

                new FesterRahmenEmbeddable(
                        domain.getFesterRahmen().getMaterial(),
                        domain.getFesterRahmen()
                                .isThermischGetrennt()),

                new FuellungEmbeddable(
                        domain.getFuellung().getArt(),
                        domain.getFuellung().getAusfuehrung(),
                        domain.getFuellung().getDickeMm(),
                        domain.getFuellung().getUgWert(),
                        domain.getFuellung()
                                .getLichtTransmissionProzent(),
                        domain.getFuellung()
                                .getSolarFaktorProzent()),

                oeffnungsRahmenEntity,
                antriebsSystemEntity);
    }

    // =========================================================
    // DOMAIN -> BESTEHENDE ENTITY AKTUALISIEREN
    // =========================================================

    public static void updateEntity(
            LichtkuppelKonfiguration domain,
            LichtkuppelKonfigurationEntity entity) {

        if (domain == null) {
            throw new IllegalArgumentException(
                    "Domain-Konfiguration darf nicht null sein.");
        }

        if (entity == null) {
            throw new IllegalArgumentException(
                    "Entity darf nicht null sein.");
        }

        OeffnungsRahmenEmbeddable oeffnungsRahmenEntity =
                toEntityOeffnungsRahmen(
                        domain.getOeffnungsRahmen());

        AntriebsSystemEntity antriebsSystemEntity =
                toEntityAntriebsSystem(
                        domain.getAntriebsSystem());

        entity.aktualisiereKonfigurationsDaten(
                domain.getProduktFamilie().getCode(),
                domain.getProduktFamilie().getBezeichnung(),

                domain.getFunktion(),

                new ProjektAnforderungenEmbeddable(
                        domain.getProjektAnforderungen()
                                .getDachOeffnungBreiteMm(),
                        domain.getProjektAnforderungen()
                                .getDachOeffnungLaengeMm(),
                        domain.getProjektAnforderungen()
                                .getDachNeigungGrad()),

                new AufsetzkranzEmbeddable(
                        domain.getAufsetzkranz().getTyp(),
                        domain.getAufsetzkranz().getMaterial(),
                        domain.getAufsetzkranz().getHoeheMm(),
                        domain.getAufsetzkranz().getDaemmStaerkeMm()),

                new FesterRahmenEmbeddable(
                        domain.getFesterRahmen().getMaterial(),
                        domain.getFesterRahmen()
                                .isThermischGetrennt()),

                new FuellungEmbeddable(
                        domain.getFuellung().getArt(),
                        domain.getFuellung().getAusfuehrung(),
                        domain.getFuellung().getDickeMm(),
                        domain.getFuellung().getUgWert(),
                        domain.getFuellung()
                                .getLichtTransmissionProzent(),
                        domain.getFuellung()
                                .getSolarFaktorProzent()),

                oeffnungsRahmenEntity,
                antriebsSystemEntity);
    }

    // =========================================================
    // ENTITY -> DOMAIN
    // =========================================================

    public static LichtkuppelKonfiguration toDomain(
            LichtkuppelKonfigurationEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "Entity darf nicht null sein.");
        }

        ProduktFamilie produktFamilie =
                new ProduktFamilie(
                        entity.getProduktCode(),
                        entity.getProduktBezeichnung());

        ProjektAnforderungenEmbeddable projektEntity =
                entity.getProjektAnforderungen();

        ProjektAnforderungen projektAnforderungen =
                new ProjektAnforderungen(
                        projektEntity.getDachOeffnungBreiteMm(),
                        projektEntity.getDachOeffnungLaengeMm(),
                        projektEntity.getDachNeigungGrad());

        AufsetzkranzEmbeddable kranzEntity =
                entity.getAufsetzkranz();

        Aufsetzkranz aufsetzkranz =
                new Aufsetzkranz(
                        kranzEntity.getTyp(),
                        kranzEntity.getMaterial(),
                        kranzEntity.getHoeheMm(),
                        kranzEntity.getDaemmStaerkeMm());

        FesterRahmenEmbeddable festerRahmenEntity =
                entity.getFesterRahmen();

        FesterRahmen festerRahmen =
                new FesterRahmen(
                        festerRahmenEntity.getMaterial(),
                        festerRahmenEntity.isThermischGetrennt());

        FuellungEmbeddable fuellungEntity =
                entity.getFuellung();

        Fuellung fuellung =
                new Fuellung(
                        fuellungEntity.getArt(),
                        fuellungEntity.getAusfuehrung(),
                        fuellungEntity.getDickeMm(),
                        fuellungEntity.getUgWert(),
                        fuellungEntity.getLichtTransmissionProzent(),
                        fuellungEntity.getSolarFaktorProzent());

        OeffnungsRahmen oeffnungsRahmen =
                toDomainOeffnungsRahmen(
                        entity.getOeffnungsRahmen());

        AntriebsSystem antriebsSystem =
                toDomainAntriebsSystem(
                        entity.getAntriebsSystem());

        return LichtkuppelKonfiguration.rehydrieren(
                entity.getKonfigurationsNummer(),
                produktFamilie,
                entity.getFunktion(),
                projektAnforderungen,
                aufsetzkranz,
                festerRahmen,
                fuellung,
                oeffnungsRahmen,
                antriebsSystem,
                entity.getTechnischerStatus());
    }

    // =========================================================
    // DOMAIN -> ENTITY HILFSMETHODEN
    // =========================================================

    private static OeffnungsRahmenEmbeddable toEntityOeffnungsRahmen(
            OeffnungsRahmen domain) {

        if (domain == null) {
            return null;
        }

        return new OeffnungsRahmenEmbeddable(
                domain.getMaterial(),
                domain.isThermischGetrennt(),
                domain.getGewichtKg());
    }

    private static AntriebsSystemEntity toEntityAntriebsSystem(
            AntriebsSystem domain) {

        if (domain == null) {
            return null;
        }

        Steuerung steuerung =
                domain.getSteuerung();

        AntriebsSystemEntity entity =
                new AntriebsSystemEntity(
                        new SteuerungEmbeddable(
                                steuerung.getVersorgungsSpannung(),
                                steuerung
                                        .getMaximalerAusgangsStromAmpere()));

        for (Antrieb antrieb :
                domain.getAntriebe()) {

            entity.fuegeAntriebHinzu(
                    new AntriebEntity(
                            antrieb.getArt(),
                            antrieb.getVersorgungsSpannung(),
                            antrieb.getHubMm(),
                            antrieb.getKraftNewton(),
                            antrieb.getNennStromAmpere()));
        }

        return entity;
    }

    // =========================================================
    // ENTITY -> DOMAIN HILFSMETHODEN
    // =========================================================

    private static OeffnungsRahmen toDomainOeffnungsRahmen(
            OeffnungsRahmenEmbeddable entity) {

        if (entity == null) {
            return null;
        }

        return new OeffnungsRahmen(
                entity.getMaterial(),
                entity.isThermischGetrennt(),
                entity.getGewichtKg());
    }

    private static AntriebsSystem toDomainAntriebsSystem(
            AntriebsSystemEntity entity) {

        if (entity == null) {
            return null;
        }

        SteuerungEmbeddable steuerungEntity =
                entity.getSteuerung();

        Steuerung steuerung =
                new Steuerung(
                        steuerungEntity.getVersorgungsSpannung(),
                        steuerungEntity
                                .getMaximalerAusgangsStromAmpere());

        List<Antrieb> antriebe =
                new ArrayList<>();

        for (AntriebEntity antriebEntity :
                entity.getAntriebe()) {

            antriebe.add(
                    new Antrieb(
                            antriebEntity.getArt(),
                            antriebEntity.getVersorgungsSpannung(),
                            antriebEntity.getHubMm(),
                            antriebEntity.getKraftNewton(),
                            antriebEntity.getNennStromAmpere()));
        }

        return new AntriebsSystem(
                antriebe,
                steuerung);
    }
}