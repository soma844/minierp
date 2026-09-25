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

import de.hafni.minierp.domain.production.ProduktionsAuftrag;
import de.hafni.minierp.dto.ProduktionsAuftragDtoMapper;
import de.hafni.minierp.dto.ProduktionsAuftragRequest;
import de.hafni.minierp.dto.ProduktionsAuftragResponse;
import de.hafni.minierp.service.ProduktionsAuftragService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produktionsauftraege")
@Tag(
        name = "Produktionsaufträge",
        description = "Planung und Statusführung der Produktion"
)
public class ProduktionsAuftragController {

    private final ProduktionsAuftragService service;

    public ProduktionsAuftragController(
            ProduktionsAuftragService service) {

        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Produktionsauftrag anlegen")
    public ResponseEntity<ProduktionsAuftragResponse> anlegen(
            @Valid
            @RequestBody
            ProduktionsAuftragRequest request) {

        ProduktionsAuftrag gespeichert =
                service.anlegen(request);

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/produktionsauftraege/"
                                        + gespeichert
                                                .getProduktionsNummer()))
                .body(
                        ProduktionsAuftragDtoMapper
                                .toResponse(gespeichert)
                );
    }

    @GetMapping("/{produktionsNummer}")
    @Operation(
            summary = "Produktionsauftrag laden")
    public ProduktionsAuftragResponse finden(
            @PathVariable
            String produktionsNummer) {

        return ProduktionsAuftragDtoMapper.toResponse(
                service.finden(
                        produktionsNummer)
        );
    }

    @GetMapping
    @Operation(
            summary = "Alle Produktionsaufträge laden")
    public List<ProduktionsAuftragResponse> alleLaden() {

        return service
                .alleLaden()
                .stream()
                .map(
                        ProduktionsAuftragDtoMapper::toResponse)
                .toList();
    }

    @PostMapping("/{produktionsNummer}/starten")
    @Operation(
            summary = "Produktion starten")
    public ProduktionsAuftragResponse starten(
            @PathVariable
            String produktionsNummer) {

        return ProduktionsAuftragDtoMapper.toResponse(
                service.starten(
                        produktionsNummer)
        );
    }

    @PostMapping("/{produktionsNummer}/produziert")
    @Operation(
            summary = "Produktion als produziert markieren")
    public ProduktionsAuftragResponse produziert(
            @PathVariable
            String produktionsNummer) {

        return ProduktionsAuftragDtoMapper.toResponse(
                service.alsProduziertMarkieren(
                        produktionsNummer)
        );
    }

    @PostMapping("/{produktionsNummer}/abschliessen")
    @Operation(
            summary = "Produktionsauftrag abschließen")
    public ProduktionsAuftragResponse abschliessen(
            @PathVariable
            String produktionsNummer) {

        return ProduktionsAuftragDtoMapper.toResponse(
                service.abschliessen(
                        produktionsNummer)
        );
    }
}