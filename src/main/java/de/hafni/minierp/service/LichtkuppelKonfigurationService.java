package de.hafni.minierp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.domain.konfiguration.TechnischerStatus;
import de.hafni.minierp.persistence.entity.LichtkuppelKonfigurationEntity;
import de.hafni.minierp.persistence.mapper.LichtkuppelKonfigurationMapper;
import de.hafni.minierp.pruefung.PruefErgebnis;
import de.hafni.minierp.pruefung.TechnischePruefung;
import de.hafni.minierp.repository.LichtkuppelKonfigurationRepository;
import de.hafni.minierp.exception.RessourceNichtGefundenException;

@Service
@Transactional
public class LichtkuppelKonfigurationService {

    private final LichtkuppelKonfigurationRepository repository;

    public LichtkuppelKonfigurationService(
            LichtkuppelKonfigurationRepository repository) {

        this.repository = repository;
    }

    // =========================================================
    // CREATE
    // =========================================================

    public LichtkuppelKonfiguration anlegen(
            LichtkuppelKonfiguration konfiguration) {

        if (konfiguration == null) {
            throw new IllegalArgumentException(
                    "Konfiguration darf nicht null sein.");
        }

        String nummer =
                konfiguration.getKonfigurationsNummer();

        if (repository.existsByKonfigurationsNummer(nummer)) {
            throw new IllegalArgumentException(
                    "Konfiguration existiert bereits: " + nummer);
        }

        LichtkuppelKonfigurationEntity entity =
                LichtkuppelKonfigurationMapper.toEntity(
                        konfiguration);

        LichtkuppelKonfigurationEntity gespeichert =
                repository.save(entity);

        return LichtkuppelKonfigurationMapper.toDomain(
                gespeichert);
    }

    // =========================================================
    // READ - EIN OBJEKT
    // =========================================================

    @Transactional(readOnly = true)
    public LichtkuppelKonfiguration finden(
            String konfigurationsNummer) {

        LichtkuppelKonfigurationEntity entity =
                findeEntity(konfigurationsNummer);

        return LichtkuppelKonfigurationMapper.toDomain(
                entity);
    }

    // =========================================================
    // READ - ALLE
    // =========================================================

    @Transactional(readOnly = true)
    public List<LichtkuppelKonfiguration> alleLaden() {

        return repository
                .findAll()
                .stream()
                .map(LichtkuppelKonfigurationMapper::toDomain)
                .toList();
    }

    // =========================================================
    // UPDATE
    // =========================================================

    public LichtkuppelKonfiguration aktualisieren(
            String konfigurationsNummer,
            LichtkuppelKonfiguration neueDaten) {

        if (neueDaten == null) {
            throw new IllegalArgumentException(
                    "Neue Konfigurationsdaten dürfen nicht null sein.");
        }

        LichtkuppelKonfigurationEntity entity =
                findeEntity(konfigurationsNummer);

        LichtkuppelKonfiguration bestehend =
                LichtkuppelKonfigurationMapper.toDomain(
                        entity);

        if (bestehend.getTechnischerStatus()
                != TechnischerStatus.ENTWURF) {

            throw new IllegalStateException(
                    "Nur eine Konfiguration im Status ENTWURF "
                            + "darf geändert werden. Aktueller Status: "
                            + bestehend.getTechnischerStatus());
        }

        if (!konfigurationsNummer.equals(
                neueDaten.getKonfigurationsNummer())) {

            throw new IllegalArgumentException(
                    "Die Konfigurationsnummer darf beim Update "
                            + "nicht geändert werden.");
        }

        LichtkuppelKonfigurationMapper.updateEntity(
                neueDaten,
                entity);

        LichtkuppelKonfigurationEntity gespeichert =
                repository.save(entity);

        return LichtkuppelKonfigurationMapper.toDomain(
                gespeichert);
    }

    // =========================================================
    // DELETE
    // =========================================================

    public void loeschen(
            String konfigurationsNummer) {

        LichtkuppelKonfigurationEntity entity =
                findeEntity(konfigurationsNummer);

        LichtkuppelKonfiguration domain =
                LichtkuppelKonfigurationMapper.toDomain(
                        entity);

        if (domain.getTechnischerStatus()
                == TechnischerStatus.FREIGEGEBEN) {

            throw new IllegalStateException(
                    "Eine freigegebene Konfiguration "
                            + "darf nicht gelöscht werden.");
        }

        repository.delete(entity);
    }

    // =========================================================
    // FACHLICHE AKTION: TECHNISCH PRÜFEN
    // =========================================================

    public List<PruefErgebnis> technischPruefen(
            String konfigurationsNummer) {

        LichtkuppelKonfigurationEntity entity =
                findeEntity(konfigurationsNummer);

        LichtkuppelKonfiguration domain =
                LichtkuppelKonfigurationMapper.toDomain(
                        entity);

        TechnischePruefung technischePruefung =
                new TechnischePruefung();

        List<PruefErgebnis> ergebnisse =
                technischePruefung.pruefe(domain);

        entity.aktualisiereTechnischenStatus(
                domain.getTechnischerStatus());

        repository.save(entity);

        return ergebnisse;
    }

    // =========================================================
    // FACHLICHE AKTION: FREIGEBEN
    // =========================================================

    public LichtkuppelKonfiguration freigeben(
            String konfigurationsNummer) {

        LichtkuppelKonfigurationEntity entity =
                findeEntity(konfigurationsNummer);

        LichtkuppelKonfiguration domain =
                LichtkuppelKonfigurationMapper.toDomain(
                        entity);

        domain.freigeben();

        entity.aktualisiereTechnischenStatus(
                domain.getTechnischerStatus());

        LichtkuppelKonfigurationEntity gespeichert =
                repository.save(entity);

        return LichtkuppelKonfigurationMapper.toDomain(
                gespeichert);
    }

    // =========================================================
    // INTERNE HILFSMETHODE
    // =========================================================

    private LichtkuppelKonfigurationEntity findeEntity(String nummer) {

        if (nummer == null || nummer.isBlank()) {
            throw new IllegalArgumentException(
                    "Konfigurationsnummer darf nicht leer sein."
            );
        }

        return repository
                .findByKonfigurationsNummer(nummer)
                .orElseThrow(() ->
                        new RessourceNichtGefundenException(
                                "Konfiguration wurde nicht gefunden: " + nummer
                        )
                );
    }
}