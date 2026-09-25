package de.hafni.minierp.dto;

import de.hafni.minierp.domain.production.ProduktionsAuftrag;

public final class ProduktionsAuftragDtoMapper {

    private ProduktionsAuftragDtoMapper() {
    }

    public static ProduktionsAuftragResponse toResponse(
            ProduktionsAuftrag produktionsAuftrag) {

        if (produktionsAuftrag == null) {
            throw new IllegalArgumentException(
                    "Produktionsauftrag darf nicht null sein.");
        }

        return new ProduktionsAuftragResponse(
                produktionsAuftrag.getProduktionsNummer(),

                produktionsAuftrag
                        .getAuftrag()
                        .getAuftragsNummer(),

                produktionsAuftrag.getGeplanterStart(),
                produktionsAuftrag.getStatus()
        );
    }
}