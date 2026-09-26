package de.hafni.minierp.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record AuftragRequest(

        @NotBlank(
                message = "Auftragsnummer darf nicht leer sein.")
        @Pattern(
                regexp = "^AUF-\\d{5}$",
                message = "Auftragsnummer muss dem Format AUF-10001 entsprechen.")
        String auftragsNummer,

        @NotBlank(
                message = "Kundennummer darf nicht leer sein.")
        @Pattern(
                regexp = "^K-\\d{5}$",
                message = "Kundennummer muss dem Format K-10001 entsprechen.")
        String kundenNummer,

        @NotEmpty(
                message = "Ein Auftrag benötigt mindestens eine Position.")
        List<@Valid AuftragsPositionRequest> positionen

) {
}