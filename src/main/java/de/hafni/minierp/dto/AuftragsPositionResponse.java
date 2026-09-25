package de.hafni.minierp.dto;

import java.math.BigDecimal;

public record AuftragsPositionResponse(

        int positionsNummer,
        int menge,
        BigDecimal einzelPreisHt,
        BigDecimal rabattProzent,

        String konfigurationsNummer,

        BigDecimal bruttoHt,
        BigDecimal rabattBetrag,
        BigDecimal gesamtHt

) {
}