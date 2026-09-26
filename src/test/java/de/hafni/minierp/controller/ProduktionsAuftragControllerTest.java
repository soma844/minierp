package de.hafni.minierp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProduktionsAuftragControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void kompletterProduktionsablaufSollFunktionieren()
            throws Exception {

        /*
         * 1. Kunde
         */
        kundeAnlegen(
                "K-30001",
                "Produktionskunde GmbH");

        /*
         * 2. Technische Konfiguration
         */
        konfigurationAnlegen(
                "LK-30001");

        /*
         * 3. Technisch prÃ¼fen + freigeben
         */
        konfigurationPruefenUndFreigeben(
                "LK-30001");

        /*
         * 4. Auftrag anlegen
         */
        auftragAnlegen(
                "AUF-30001",
                "K-30001",
                "LK-30001");

        /*
         * 5. Auftrag freigeben
         */
        auftragFreigeben(
                "AUF-30001");

        /*
         * 6. Produktionsauftrag anlegen
         */
        String produktionsJson = """
                {
                  "produktionsNummer": "PROD-001",
                  "auftragsNummer": "AUF-30001",
                  "geplanterStart": "2026-10-01"
                }
                """;

        mockMvc.perform(
                        post("/api/produktionsauftraege")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(
                                        produktionsJson)
                )
                .andExpect(
                        status().isCreated())

                .andExpect(
                        header().string(
                                "Location",
                                "/api/produktionsauftraege/PROD-001"))

                .andExpect(
                        jsonPath("$.produktionsNummer")
                                .value("PROD-001"))

                .andExpect(
                        jsonPath("$.auftragsNummer")
                                .value("AUF-30001"))

                .andExpect(
                        jsonPath("$.geplanterStart")
                                .value("2026-10-01"))

                .andExpect(
                        jsonPath("$.status")
                                .value("GEPLANT"));

        /*
         * 7. Produktion starten
         *
         * GEPLANT -> IN_PRODUKTION
         */
        mockMvc.perform(
                        post(
                                "/api/produktionsauftraege/{nummer}/starten",
                                "PROD-001")
                )
                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.status")
                                .value("IN_PRODUKTION"));

        /*
         * 8. Fertigung erfolgt
         *
         * IN_PRODUKTION -> PRODUZIERT
         */
        mockMvc.perform(
                        post(
                                "/api/produktionsauftraege/{nummer}/produziert",
                                "PROD-001")
                )
                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.status")
                                .value("PRODUZIERT"));

        /*
         * 9. Produktionsauftrag abschlieÃŸen
         *
         * PRODUZIERT -> ABGESCHLOSSEN
         */
        mockMvc.perform(
                        post(
                                "/api/produktionsauftraege/{nummer}/abschliessen",
                                "PROD-001")
                )
                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.status")
                                .value("ABGESCHLOSSEN"));

        /*
         * 10. Erneut aus der Datenbank laden.
         *
         * Sehr wichtig:
         * Dadurch prÃ¼fen wir die Rehydration.
         */
        mockMvc.perform(
                        get(
                                "/api/produktionsauftraege/{nummer}",
                                "PROD-001")
                )
                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.produktionsNummer")
                                .value("PROD-001"))

                .andExpect(
                        jsonPath("$.status")
                                .value("ABGESCHLOSSEN"));
    }

    @Test
    void nichtFreigegebenerAuftragDarfNichtProduziertWerden()
            throws Exception {

        kundeAnlegen(
                "K-30002",
                "Kunde Zwei GmbH");

        konfigurationAnlegen(
                "LK-30002");

        konfigurationPruefenUndFreigeben(
                "LK-30002");

        /*
         * Auftrag wird angelegt,
         * aber NICHT freigegeben.
         */
        auftragAnlegen(
                "AUF-30002",
                "K-30002",
                "LK-30002");

        String json = """
                {
                  "produktionsNummer": "PROD-002",
                  "auftragsNummer": "AUF-30002",
                  "geplanterStart": "2026-10-02"
                }
                """;

        mockMvc.perform(
                        post("/api/produktionsauftraege")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        status().isConflict());
    }

    @Test
    void ungueltigerStatuswechselSoll409Liefern()
            throws Exception {

        kundeAnlegen(
                "K-30003",
                "Kunde Drei GmbH");

        konfigurationAnlegen(
                "LK-30003");

        konfigurationPruefenUndFreigeben(
                "LK-30003");

        auftragAnlegen(
                "AUF-30003",
                "K-30003",
                "LK-30003");

        auftragFreigeben(
                "AUF-30003");

        produktionsAuftragAnlegen(
                "PROD-003",
                "AUF-30003");

        /*
         * Aktueller Status ist GEPLANT.
         *
         * Direkt "produziert" ist nicht erlaubt.
         */
        mockMvc.perform(
                        post(
                                "/api/produktionsauftraege/{nummer}/produziert",
                                "PROD-003")
                )
                .andExpect(
                        status().isConflict());
    }

    @Test
    void unbekannterProduktionsauftragSoll404Liefern()
            throws Exception {

        mockMvc.perform(
                        get(
                                "/api/produktionsauftraege/{nummer}",
                                "PROD-NICHT-DA")
                )
                .andExpect(
                        status().isNotFound())

                .andExpect(
                        jsonPath("$.title")
                                .value(
                                        "Ressource nicht gefunden"));
    }

    @Test
    void unvollstaendigerRequestSoll400Liefern()
            throws Exception {

        String json = """
                {
                  "produktionsNummer": "",
                  "auftragsNummer": "",
                  "geplanterStart": null
                }
                """;

        mockMvc.perform(
                        post("/api/produktionsauftraege")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        status().isBadRequest());
    }

    // ============================================================
    // TEST-HILFSMETHODEN
    // ============================================================

    private void kundeAnlegen(
            String kundenNummer,
            String firmenName)
            throws Exception {

        String json = """
                {
                  "kundenNummer": "%s",
                  "firmenName": "%s"
                }
                """.formatted(
                        kundenNummer,
                        firmenName);

        mockMvc.perform(
                        post("/api/kunden")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        status().isCreated());
    }

    private void konfigurationAnlegen(
            String nummer)
            throws Exception {

        String json = """
                {
                  "konfigurationsNummer": "%s",
                  "produktCode": "ECOLUX-PREMIUM-ALU",
                  "produktBezeichnung": "ECOLUX Premium Alu",
                  "funktion": "FEST",

                  "projekt": {
                    "breiteMm": 1200,
                    "laengeMm": 1500,
                    "dachNeigungGrad": 10
                  },

                  "aufsetzkranz": {
                    "typ": "STANDARD",
                    "material": "VERZINKTER_STAHL",
                    "hoeheMm": 400,
                    "daemmStaerkeMm": 50
                  },

                  "festerRahmen": {
                    "material": "ALUMINIUM",
                    "thermischGetrennt": true
                  },

                  "fuellung": {
                    "art": "PCA",
                    "ausfuehrung": "OPAL",
                    "dickeMm": 32,
                    "ugWert": 1.3,
                    "lichtTransmissionProzent": 38,
                    "solarFaktorProzent": 40
                  },

                  "oeffnungsRahmen": null,
                  "antriebsSystem": null
                }
                """.formatted(
                        nummer);

        mockMvc.perform(
                        post("/api/konfigurationen")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        status().isCreated());
    }

    private void konfigurationPruefenUndFreigeben(
            String nummer)
            throws Exception {

        mockMvc.perform(
                        post(
                                "/api/konfigurationen/{nummer}/pruefen",
                                nummer)
                )
                .andExpect(
                        status().isOk());

        mockMvc.perform(
                        post(
                                "/api/konfigurationen/{nummer}/freigeben",
                                nummer)
                )
                .andExpect(
                        status().isOk());
    }

    private void auftragAnlegen(
            String auftragsNummer,
            String kundenNummer,
            String konfigurationsNummer)
            throws Exception {

        String json = """
                {
                  "auftragsNummer": "%s",
                  "kundenNummer": "%s",
                  "positionen": [
                    {
                      "positionsNummer": 10,
                      "menge": 2,
                      "einzelPreisHt": 2300.00,
                      "rabattProzent": 10.00,
                      "konfigurationsNummer": "%s"
                    }
                  ]
                }
                """.formatted(
                        auftragsNummer,
                        kundenNummer,
                        konfigurationsNummer);

        mockMvc.perform(
                        post("/api/auftraege")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        status().isCreated());
    }

    private void auftragFreigeben(
            String auftragsNummer)
            throws Exception {

        mockMvc.perform(
                        post(
                                "/api/auftraege/{nummer}/freigeben",
                                auftragsNummer)
                )
                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.status")
                                .value("FREIGEGEBEN"));
    }

    private void produktionsAuftragAnlegen(
            String produktionsNummer,
            String auftragsNummer)
            throws Exception {

        String json = """
                {
                  "produktionsNummer": "%s",
                  "auftragsNummer": "%s",
                  "geplanterStart": "2026-10-01"
                }
                """.formatted(
                        produktionsNummer,
                        auftragsNummer);

        mockMvc.perform(
                        post("/api/produktionsauftraege")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(
                        status().isCreated())

                .andExpect(
                        jsonPath("$.status")
                                .value("GEPLANT"));
    }
}
