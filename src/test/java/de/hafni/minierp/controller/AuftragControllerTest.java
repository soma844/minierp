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
                "K-20001",
                "Musterbau GmbH");

        konfigurationAnlegen(
                "LK-20001");

        String json = """
                {
                  "auftragsNummer": "AUF-20001",
                  "kundenNummer": "K-20001",
                  "positionen": [
                    {
                      "positionsNummer": 10,
                      "menge": 3,
                      "einzelPreisHt": 2300.00,
                      "rabattProzent": 10.00,
                      "konfigurationsNummer": "LK-20001"
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
                                "/api/auftraege/AUF-20001"))

                .andExpect(
                        jsonPath("$.auftragsNummer")
                                .value("AUF-20001"))

                .andExpect(
                        jsonPath("$.kundenNummer")
                                .value("K-20001"))

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
                "K-20002",
                "Firma Zwei GmbH");

        konfigurationAnlegen(
                "LK-20002");

        auftragAnlegen(
                "AUF-20002",
                "K-20002",
                "LK-20002");

        mockMvc.perform(
                        get(
                                "/api/auftraege/{nummer}",
                                "AUF-20002")
                )

                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.auftragsNummer")
                                .value("AUF-20002"))

                .andExpect(
                        jsonPath("$.status")
                                .value("ENTWURF"))

                .andExpect(
                        jsonPath("$.positionen[0].konfigurationsNummer")
                                .value("LK-20002"));
    }

    @Test
    void auftragMitFreigegebenerKonfigurationSollFreigegebenWerden()
            throws Exception {

        kundeAnlegen(
                "K-20003",
                "Firma Drei GmbH");

        konfigurationAnlegen(
                "LK-20003");

        konfigurationPruefenUndFreigeben(
                "LK-20003");

        auftragAnlegen(
                "AUF-20003",
                "K-20003",
                "LK-20003");

        mockMvc.perform(
                        post(
                                "/api/auftraege/{nummer}/freigeben",
                                "AUF-20003")
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
         * Damit prÃ¼fen wir auch unsere
         * Auftrag.rehydrieren()-Logik.
         */
        mockMvc.perform(
                        get(
                                "/api/auftraege/{nummer}",
                                "AUF-20003")
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
                "K-20004",
                "Firma Vier GmbH");

        konfigurationAnlegen(
                "LK-20004");

        auftragAnlegen(
                "AUF-20004",
                "K-20004",
                "LK-20004");

        mockMvc.perform(
                        post(
                                "/api/auftraege/{nummer}/freigeben",
                                "AUF-20004")
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
                  "auftragsNummer": "AUF-20005",
                  "kundenNummer": "K-29999",
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
                "K-20006",
                "Firma Sechs GmbH");

        String json = """
                {
                  "auftragsNummer": "AUF-20006",
                  "kundenNummer": "K-20006",
                  "positionen": [
                    {
                      "positionsNummer": 10,
                      "menge": 1,
                      "einzelPreisHt": 1000.00,
                      "rabattProzent": 0.00,
                      "konfigurationsNummer": "LK-29999"
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
                                "AUF-29999")
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
