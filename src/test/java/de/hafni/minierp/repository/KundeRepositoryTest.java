package de.hafni.minierp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import de.hafni.minierp.persistence.entity.KundeEntity;

@DataJpaTest
class KundeRepositoryTest {

    @Autowired
    private KundeRepository kundeRepository;

    @Test
    void kundeSollGespeichertUndGefundenWerden() {

        KundeEntity kunde =
                new KundeEntity(
                        "K-1001",
                        "Société Demo SARL");

        kundeRepository.save(kunde);

        var gefunden =
                kundeRepository.findByKundenNummer(
                        "K-1001");

        assertThat(gefunden)
                .isPresent();

        assertThat(
                gefunden.orElseThrow()
                        .getFirmenName())
                .isEqualTo(
                        "Société Demo SARL");
    }
}