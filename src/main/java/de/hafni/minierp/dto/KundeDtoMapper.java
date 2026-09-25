package de.hafni.minierp.dto;

import de.hafni.minierp.domain.auftrag.Kunde;

public final class KundeDtoMapper {

    private KundeDtoMapper() {
    }

    public static Kunde toDomain(
            KundeRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "KundeRequest darf nicht null sein."
            );
        }

        return new Kunde(
                request.kundenNummer(),
                request.firmenName()
        );
    }

    public static KundeResponse toResponse(
            Kunde kunde) {

        if (kunde == null) {
            throw new IllegalArgumentException(
                    "Kunde darf nicht null sein."
            );
        }

        return new KundeResponse(
                kunde.getKundenNummer(),
                kunde.getFirmenName()
        );
    }
}