package de.hafni.minierp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hafni.minierp.domain.auftrag.Auftrag;
import de.hafni.minierp.domain.production.ProduktionsAuftrag;
import de.hafni.minierp.dto.ProduktionsAuftragRequest;
import de.hafni.minierp.exception.RessourceNichtGefundenException;
import de.hafni.minierp.persistence.entity.AuftragEntity;
import de.hafni.minierp.persistence.entity.ProduktionsAuftragEntity;
import de.hafni.minierp.persistence.mapper.AuftragMapper;
import de.hafni.minierp.persistence.mapper.ProduktionsAuftragMapper;
import de.hafni.minierp.repository.AuftragRepository;
import de.hafni.minierp.repository.ProduktionsAuftragRepository;

@Service
@Transactional
public class ProduktionsAuftragService {

    private final ProduktionsAuftragRepository produktionsRepository;
    private final AuftragRepository auftragRepository;

    public ProduktionsAuftragService(
            ProduktionsAuftragRepository produktionsRepository,
            AuftragRepository auftragRepository) {

        this.produktionsRepository = produktionsRepository;
        this.auftragRepository = auftragRepository;
    }

    public ProduktionsAuftrag anlegen(
            ProduktionsAuftragRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "ProduktionsAuftragRequest darf nicht null sein.");
        }

        if (produktionsRepository.existsByProduktionsNummer(
                request.produktionsNummer())) {

            throw new IllegalStateException(
                    "Produktionsnummer existiert bereits: "
                            + request.produktionsNummer());
        }

        AuftragEntity auftragEntity =
                auftragRepository
                        .findByAuftragsNummer(
                                request.auftragsNummer())
                        .orElseThrow(() ->
                                new RessourceNichtGefundenException(
                                        "Auftrag wurde nicht gefunden: "
                                                + request.auftragsNummer()
                                )
                        );

        Auftrag auftrag =
                AuftragMapper.toDomain(
                        auftragEntity);

        /*
         * Genau hier greift die Domain-Regel:
         *
         * Nur FREIGEGEBENE Aufträge dürfen
         * Produktionsaufträge bekommen.
         */
        ProduktionsAuftrag produktionsAuftrag =
                new ProduktionsAuftrag(
                        request.produktionsNummer(),
                        auftrag,
                        request.geplanterStart()
                );

        ProduktionsAuftragEntity entity =
                ProduktionsAuftragMapper.toEntity(
                        produktionsAuftrag,
                        auftragEntity
                );

        return ProduktionsAuftragMapper.toDomain(
                produktionsRepository.save(entity)
        );
    }

    @Transactional(readOnly = true)
    public ProduktionsAuftrag finden(
            String produktionsNummer) {

        return ProduktionsAuftragMapper.toDomain(
                findeEntity(produktionsNummer)
        );
    }

    @Transactional(readOnly = true)
    public List<ProduktionsAuftrag> alleLaden() {

        return produktionsRepository
                .findAll()
                .stream()
                .map(ProduktionsAuftragMapper::toDomain)
                .toList();
    }

    public ProduktionsAuftrag starten(
            String produktionsNummer) {

        ProduktionsAuftragEntity entity =
                findeEntity(produktionsNummer);

        ProduktionsAuftrag domain =
                ProduktionsAuftragMapper.toDomain(entity);

        domain.starteProduktion();

        entity.aktualisiereStatus(
                domain.getStatus());

        return ProduktionsAuftragMapper.toDomain(
                produktionsRepository.save(entity)
        );
    }

    public ProduktionsAuftrag alsProduziertMarkieren(
            String produktionsNummer) {

        ProduktionsAuftragEntity entity =
                findeEntity(produktionsNummer);

        ProduktionsAuftrag domain =
                ProduktionsAuftragMapper.toDomain(entity);

        domain.markiereProduziert();

        entity.aktualisiereStatus(
                domain.getStatus());

        return ProduktionsAuftragMapper.toDomain(
                produktionsRepository.save(entity)
        );
    }

    public ProduktionsAuftrag abschliessen(
            String produktionsNummer) {

        ProduktionsAuftragEntity entity =
                findeEntity(produktionsNummer);

        ProduktionsAuftrag domain =
                ProduktionsAuftragMapper.toDomain(entity);

        domain.abschliessen();

        entity.aktualisiereStatus(
                domain.getStatus());

        return ProduktionsAuftragMapper.toDomain(
                produktionsRepository.save(entity)
        );
    }

    private ProduktionsAuftragEntity findeEntity(
            String produktionsNummer) {

        if (produktionsNummer == null
                || produktionsNummer.isBlank()) {

            throw new IllegalArgumentException(
                    "Produktionsnummer darf nicht leer sein.");
        }

        return produktionsRepository
                .findByProduktionsNummer(
                        produktionsNummer)
                .orElseThrow(() ->
                        new RessourceNichtGefundenException(
                                "Produktionsauftrag wurde nicht gefunden: "
                                        + produktionsNummer
                        )
                );
    }
}