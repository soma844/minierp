package de.hafni.minierp.dto;

import java.util.List;

import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.domain.konfiguration.ProjektAnforderungen;
import de.hafni.minierp.domain.product.Antrieb;
import de.hafni.minierp.domain.product.AntriebsSystem;
import de.hafni.minierp.domain.product.Aufsetzkranz;
import de.hafni.minierp.domain.product.FesterRahmen;
import de.hafni.minierp.domain.product.Fuellung;
import de.hafni.minierp.domain.product.OeffnungsRahmen;
import de.hafni.minierp.domain.product.ProduktFamilie;
import de.hafni.minierp.domain.product.Steuerung;

public final class LichtkuppelKonfigurationDtoMapper {

    private LichtkuppelKonfigurationDtoMapper() {
    }

    public static LichtkuppelKonfiguration toDomain(
            KonfigurationRequest request) {

        OeffnungsRahmen oeffnungsRahmen = null;

        if (request.oeffnungsRahmen() != null) {

            var dto = request.oeffnungsRahmen();

            oeffnungsRahmen =
                    new OeffnungsRahmen(
                            dto.material(),
                            dto.thermischGetrennt(),
                            dto.gewichtKg());
        }

        AntriebsSystem antriebsSystem = null;

        if (request.antriebsSystem() != null) {

            var systemDto =
                    request.antriebsSystem();

            Steuerung steuerung =
                    new Steuerung(
                            systemDto
                                    .steuerung()
                                    .versorgungsSpannung(),

                            systemDto
                                    .steuerung()
                                    .maximalerAusgangsStromAmpere());

            List<Antrieb> antriebe =
                    systemDto
                            .antriebe()
                            .stream()
                            .map(dto ->
                                    new Antrieb(
                                            dto.art(),
                                            dto.versorgungsSpannung(),
                                            dto.hubMm(),
                                            dto.kraftNewton(),
                                            dto.nennStromAmpere()))
                            .toList();

            antriebsSystem =
                    new AntriebsSystem(
                            antriebe,
                            steuerung);
        }

        return new LichtkuppelKonfiguration(

                request.konfigurationsNummer(),

                new ProduktFamilie(
                        request.produktCode(),
                        request.produktBezeichnung()),

                request.funktion(),

                new ProjektAnforderungen(
                        request.projekt().breiteMm(),
                        request.projekt().laengeMm(),
                        request.projekt().dachNeigungGrad()),

                new Aufsetzkranz(
                        request.aufsetzkranz().typ(),
                        request.aufsetzkranz().material(),
                        request.aufsetzkranz().hoeheMm(),
                        request.aufsetzkranz().daemmStaerkeMm()),

                new FesterRahmen(
                        request.festerRahmen().material(),
                        request.festerRahmen().thermischGetrennt()),

                new Fuellung(
                        request.fuellung().art(),
                        request.fuellung().ausfuehrung(),
                        request.fuellung().dickeMm(),
                        request.fuellung().ugWert(),
                        request.fuellung().lichtTransmissionProzent(),
                        request.fuellung().solarFaktorProzent()),

                oeffnungsRahmen,
                antriebsSystem);
    }

    public static KonfigurationResponse toResponse(
            LichtkuppelKonfiguration domain) {

        return new KonfigurationResponse(
                domain.getKonfigurationsNummer(),
                domain.getProduktFamilie().getCode(),
                domain.getProduktFamilie().getBezeichnung(),
                domain.getFunktion(),
                domain.getTechnischerStatus());
    }
}