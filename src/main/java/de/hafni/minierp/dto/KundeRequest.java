package de.hafni.minierp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record KundeRequest(

        @NotBlank(message = "Kundennummer darf nicht leer sein.")
        @Size(max = 50, message = "Kundennummer darf maximal 50 Zeichen enthalten.")
        String kundenNummer,

        @NotBlank(message = "Firmenname darf nicht leer sein.")
        @Size(max = 200, message = "Firmenname darf maximal 200 Zeichen enthalten.")
        String firmenName

) {
}