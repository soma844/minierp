package de.hafni.minierp.dto;

import java.math.BigDecimal;
import java.util.List;

import de.hafni.minierp.domain.konfiguration.LichtkuppelFunktion;
import de.hafni.minierp.domain.product.AntriebsArt;
import de.hafni.minierp.domain.product.AufsetzkranzTyp;
import de.hafni.minierp.domain.product.FuellungsArt;
import de.hafni.minierp.domain.product.FuellungsAusfuehrung;
import de.hafni.minierp.domain.product.Material;
import de.hafni.minierp.domain.product.VersorgungsSpannung;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record KonfigurationRequest(

        @NotBlank
        String konfigurationsNummer,

        @NotBlank
        String produktCode,

        @NotBlank
        String produktBezeichnung,

        @NotNull
        LichtkuppelFunktion funktion,

        @Valid
        @NotNull
        ProjektRequest projekt,

        @Valid
        @NotNull
        AufsetzkranzRequest aufsetzkranz,

        @Valid
        @NotNull
        FesterRahmenRequest festerRahmen,

        @Valid
        @NotNull
        FuellungRequest fuellung,

        @Valid
        OeffnungsRahmenRequest oeffnungsRahmen,

        @Valid
        AntriebsSystemRequest antriebsSystem) {

    public record ProjektRequest(
            @Positive int breiteMm,
            @Positive int laengeMm,
            @PositiveOrZero int dachNeigungGrad) {
    }

    public record AufsetzkranzRequest(
            @NotNull AufsetzkranzTyp typ,
            @NotNull Material material,
            @Positive int hoeheMm,
            @PositiveOrZero int daemmStaerkeMm) {
    }

    public record FesterRahmenRequest(
            @NotNull Material material,
            boolean thermischGetrennt) {
    }

    public record FuellungRequest(
            @NotNull FuellungsArt art,
            @NotNull FuellungsAusfuehrung ausfuehrung,
            @Positive int dickeMm,
            @NotNull BigDecimal ugWert,
            @NotNull BigDecimal lichtTransmissionProzent,
            @NotNull BigDecimal solarFaktorProzent) {
    }

    public record OeffnungsRahmenRequest(
            @NotNull Material material,
            boolean thermischGetrennt,
            @NotNull BigDecimal gewichtKg) {
    }

    public record AntriebsSystemRequest(
            @Valid
            @NotNull
            SteuerungRequest steuerung,

            @Valid
            @NotNull
            List<AntriebRequest> antriebe) {
    }

    public record SteuerungRequest(
            @NotNull VersorgungsSpannung versorgungsSpannung,
            @NotNull BigDecimal maximalerAusgangsStromAmpere) {
    }

    public record AntriebRequest(
            @NotNull AntriebsArt art,
            @NotNull VersorgungsSpannung versorgungsSpannung,
            @Positive int hubMm,
            @Positive int kraftNewton,
            @NotNull BigDecimal nennStromAmpere) {
    }
}