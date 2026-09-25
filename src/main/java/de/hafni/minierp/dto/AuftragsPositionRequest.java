package de.hafni.minierp.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuftragsPositionRequest(

        @Min(
                value = 1,
                message = "Positionsnummer muss größer als 0 sein.")
        int positionsNummer,

        @Min(
                value = 1,
                message = "Menge muss größer als 0 sein.")
        int menge,

        @NotNull(
                message = "Einzelpreis darf nicht fehlen.")
        @DecimalMin(
                value = "0.00",
                message = "Einzelpreis darf nicht negativ sein.")
        BigDecimal einzelPreisHt,

        @NotNull(
                message = "Rabatt darf nicht fehlen.")
        @DecimalMin(
                value = "0.00",
                message = "Rabatt darf nicht negativ sein.")
        @DecimalMax(
                value = "100.00",
                message = "Rabatt darf maximal 100 Prozent betragen.")
        BigDecimal rabattProzent,

        @NotBlank(
                message = "Konfigurationsnummer darf nicht leer sein.")
        String konfigurationsNummer

) {
}