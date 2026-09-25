package de.hafni.minierp.dto;

import de.hafni.minierp.domain.auftrag.Auftrag;
import de.hafni.minierp.domain.auftrag.AuftragsPosition;

public final class AuftragDtoMapper {

    private AuftragDtoMapper() {
    }

    public static AuftragResponse toResponse(
            Auftrag auftrag) {

        if (auftrag == null) {
            throw new IllegalArgumentException(
                    "Auftrag darf nicht null sein."
            );
        }

        return new AuftragResponse(

                auftrag.getAuftragsNummer(),

                auftrag.getKunde()
                        .getKundenNummer(),

                auftrag.getKunde()
                        .getFirmenName(),

                auftrag.getStatus(),

                auftrag.getPositionen()
                        .stream()
                        .map(AuftragDtoMapper::toResponse)
                        .toList(),

                auftrag.berechneGesamtHt()
        );
    }

    private static AuftragsPositionResponse toResponse(
            AuftragsPosition position) {

        return new AuftragsPositionResponse(

                position.getPositionsNummer(),
                position.getMenge(),
                position.getEinzelPreisHt(),
                position.getRabattProzent(),

                position.getKonfiguration()
                        .getKonfigurationsNummer(),

                position.berechneBruttoHt(),
                position.berechneRabattBetrag(),
                position.berechneGesamtHt()
        );
    }
}