package de.hafni.minierp.dto;

import de.hafni.minierp.domain.konfiguration.LichtkuppelFunktion;
import de.hafni.minierp.domain.konfiguration.TechnischerStatus;

public record KonfigurationResponse(

        String konfigurationsNummer,
        String produktCode,
        String produktBezeichnung,
        LichtkuppelFunktion funktion,
        TechnischerStatus technischerStatus) {
}