package de.hafni.minierp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hafni.minierp.domain.auftrag.Auftrag;
import de.hafni.minierp.domain.auftrag.AuftragsPosition;
import de.hafni.minierp.domain.auftrag.Kunde;
import de.hafni.minierp.dto.AuftragRequest;
import de.hafni.minierp.dto.AuftragsPositionRequest;
import de.hafni.minierp.exception.RessourceNichtGefundenException;
import de.hafni.minierp.persistence.entity.AuftragEntity;
import de.hafni.minierp.persistence.entity.AuftragsPositionEntity;
import de.hafni.minierp.persistence.entity.KundeEntity;
import de.hafni.minierp.persistence.entity.LichtkuppelKonfigurationEntity;
import de.hafni.minierp.persistence.mapper.AuftragMapper;
import de.hafni.minierp.persistence.mapper.KundeMapper;
import de.hafni.minierp.persistence.mapper.LichtkuppelKonfigurationMapper;
import de.hafni.minierp.repository.AuftragRepository;
import de.hafni.minierp.repository.KundeRepository;
import de.hafni.minierp.repository.LichtkuppelKonfigurationRepository;

@Service
@Transactional
public class AuftragService {

    private final AuftragRepository auftragRepository;
    private final KundeRepository kundeRepository;
    private final LichtkuppelKonfigurationRepository konfigurationRepository;

    public AuftragService(
            AuftragRepository auftragRepository,
            KundeRepository kundeRepository,
            LichtkuppelKonfigurationRepository konfigurationRepository) {

        this.auftragRepository = auftragRepository;
        this.kundeRepository = kundeRepository;
        this.konfigurationRepository = konfigurationRepository;
    }

    // ============================================================
    // CREATE
    // ============================================================

    public Auftrag anlegen(
            AuftragRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "AuftragRequest darf nicht null sein."
            );
        }

        if (auftragRepository.existsByAuftragsNummer(
                request.auftragsNummer())) {

            throw new IllegalStateException(
                    "Auftragsnummer existiert bereits: "
                            + request.auftragsNummer()
            );
        }

        // --------------------------------------------------------
        // 1. Kunde aus Datenbank laden
        // --------------------------------------------------------

        KundeEntity kundeEntity =
                kundeRepository
                        .findByKundenNummer(
                                request.kundenNummer())
                        .orElseThrow(() ->
                                new RessourceNichtGefundenException(
                                        "Kunde wurde nicht gefunden: "
                                                + request.kundenNummer()
                                )
                        );

        Kunde kunde =
                KundeMapper.toDomain(kundeEntity);

        // --------------------------------------------------------
        // 2. Domain-Auftrag erzeugen
        // --------------------------------------------------------

        Auftrag auftrag =
                new Auftrag(
                        request.auftragsNummer(),
                        kunde
                );

        /*
         * Wir merken uns die bereits geladenen JPA-Entities.
         *
         * Schlüssel:
         * Konfigurationsnummer
         *
         * Wert:
         * JPA Entity
         */
        Map<String, LichtkuppelKonfigurationEntity>
                konfigurationen = new HashMap<>();

        // --------------------------------------------------------
        // 3. Positionen aufbauen
        // --------------------------------------------------------

        for (AuftragsPositionRequest positionRequest
                : request.positionen()) {

            LichtkuppelKonfigurationEntity
                    konfigurationEntity =
                    konfigurationRepository
                            .findByKonfigurationsNummer(
                                    positionRequest
                                            .konfigurationsNummer())
                            .orElseThrow(() ->
                                    new RessourceNichtGefundenException(
                                            "Konfiguration wurde nicht gefunden: "
                                                    + positionRequest
                                                            .konfigurationsNummer()
                                    )
                            );

            AuftragsPosition position =
                    new AuftragsPosition(

                            positionRequest
                                    .positionsNummer(),

                            positionRequest
                                    .menge(),

                            positionRequest
                                    .einzelPreisHt(),

                            positionRequest
                                    .rabattProzent(),

                            LichtkuppelKonfigurationMapper
                                    .toDomain(
                                            konfigurationEntity)
                    );

            auftrag.fuegePositionHinzu(
                    position
            );

            konfigurationen.put(
                    positionRequest
                            .konfigurationsNummer(),
                    konfigurationEntity
            );
        }

        // --------------------------------------------------------
        // 4. Auftrag Domain -> Entity
        // --------------------------------------------------------

        AuftragEntity auftragEntity =
                AuftragMapper.toEntity(
                        auftrag,
                        kundeEntity
                );

        // --------------------------------------------------------
        // 5. Positionen Domain -> Entity
        // --------------------------------------------------------

        for (AuftragsPosition position
                : auftrag.getPositionen()) {

            String konfigurationsNummer =
                    position
                            .getKonfiguration()
                            .getKonfigurationsNummer();

            LichtkuppelKonfigurationEntity
                    konfigurationEntity =
                    konfigurationen.get(
                            konfigurationsNummer);

            AuftragsPositionEntity positionEntity =
                    AuftragMapper.toEntity(
                            position,
                            konfigurationEntity
                    );

            auftragEntity.fuegePositionHinzu(
                    positionEntity
            );
        }

        // --------------------------------------------------------
        // 6. Auftrag speichern
        // --------------------------------------------------------

        AuftragEntity gespeichert =
                auftragRepository.save(
                        auftragEntity);

        return AuftragMapper.toDomain(
                gespeichert);
    }

    // ============================================================
    // READ ONE
    // ============================================================

    @Transactional(readOnly = true)
    public Auftrag finden(
            String auftragsNummer) {

        return AuftragMapper.toDomain(
                findeEntity(
                        auftragsNummer)
        );
    }

    // ============================================================
    // READ ALL
    // ============================================================

    @Transactional(readOnly = true)
    public List<Auftrag> alleLaden() {

        return auftragRepository
                .findAll()
                .stream()
                .map(AuftragMapper::toDomain)
                .toList();
    }

    // ============================================================
    // FREIGEBEN
    // ============================================================

    public Auftrag freigeben(
            String auftragsNummer) {

        AuftragEntity entity =
                findeEntity(
                        auftragsNummer);

        Auftrag domain =
                AuftragMapper.toDomain(
                        entity);

        /*
         * Die Geschäftsregel liegt NICHT hier.
         *
         * Domain entscheidet:
         * - Positionen vorhanden?
         * - alle Konfigurationen FREIGEGEBEN?
         */
        domain.freigeben();

        entity.aktualisiereStatus(
                domain.getStatus());

        AuftragEntity gespeichert =
                auftragRepository.save(
                        entity);

        return AuftragMapper.toDomain(
                gespeichert);
    }

    // ============================================================
    // INTERN
    // ============================================================

    private AuftragEntity findeEntity(
            String auftragsNummer) {

        if (auftragsNummer == null
                || auftragsNummer.isBlank()) {

            throw new IllegalArgumentException(
                    "Auftragsnummer darf nicht leer sein."
            );
        }

        return auftragRepository
                .findByAuftragsNummer(
                        auftragsNummer)
                .orElseThrow(() ->
                        new RessourceNichtGefundenException(
                                "Auftrag wurde nicht gefunden: "
                                        + auftragsNummer
                        )
                );
    }
}