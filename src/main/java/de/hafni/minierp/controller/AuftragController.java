package de.hafni.minierp.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.hafni.minierp.domain.auftrag.Auftrag;
import de.hafni.minierp.dto.AuftragDtoMapper;
import de.hafni.minierp.dto.AuftragRequest;
import de.hafni.minierp.dto.AuftragResponse;
import de.hafni.minierp.service.AuftragService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auftraege")
@Tag(
        name = "Aufträge",
        description = "Verwaltung und Freigabe von Kundenaufträgen"
)
public class AuftragController {

    private final AuftragService service;

    public AuftragController(
            AuftragService service) {

        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Auftrag anlegen"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Auftrag angelegt"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ungültige Eingabedaten"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kunde oder Konfiguration nicht gefunden"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Auftragsnummer existiert bereits")
    })
    public ResponseEntity<AuftragResponse> anlegen(
            @Valid
            @RequestBody
            AuftragRequest request) {

        Auftrag gespeichert =
                service.anlegen(request);

        AuftragResponse response =
                AuftragDtoMapper.toResponse(
                        gespeichert);

        URI location =
                URI.create(
                        "/api/auftraege/"
                                + gespeichert
                                        .getAuftragsNummer());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping("/{auftragsNummer}")
    @Operation(
            summary = "Auftrag laden"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Auftrag gefunden"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Auftrag nicht gefunden")
    })
    public AuftragResponse finden(
            @PathVariable
            String auftragsNummer) {

        return AuftragDtoMapper.toResponse(
                service.finden(
                        auftragsNummer)
        );
    }

    @GetMapping
    @Operation(
            summary = "Alle Aufträge laden"
    )
    public List<AuftragResponse> alleLaden() {

        return service
                .alleLaden()
                .stream()
                .map(AuftragDtoMapper::toResponse)
                .toList();
    }

    @PostMapping("/{auftragsNummer}/freigeben")
    @Operation(
            summary = "Auftrag freigeben",
            description = """
                    Gibt einen Auftrag frei.
                    Alle technischen Konfigurationen
                    der Auftragspositionen müssen zuvor
                    technisch freigegeben sein.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Auftrag freigegeben"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Auftrag nicht gefunden"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Fachliche Freigabe nicht möglich")
    })
    public AuftragResponse freigeben(
            @PathVariable
            String auftragsNummer) {

        return AuftragDtoMapper.toResponse(
                service.freigeben(
                        auftragsNummer)
        );
    }
}