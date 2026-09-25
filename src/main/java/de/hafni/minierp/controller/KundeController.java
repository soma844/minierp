package de.hafni.minierp.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hafni.minierp.domain.auftrag.Kunde;
import de.hafni.minierp.dto.KundeDtoMapper;
import de.hafni.minierp.dto.KundeRequest;
import de.hafni.minierp.dto.KundeResponse;
import de.hafni.minierp.service.KundeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/kunden")
@Tag(
        name = "Kunden",
        description = "Verwaltung der Kundenstammdaten"
)
public class KundeController {

    private final KundeService service;

    public KundeController(
            KundeService service) {

        this.service = service;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @PostMapping
    @Operation(
            summary = "Kunde anlegen",
            description = "Legt einen neuen Kunden an."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Kunde erfolgreich angelegt"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ungültige Kundendaten"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Kundennummer existiert bereits")
    })
    public ResponseEntity<KundeResponse> anlegen(
            @Valid
            @RequestBody
            KundeRequest request) {

        Kunde kunde =
                KundeDtoMapper.toDomain(request);

        Kunde gespeichert =
                service.anlegen(kunde);

        KundeResponse response =
                KundeDtoMapper.toResponse(gespeichert);

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/kunden/"
                                        + gespeichert
                                                .getKundenNummer()))
                .body(response);
    }

    // ============================================================
    // READ ONE
    // ============================================================

    @GetMapping("/{kundenNummer}")
    @Operation(
            summary = "Kunde laden"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Kunde gefunden"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kunde nicht gefunden")
    })
    public KundeResponse finden(

            @Parameter(
                    description = "Fachliche Kundennummer",
                    example = "K-1001"
            )
            @PathVariable
            String kundenNummer) {

        return KundeDtoMapper.toResponse(
                service.finden(kundenNummer)
        );
    }

    // ============================================================
    // READ ALL
    // ============================================================

    @GetMapping
    @Operation(
            summary = "Alle Kunden laden"
    )
    public List<KundeResponse> alleLaden() {

        return service
                .alleLaden()
                .stream()
                .map(KundeDtoMapper::toResponse)
                .toList();
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @PutMapping("/{kundenNummer}")
    @Operation(
            summary = "Kunde aktualisieren",
            description = """
                    Aktualisiert die Kundenstammdaten.
                    Die bestehende Kundennummer bleibt erhalten.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Kunde aktualisiert"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ungültige Kundendaten"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kunde nicht gefunden")
    })
    public KundeResponse aktualisieren(
            @PathVariable
            String kundenNummer,

            @Valid
            @RequestBody
            KundeRequest request) {

        Kunde neueDaten =
                KundeDtoMapper.toDomain(request);

        return KundeDtoMapper.toResponse(
                service.aktualisieren(
                        kundenNummer,
                        neueDaten
                )
        );
    }

    // ============================================================
    // DELETE
    // ============================================================

    @DeleteMapping("/{kundenNummer}")
    @Operation(
            summary = "Kunde löschen",
            description = """
                    Löscht einen Kunden nur dann,
                    wenn noch keine Aufträge vorhanden sind.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Kunde gelöscht"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kunde nicht gefunden"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Kunde besitzt bereits Aufträge")
    })
    public ResponseEntity<Void> loeschen(
            @PathVariable
            String kundenNummer) {

        service.loeschen(kundenNummer);

        return ResponseEntity
                .noContent()
                .build();
    }
}