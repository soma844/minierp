package de.hafni.minierp.persistence.mapper;

import java.util.List;

import de.hafni.minierp.domain.auftrag.Auftrag;
import de.hafni.minierp.domain.auftrag.AuftragsPosition;
import de.hafni.minierp.domain.auftrag.Kunde;
import de.hafni.minierp.persistence.entity.AuftragEntity;
import de.hafni.minierp.persistence.entity.AuftragsPositionEntity;
import de.hafni.minierp.persistence.entity.KundeEntity;
import de.hafni.minierp.persistence.entity.LichtkuppelKonfigurationEntity;

public final class AuftragMapper {

    private AuftragMapper() {
    }

    // ============================================================
    // AUFTRAGSKOPF DOMAIN -> ENTITY
    // ============================================================

    public static AuftragEntity toEntity(
            Auftrag domain,
            KundeEntity kundeEntity) {

        if (domain == null) {
            throw new IllegalArgumentException(
                    "Auftrag darf nicht null sein."
            );
        }

        if (kundeEntity == null) {
            throw new IllegalArgumentException(
                    "KundeEntity darf nicht null sein."
            );
        }

        return new AuftragEntity(
                domain.getAuftragsNummer(),
                domain.getStatus(),
                kundeEntity
        );
    }

    // ============================================================
    // POSITION DOMAIN -> ENTITY
    // ============================================================

    public static AuftragsPositionEntity toEntity(
            AuftragsPosition domain,
            LichtkuppelKonfigurationEntity konfigurationEntity) {

        if (domain == null) {
            throw new IllegalArgumentException(
                    "Auftragsposition darf nicht null sein."
            );
        }

        if (konfigurationEntity == null) {
            throw new IllegalArgumentException(
                    "KonfigurationEntity darf nicht null sein."
            );
        }

        return new AuftragsPositionEntity(
                domain.getPositionsNummer(),
                domain.getMenge(),
                domain.getEinzelPreisHt(),
                domain.getRabattProzent(),
                konfigurationEntity
        );
    }

    // ============================================================
    // ENTITY -> DOMAIN
    // ============================================================

    public static Auftrag toDomain(
            AuftragEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "AuftragEntity darf nicht null sein."
            );
        }

        Kunde kunde =
                new Kunde(
                        entity.getKunde()
                                .getKundenNummer(),
                        entity.getKunde()
                                .getFirmenName()
                );

        List<AuftragsPosition> positionen =
                entity.getPositionen()
                        .stream()
                        .map(AuftragMapper::toDomain)
                        .toList();

        return Auftrag.rehydrieren(
                entity.getAuftragsNummer(),
                kunde,
                positionen,
                entity.getStatus()
        );
    }

    // ============================================================
    // POSITION ENTITY -> DOMAIN
    // ============================================================

    private static AuftragsPosition toDomain(
            AuftragsPositionEntity entity) {

        return new AuftragsPosition(
                entity.getPositionsNummer(),
                entity.getMenge(),
                entity.getEinzelPreisHt(),
                entity.getRabattProzent(),

                LichtkuppelKonfigurationMapper
                        .toDomain(
                                entity.getKonfiguration()
                        )
        );
    }
}