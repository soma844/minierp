package de.hafni.minierp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.hafni.minierp.persistence.entity.KundeEntity;

public interface KundeRepository
        extends JpaRepository<KundeEntity, Long> {

    Optional<KundeEntity> findByKundenNummer(
            String kundenNummer);

    boolean existsByKundenNummer(
            String kundenNummer);
}