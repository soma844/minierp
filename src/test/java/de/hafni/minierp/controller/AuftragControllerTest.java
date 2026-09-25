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
class AuftragControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void auftragSollAngelegtUndBerechnetWerden()
            throws Exception {

        kundeAnlegen(
                "K-AUF-001",
                "Musterbau GmbH");

        konfigurationAnlegen(
                "LK-AUF-001");

        String json = """
                {
                  "auftragsNummer": "AUF-REST-001",
                  "kundenNummer": "K-AUF-001",
                  "positionen": [
                    {
                      "positionsNummer": 10,
                      "menge": 3,
                      "einzelPreisHt": 2300.00,
                      "rabattProzent": 10.00,
                      "konfigurationsNummer": "LK-AUF-001"
                    }
                  ]
                }
                """;

        mockMvc.perform(
                        post("/api/auftraege")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )

                .andExpect(
                        status().isCreated())

                .andExpect(
                        header().string(
                                "Location",
                                "/api/auftraege/AUF-REST-001"))

                .andExpect(
                        jsonPath("$.auftragsNummer")
                                .value("AUF-REST-001"))

                .andExpect(
                        jsonPath("$.kundenNummer")
                                .value("K-AUF-001"))

                .andExpect(
                        jsonPath("$.status")
                                .value("ENTWURF"))

                .andExpect(
                        jsonPath("$.positionen[0].menge")
                                .value(3))

                .andExpect(
                        jsonPath("$.positionen[0].bruttoHt")
                                .value(6900.0))

                .andExpect(
                        jsonPath("$.positionen[0].rabattBetrag")
                                .value(690.0))

                .andExpect(
                        jsonPath("$.positionen[0].gesamtHt")
                                .value(6210.0))

                .andExpect(
                        jsonPath("$.gesamtHt")
                                .value(6210.0));
    }

    @Test
    void gespeicherterAuftragSollGelesenWerden()
            throws Exception {

        kundeAnlegen(
                "K-AUF-002",
                "Firma Zwei GmbH");

        konfigurationAnlegen(
                "LK-AUF-002");

        auftragAnlegen(
                "AUF-REST-002",
                "K-AUF-002",
                "LK-AUF-002");

        mockMvc.perform(
                        get(
                                "/api/auftraege/{nummer}",
                                "AUF-REST-002")
                )

                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.auftragsNummer")
                                .value("AUF-REST-002"))

                .andExpect(
                        jsonPath("$.status")
                                .value("ENTWURF"))

                .andExpect(
                        jsonPath("$.positionen[0].konfigurationsNummer")
                                .value("LK-AUF-002"));
    }

    @Test
    void auftragMitFreigegebenerKonfigurationSollFreigegebenWerden()
            throws Exception {

        kundeAnlegen(
                "K-AUF-003",
                "Firma Drei GmbH");

        konfigurationAnlegen(
                "LK-AUF-003");

        konfigurationPruefenUndFreigeben(
                "LK-AUF-003");

        auftragAnlegen(
                "AUF-REST-003",
                "K-AUF-003",
                "LK-AUF-003");

        mockMvc.perform(
                        post(
                                "/api/auftraege/{nummer}/freigeben",
                                "AUF-REST-003")
                )

                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.status")
                                .value("FREIGEGEBEN"));

        /*
         * Besonders wichtig:
         *
         * Wir lesen danach erneut aus der DB.
         *
         * Damit prüfen wir auch unsere
         * Auftrag.rehydrieren()-Logik.
         */
        mockMvc.perform(
                        get(
                                "/api/auftraege/{nummer}",
                                "AUF-REST-003")
                )

                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.status")
                                .value("FREIGEGEBEN"));
    }

    @Test
    void auftragMitNichtFreigegebenerKonfigurationSoll409Liefern()
            throws Exception {

        kundeAnlegen(
                "K-AUF-004",
                "Firma Vier GmbH");

        konfigurationAnlegen(
                "LK-AUF-004");

        auftragAnlegen(
                "AUF-REST-004",
                "K-AUF-004",
                "LK-AUF-004");

        mockMvc.perform(
                        post(
                                "/api/auftraege/{nummer}/freigeben",
                                "AUF-REST-004")
                )

                .andExpect(
                        status().isConflict());
    }

    @Test
    void unbekannterKundeSoll404Liefern()
            throws Exception {

        konfigurationAnlegen(
                "LK-AUF-005");

        String json = """
                {
                  "auftragsNummer": "AUF-REST-005",
                  "kundenNummer": "K-NICHT-DA",
                  "positionen": [
                    {
                      "positionsNummer": 10,
                      "menge": 1,
                      "einzelPreisHt": 1000.00,
                      "rabattProzent": 0.00,
                      "konfigurationsNummer": "LK-AUF-005"
                    }
                  ]
                }
                """;

        mockMvc.perform(
                        post("/api/auftraege")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )

                .andExpect(
                        status().isNotFound())

                .andExpect(
                        jsonPath("$.status")
                                .value(404));
    }

    @Test
    void unbekannteKonfigurationSoll404Liefern()
            throws Exception {

        kundeAnlegen(
                "K-AUF-006",
                "Firma Sechs GmbH");

        String json = """
                {
                  "auftragsNummer": "AUF-REST-006",
                  "kundenNummer": "K-AUF-006",
                  "positionen": [
                    {
                      "positionsNummer": 10,
                      "menge": 1,
                      "einzelPreisHt": 1000.00,
                      "rabattProzent": 0.00,
                      "konfigurationsNummer": "LK-NICHT-DA"
                    }
                  ]
                }
                """;

        mockMvc.perform(
                        post("/api/auftraege")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )

                .andExpect(
                        status().isNotFound());
    }

    @Test
    void unbekannterAuftragSoll404Liefern()
            throws Exception {

        mockMvc.perform(
                        get(
                                "/api/auftraege/{nummer}",
                                "AUF-NICHT-DA")
                )

                .andExpect(
                        status().isNotFound())

                .andExpect(
                        jsonPath("$.title")
                                .value(
                                        "Ressource nicht gefunden"));
    }

    // ============================================================
    // HILFSMETHODEN
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
                """.formatted(nummer);

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
                      "menge": 3,
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
}