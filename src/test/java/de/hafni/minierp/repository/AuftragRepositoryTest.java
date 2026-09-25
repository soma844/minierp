package de.hafni.minierp.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import de.hafni.minierp.PersistenceTestObjekte;
import de.hafni.minierp.domain.auftrag.AuftragsStatus;
import de.hafni.minierp.domain.konfiguration.LichtkuppelFunktion;
import de.hafni.minierp.persistence.entity.AuftragEntity;
import de.hafni.minierp.persistence.entity.AuftragsPositionEntity;
import de.hafni.minierp.persistence.entity.KundeEntity;
import de.hafni.minierp.persistence.entity.LichtkuppelKonfigurationEntity;

@DataJpaTest
class AuftragRepositoryTest {

    @Autowired
    private KundeRepository kundeRepository;

    @Autowired
    private AuftragRepository auftragRepository;
    

    @Test
    void auftragMitFesterKonfigurationSollGespeichertWerden() {

        KundeEntity kunde =
                kundeRepository.save(
                        new KundeEntity(
                                "K-2001",
                                "Demo Industrie SARL"));

        AuftragEntity auftrag =
                new AuftragEntity(
                        "A-2026-2001",
                        AuftragsStatus.ENTWURF,
                        kunde);

        auftrag.fuegePositionHinzu(
                new AuftragsPositionEntity(
                        10,
                        3,
                        new BigDecimal("1500.00"),
                        new BigDecimal("5.00"),
                        PersistenceTestObjekte.festeKonfiguration()));

        auftragRepository.save(auftrag);

        AuftragEntity gespeichert =
                auftragRepository
                        .findByAuftragsNummer("A-2026-2001")
                        .orElseThrow();

        assertThat(gespeichert.getKunde().getKundenNummer())
                .isEqualTo("K-2001");

        assertThat(gespeichert.getPositionen())
                .hasSize(1);

        assertThat(
                gespeichert
                        .getPositionen()
                        .get(0)
                        .getKonfiguration()
                        .getFunktion())
                .isEqualTo(LichtkuppelFunktion.FEST);
    }
    @Test
    void lueftungsKonfigurationMitAntriebenSollGespeichertWerden() {

        KundeEntity kunde =
                kundeRepository.save(
                        new KundeEntity(
                                "K-2002",
                                "Ventilation Demo SARL"));

        AuftragEntity auftrag =
                new AuftragEntity(
                        "A-2026-2002",
                        AuftragsStatus.ENTWURF,
                        kunde);

        auftrag.fuegePositionHinzu(
                new AuftragsPositionEntity(
                        10,
                        1,
                        new BigDecimal("2200.00"),
                        BigDecimal.ZERO,
                        PersistenceTestObjekte.lueftungsKonfiguration()));

        auftragRepository.save(auftrag);

        AuftragEntity gespeichert =
                auftragRepository
                        .findByAuftragsNummer("A-2026-2002")
                        .orElseThrow();

        LichtkuppelKonfigurationEntity konfiguration =
                gespeichert
                        .getPositionen()
                        .get(0)
                        .getKonfiguration();

        assertThat(konfiguration.getFunktion())
                .isEqualTo(LichtkuppelFunktion.LUEFTUNG);

        assertThat(konfiguration.getOeffnungsRahmen())
                .isNotNull();

        assertThat(konfiguration.getAntriebsSystem())
                .isNotNull();

        assertThat(konfiguration.getAntriebsSystem().getAntriebe())
                .hasSize(2);

        assertThat(
                konfiguration
                        .getAntriebsSystem()
                        .getAntriebe()
                        .get(0)
                        .getNennStromAmpere())
                .isEqualByComparingTo("0.24");
    }
}