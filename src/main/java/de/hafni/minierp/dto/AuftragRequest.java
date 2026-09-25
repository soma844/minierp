package de.hafni.minierp.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record AuftragRequest(

        @NotBlank(
                message = "Auftragsnummer darf nicht leer sein.")
        String auftragsNummer,

        @NotBlank(
                message = "Kundennummer darf nicht leer sein.")
        String kundenNummer,

        @NotEmpty(
                message = "Ein Auftrag benötigt mindestens eine Position.")
        List<@Valid AuftragsPositionRequest> positionen

) {
}