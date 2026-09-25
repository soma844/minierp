package de.hafni.minierp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hafni.minierp.persistence.entity.LichtkuppelKonfigurationEntity;

public interface LichtkuppelKonfigurationRepository
        extends JpaRepository<LichtkuppelKonfigurationEntity, Long> {

    Optional<LichtkuppelKonfigurationEntity>
            findByKonfigurationsNummer(String konfigurationsNummer);

    boolean existsByKonfigurationsNummer(
            String konfigurationsNummer);
}