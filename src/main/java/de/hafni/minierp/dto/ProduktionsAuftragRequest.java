package de.hafni.minierp.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProduktionsAuftragRequest(

        @NotBlank(
                message = "Produktionsnummer darf nicht leer sein.")
        String produktionsNummer,

        @NotBlank(
                message = "Auftragsnummer darf nicht leer sein.")
        String auftragsNummer,

        @NotNull(
                message = "Geplanter Start darf nicht fehlen.")
        LocalDate geplanterStart

) {
}