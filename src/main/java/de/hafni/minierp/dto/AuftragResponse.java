package de.hafni.minierp.dto;

import java.math.BigDecimal;
import java.util.List;

import de.hafni.minierp.domain.auftrag.AuftragsStatus;

public record AuftragResponse(

        String auftragsNummer,
        String kundenNummer,
        String firmenName,
        AuftragsStatus status,

        List<AuftragsPositionResponse> positionen,

        BigDecimal gesamtHt

) {
}