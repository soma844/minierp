package de.hafni.minierp.dto;

import java.time.LocalDate;

import de.hafni.minierp.domain.production.ProduktionsStatus;

public record ProduktionsAuftragResponse(

        String produktionsNummer,
        String auftragsNummer,
        LocalDate geplanterStart,
        ProduktionsStatus status

) {
}