package de.hafni.minierp.persistence.mapper;

import de.hafni.minierp.domain.production.ProduktionsAuftrag;
import de.hafni.minierp.persistence.entity.AuftragEntity;
import de.hafni.minierp.persistence.entity.ProduktionsAuftragEntity;

public final class ProduktionsAuftragMapper {

    private ProduktionsAuftragMapper() {
    }

    public static ProduktionsAuftragEntity toEntity(
            ProduktionsAuftrag domain,
            AuftragEntity auftragEntity) {

        if (domain == null) {
            throw new IllegalArgumentException(
                    "Produktionsauftrag darf nicht null sein.");
        }

        if (auftragEntity == null) {
            throw new IllegalArgumentException(
                    "AuftragEntity darf nicht null sein.");
        }

        return new ProduktionsAuftragEntity(
                domain.getProduktionsNummer(),
                auftragEntity,
                domain.getGeplanterStart(),
                domain.getStatus()
        );
    }

    public static ProduktionsAuftrag toDomain(
            ProduktionsAuftragEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "ProduktionsAuftragEntity darf nicht null sein.");
        }

        return ProduktionsAuftrag.rehydrieren(
                entity.getProduktionsNummer(),

                AuftragMapper.toDomain(
                        entity.getAuftrag()),

                entity.getGeplanterStart(),
                entity.getStatus()
        );
    }
}