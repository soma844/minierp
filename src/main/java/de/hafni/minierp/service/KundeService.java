package de.hafni.minierp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hafni.minierp.domain.auftrag.Kunde;
import de.hafni.minierp.exception.RessourceNichtGefundenException;
import de.hafni.minierp.persistence.entity.KundeEntity;
import de.hafni.minierp.persistence.mapper.KundeMapper;
import de.hafni.minierp.repository.KundeRepository;

@Service
@Transactional
public class KundeService {

    private final KundeRepository repository;

    public KundeService(
            KundeRepository repository) {

        this.repository = repository;
    }

    // ============================================================
    // CREATE
    // ============================================================

    public Kunde anlegen(Kunde kunde) {

        if (kunde == null) {
            throw new IllegalArgumentException(
                    "Kunde darf nicht null sein."
            );
        }

        if (repository.existsByKundenNummer(
                kunde.getKundenNummer())) {

            throw new IllegalStateException(
                    "Kundennummer existiert bereits: "
                            + kunde.getKundenNummer()
            );
        }

        KundeEntity entity =
                KundeMapper.toEntity(kunde);

        KundeEntity gespeichert =
                repository.save(entity);

        return KundeMapper.toDomain(gespeichert);
    }

    // ============================================================
    // READ ONE
    // ============================================================

    @Transactional(readOnly = true)
    public Kunde finden(String kundenNummer) {

        return KundeMapper.toDomain(
                findeEntity(kundenNummer)
        );
    }

    // ============================================================
    // READ ALL
    // ============================================================

    @Transactional(readOnly = true)
    public List<Kunde> alleLaden() {

        return repository
                .findAll()
                .stream()
                .map(KundeMapper::toDomain)
                .toList();
    }

    // ============================================================
    // UPDATE
    // ============================================================

    public Kunde aktualisieren(
            String kundenNummer,
            Kunde neueDaten) {

        if (neueDaten == null) {
            throw new IllegalArgumentException(
                    "Neue Kundendaten dürfen nicht null sein."
            );
        }

        if (!kundenNummer.equals(
                neueDaten.getKundenNummer())) {

            throw new IllegalArgumentException(
                    "Kundennummer im Pfad und Request müssen übereinstimmen."
            );
        }

        KundeEntity entity =
                findeEntity(kundenNummer);

        KundeMapper.updateEntity(
                neueDaten,
                entity
        );

        KundeEntity gespeichert =
                repository.save(entity);

        return KundeMapper.toDomain(gespeichert);
    }

    // ============================================================
    // DELETE
    // ============================================================

    public void loeschen(String kundenNummer) {

        KundeEntity entity =
                findeEntity(kundenNummer);

        if (!entity.getAuftraege().isEmpty()) {

            throw new IllegalStateException(
                    "Kunde kann nicht gelöscht werden, "
                            + "weil bereits Aufträge existieren."
            );
        }

        repository.delete(entity);
    }

    // ============================================================
    // INTERN
    // ============================================================

    private KundeEntity findeEntity(
            String kundenNummer) {

        if (kundenNummer == null
                || kundenNummer.isBlank()) {

            throw new IllegalArgumentException(
                    "Kundennummer darf nicht leer sein."
            );
        }

        return repository
                .findByKundenNummer(kundenNummer)
                .orElseThrow(() ->
                        new RessourceNichtGefundenException(
                                "Kunde wurde nicht gefunden: "
                                        + kundenNummer
                        )
                );
    }
}