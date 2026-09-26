package de.hafni.minierp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record KundeRequest(

        @NotBlank(message = "Kundennummer darf nicht leer sein.")
        @Pattern(
                regexp = "^K-\\d{5}$",
                message = "Kundennummer muss dem Format K-10001 entsprechen."
        )
        String kundenNummer,

        @NotBlank(message = "Firmenname darf nicht leer sein.")
        @Size(
                max = 200,
                message = "Firmenname darf maximal 200 Zeichen enthalten."
        )
        String firmenName

) {
}