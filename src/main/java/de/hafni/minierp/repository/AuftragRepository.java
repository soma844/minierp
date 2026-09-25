package de.hafni.minierp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hafni.minierp.persistence.entity.AuftragEntity;

public interface AuftragRepository
        extends JpaRepository<AuftragEntity, Long> {

    Optional<AuftragEntity> findByAuftragsNummer(
            String auftragsNummer);

    boolean existsByAuftragsNummer(
            String auftragsNummer);
}