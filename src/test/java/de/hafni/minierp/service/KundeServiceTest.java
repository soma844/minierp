package de.hafni.minierp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import de.hafni.minierp.domain.auftrag.Kunde;
import de.hafni.minierp.exception.RessourceNichtGefundenException;

@SpringBootTest
@Transactional
class KundeServiceTest {

    @Autowired
    private KundeService service;

    @Test
    void kundeSollAngelegtUndGeladenWerden() {

        Kunde kunde =
                new Kunde(
                        "K-TEST-001",
                        "Musterbau GmbH"
                );

        Kunde gespeichert =
                service.anlegen(kunde);

        assertEquals(
                "K-TEST-001",
                gespeichert.getKundenNummer()
        );

        assertEquals(
                "Musterbau GmbH",
                gespeichert.getFirmenName()
        );

        Kunde geladen =
                service.finden("K-TEST-001");

        assertEquals(
                "K-TEST-001",
                geladen.getKundenNummer()
        );

        assertEquals(
                "Musterbau GmbH",
                geladen.getFirmenName()
        );
    }

    @Test
    void kundeSollAktualisiertWerden() {

        service.anlegen(
                new Kunde(
                        "K-TEST-002",
                        "Alte Firma GmbH"
                )
        );

        Kunde neueDaten =
                new Kunde(
                        "K-TEST-002",
                        "Neue Firma GmbH"
                );

        Kunde aktualisiert =
                service.aktualisieren(
                        "K-TEST-002",
                        neueDaten
                );

        assertEquals(
                "Neue Firma GmbH",
                aktualisiert.getFirmenName()
        );

        Kunde geladen =
                service.finden("K-TEST-002");

        assertEquals(
                "Neue Firma GmbH",
                geladen.getFirmenName()
        );
    }

    @Test
    void doppelteKundennummerSollAbgelehntWerden() {

        service.anlegen(
                new Kunde(
                        "K-TEST-003",
                        "Firma A GmbH"
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> service.anlegen(
                        new Kunde(
                                "K-TEST-003",
                                "Firma B GmbH"
                        )
                )
        );
    }

    @Test
    void unbekannterKundeSoll404ExceptionAusloesen() {

        assertThrows(
                RessourceNichtGefundenException.class,
                () -> service.finden(
                        "K-NICHT-DA"
                )
        );
    }

    @Test
    void kundeOhneAuftraegeSollGeloeschtWerden() {

        service.anlegen(
                new Kunde(
                        "K-TEST-004",
                        "Loeschbare Firma GmbH"
                )
        );

        service.loeschen(
                "K-TEST-004"
        );

        assertThrows(
                RessourceNichtGefundenException.class,
                () -> service.finden(
                        "K-TEST-004"
                )
        );
    }
}