package de.hafni.minierp.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import de.hafni.minierp.TestObjekte;
import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.domain.konfiguration.TechnischerStatus;

@SpringBootTest
@Transactional
class LichtkuppelKonfigurationServiceTest {

    @Autowired
    private LichtkuppelKonfigurationService service;

    @Test
    void konfigurationSollAngelegtUndWiederGeladenWerden() {

        LichtkuppelKonfiguration original =
                TestObjekte.fest();

        LichtkuppelKonfiguration gespeichert =
                service.anlegen(original);

        LichtkuppelKonfiguration geladen =
                service.finden(
                        original.getKonfigurationsNummer());

        assertThat(gespeichert.getKonfigurationsNummer())
                .isEqualTo(
                        original.getKonfigurationsNummer());

        assertThat(geladen.getKonfigurationsNummer())
                .isEqualTo(
                        original.getKonfigurationsNummer());

        assertThat(geladen.getTechnischerStatus())
                .isEqualTo(
                        TechnischerStatus.ENTWURF);
    }

    @Test
    void technischePruefungSollStatusAufFreigabebereitSetzen() {

        LichtkuppelKonfiguration konfiguration =
                TestObjekte.fest();

        service.anlegen(konfiguration);

        var ergebnisse =
                service.technischPruefen(
                        konfiguration.getKonfigurationsNummer());

        assertThat(ergebnisse)
                .isNotEmpty();

        assertThat(ergebnisse)
                .allMatch(ergebnis -> !ergebnis.istFehler());

        LichtkuppelKonfiguration geladen =
                service.finden(
                        konfiguration.getKonfigurationsNummer());

        assertThat(geladen.getTechnischerStatus())
                .isEqualTo(
                        TechnischerStatus.FREIGABEBEREIT);
    }

    @Test
    void freigabebereiteKonfigurationSollFreigegebenWerden() {

        LichtkuppelKonfiguration konfiguration =
                TestObjekte.fest();

        service.anlegen(konfiguration);

        service.technischPruefen(
                konfiguration.getKonfigurationsNummer());

        LichtkuppelKonfiguration freigegeben =
                service.freigeben(
                        konfiguration.getKonfigurationsNummer());

        assertThat(freigegeben.getTechnischerStatus())
                .isEqualTo(
                        TechnischerStatus.FREIGEGEBEN);

        LichtkuppelKonfiguration erneutGeladen =
                service.finden(
                        konfiguration.getKonfigurationsNummer());

        assertThat(
                erneutGeladen.getTechnischerStatus())
                .isEqualTo(
                        TechnischerStatus.FREIGEGEBEN);
    }

    @Test
    void entwurfSollGeloeschtWerdenKoennen() {

        LichtkuppelKonfiguration konfiguration =
                TestObjekte.fest();

        service.anlegen(konfiguration);

        service.loeschen(
                konfiguration.getKonfigurationsNummer());

        assertThat(service.alleLaden())
                .noneMatch(
                        k -> k.getKonfigurationsNummer()
                                .equals(
                                        konfiguration
                                                .getKonfigurationsNummer()));
    }
}