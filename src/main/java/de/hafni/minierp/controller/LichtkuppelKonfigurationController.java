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

import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.dto.KonfigurationRequest;
import de.hafni.minierp.dto.KonfigurationResponse;
import de.hafni.minierp.dto.LichtkuppelKonfigurationDtoMapper;
import de.hafni.minierp.dto.PruefErgebnisResponse;
import de.hafni.minierp.service.LichtkuppelKonfigurationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/konfigurationen")
@Tag(
        name = "Lichtkuppel-Konfigurationen",
        description = """
                Verwaltung, technische Prüfung und Freigabe
                von Lichtkuppel-Konfigurationen.
                """
)
public class LichtkuppelKonfigurationController {

    private final LichtkuppelKonfigurationService service;

    public LichtkuppelKonfigurationController(
            LichtkuppelKonfigurationService service) {

        this.service = service;
    }

    // ============================================================
    // ERSTELLEN
    // ============================================================

    @PostMapping
    @Operation(
            summary = "Konfiguration anlegen",
            description = """
                    Legt eine neue Lichtkuppel-Konfiguration an.

                    Eine neu angelegte Konfiguration befindet sich
                    zunächst im technischen Status ENTWURF.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Konfiguration erfolgreich angelegt"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ungültige Eingabedaten"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Geschäftsregel verletzt"
            )
    })
    public ResponseEntity<KonfigurationResponse> anlegen(
            @Valid
            @RequestBody
            KonfigurationRequest request) {

        LichtkuppelKonfiguration domain =
                LichtkuppelKonfigurationDtoMapper
                        .toDomain(request);

        LichtkuppelKonfiguration gespeichert =
                service.anlegen(domain);

        KonfigurationResponse response =
                LichtkuppelKonfigurationDtoMapper
                        .toResponse(gespeichert);

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/konfigurationen/"
                                        + gespeichert
                                                .getKonfigurationsNummer()))
                .body(response);
    }

    // ============================================================
    // LESEN
    // ============================================================

    @GetMapping("/{nummer}")
    @Operation(
            summary = "Konfiguration laden",
            description = """
                    Lädt eine einzelne Lichtkuppel-Konfiguration
                    anhand ihrer fachlichen Konfigurationsnummer.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Konfiguration gefunden"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Konfiguration nicht gefunden"
            )
    })
    public KonfigurationResponse finden(

            @Parameter(
                    description = "Fachliche Konfigurationsnummer",
                    example = "LK-HTTP-001"
            )
            @PathVariable
            String nummer) {

        return LichtkuppelKonfigurationDtoMapper
                .toResponse(
                        service.finden(nummer));
    }

    // ============================================================
    // ALLE LESEN
    // ============================================================

    @GetMapping
    @Operation(
            summary = "Alle Konfigurationen laden",
            description = """
                    Liefert alle gespeicherten
                    Lichtkuppel-Konfigurationen.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Konfigurationen erfolgreich geladen"
    )
    public List<KonfigurationResponse> alleLaden() {

        return service
                .alleLaden()
                .stream()
                .map(
                        LichtkuppelKonfigurationDtoMapper
                                ::toResponse)
                .toList();
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @PutMapping("/{nummer}")
    @Operation(
            summary = "Konfiguration aktualisieren",
            description = """
                    Aktualisiert die technischen Daten einer
                    bestehenden Lichtkuppel-Konfiguration.

                    Die Änderung ist nur erlaubt, solange der
                    aktuelle fachliche Status dies zulässt.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Konfiguration erfolgreich aktualisiert"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ungültige Eingabedaten"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Konfiguration nicht gefunden"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Aktualisierung aufgrund des aktuellen Status nicht erlaubt"
            )
    })
    public KonfigurationResponse aktualisieren(

            @Parameter(
                    description = "Fachliche Konfigurationsnummer",
                    example = "LK-HTTP-001"
            )
            @PathVariable
            String nummer,

            @Valid
            @RequestBody
            KonfigurationRequest request) {

        LichtkuppelKonfiguration neueDaten =
                LichtkuppelKonfigurationDtoMapper
                        .toDomain(request);

        return LichtkuppelKonfigurationDtoMapper
                .toResponse(
                        service.aktualisieren(
                                nummer,
                                neueDaten));
    }

    // ============================================================
    // LÖSCHEN
    // ============================================================

    @DeleteMapping("/{nummer}")
    @Operation(
            summary = "Konfiguration löschen",
            description = """
                    Löscht eine bestehende Lichtkuppel-Konfiguration.

                    Eine bereits freigegebene Konfiguration
                    darf nicht gelöscht werden.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Konfiguration erfolgreich gelöscht"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Konfiguration nicht gefunden"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Löschen aufgrund des aktuellen Status nicht erlaubt"
            )
    })
    public ResponseEntity<Void> loeschen(

            @Parameter(
                    description = "Fachliche Konfigurationsnummer",
                    example = "LK-HTTP-001"
            )
            @PathVariable
            String nummer) {

        service.loeschen(nummer);

        return ResponseEntity
                .noContent()
                .build();
    }

    // ============================================================
    // TECHNISCHE PRÜFUNG
    // ============================================================

    @PostMapping("/{nummer}/pruefen")
    @Operation(
            summary = "Technische Prüfung durchführen",
            description = """
                    Führt die technischen Prüfregeln für eine
                    Lichtkuppel-Konfiguration aus.

                    Die Prüfung kann beispielsweise die Ergebnisse
                    BESTANDEN, WARNUNG oder NICHT_BESTANDEN liefern.

                    Abhängig vom Prüfergebnis wird auch der
                    technische Status der Konfiguration verändert.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Technische Prüfung erfolgreich durchgeführt"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Konfiguration nicht gefunden"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Technische Prüfung im aktuellen Zustand nicht erlaubt"
            )
    })
    public List<PruefErgebnisResponse> pruefen(

            @Parameter(
                    description = "Fachliche Konfigurationsnummer",
                    example = "LK-HTTP-001"
            )
            @PathVariable
            String nummer) {

        return service
                .technischPruefen(nummer)
                .stream()
                .map(PruefErgebnisResponse::from)
                .toList();
    }

    // ============================================================
    // FREIGEBEN
    // ============================================================

    @PostMapping("/{nummer}/freigeben")
    @Operation(
            summary = "Konfiguration freigeben",
            description = """
                    Gibt eine technisch geprüfte Konfiguration frei.

                    Die Freigabe ist nur möglich, wenn sich die
                    Konfiguration im Status FREIGABEBEREIT befindet.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Konfiguration erfolgreich freigegeben"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Konfiguration nicht gefunden"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Konfiguration ist nicht freigabebereit"
            )
    })
    public KonfigurationResponse freigeben(

            @Parameter(
                    description = "Fachliche Konfigurationsnummer",
                    example = "LK-HTTP-001"
            )
            @PathVariable
            String nummer) {

        return LichtkuppelKonfigurationDtoMapper
                .toResponse(
                        service.freigeben(nummer));
    }
}
