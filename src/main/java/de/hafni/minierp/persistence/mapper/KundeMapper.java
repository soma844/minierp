package de.hafni.minierp.persistence.mapper;

import de.hafni.minierp.domain.auftrag.Kunde;
import de.hafni.minierp.persistence.entity.KundeEntity;

public final class KundeMapper {

    private KundeMapper() {
    }

    public static KundeEntity toEntity(Kunde kunde) {

        if (kunde == null) {
            throw new IllegalArgumentException(
                    "Kunde darf nicht null sein."
            );
        }

        return new KundeEntity(
                kunde.getKundenNummer(),
                kunde.getFirmenName()
        );
    }

    public static Kunde toDomain(KundeEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "KundeEntity darf nicht null sein."
            );
        }

        return new Kunde(
                entity.getKundenNummer(),
                entity.getFirmenName()
        );
    }

    public static void updateEntity(
            Kunde kunde,
            KundeEntity entity) {

        if (kunde == null) {
            throw new IllegalArgumentException(
                    "Kunde darf nicht null sein."
            );
        }

        if (entity == null) {
            throw new IllegalArgumentException(
                    "KundeEntity darf nicht null sein."
            );
        }

        entity.aktualisiereFirmenName(
                kunde.getFirmenName()
        );
    }
}