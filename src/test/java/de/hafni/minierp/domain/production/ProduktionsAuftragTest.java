package de.hafni.minierp.domain.production;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import de.hafni.minierp.TestSzenarien;
import de.hafni.minierp.domain.auftrag.Auftrag;
import de.hafni.minierp.exception.GeschaeftsregelException;

class ProduktionsAuftragTest {

    @Test
    void freigegebenerAuftragSollProduktionsauftragErzeugenKoennen() {

        Auftrag auftrag =
                TestSzenarien.freigegebenerAuftrag6900();

        ProduktionsAuftrag produktionsAuftrag =
                new ProduktionsAuftrag(
                        "P-2026-001",
                        auftrag,
                        LocalDate.of(2026, 10, 1));

        assertThat(produktionsAuftrag.getStatus())
                .isEqualTo(ProduktionsStatus.GEPLANT);
    }

    @Test
    void produktionSollKorrektenStatusflussDurchlaufen() {

        ProduktionsAuftrag produktionsAuftrag =
                new ProduktionsAuftrag(
                        "P-2026-002",
                        TestSzenarien.freigegebenerAuftrag6900(),
                        LocalDate.of(2026, 10, 1));

        produktionsAuftrag.starteProduktion();

        assertThat(produktionsAuftrag.getStatus())
                .isEqualTo(ProduktionsStatus.IN_PRODUKTION);

        produktionsAuftrag.markiereProduziert();

        assertThat(produktionsAuftrag.getStatus())
                .isEqualTo(ProduktionsStatus.PRODUZIERT);

        produktionsAuftrag.abschliessen();

        assertThat(produktionsAuftrag.getStatus())
                .isEqualTo(ProduktionsStatus.ABGESCHLOSSEN);
    }

    @Test
    void geplanterAuftragDarfNichtDirektProduziertWerden() {

        ProduktionsAuftrag produktionsAuftrag =
                new ProduktionsAuftrag(
                        "P-2026-003",
                        TestSzenarien.freigegebenerAuftrag6900(),
                        LocalDate.of(2026, 10, 1));

        assertThatThrownBy(
                produktionsAuftrag::markiereProduziert)
                .isInstanceOf(
                        GeschaeftsregelException.class);
    }
}