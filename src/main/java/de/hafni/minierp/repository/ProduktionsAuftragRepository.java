package de.hafni.minierp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hafni.minierp.persistence.entity.ProduktionsAuftragEntity;

public interface ProduktionsAuftragRepository
        extends JpaRepository<ProduktionsAuftragEntity, Long> {

    Optional<ProduktionsAuftragEntity> findByProduktionsNummer(
            String produktionsNummer);

    boolean existsByProduktionsNummer(
            String produktionsNummer);
}