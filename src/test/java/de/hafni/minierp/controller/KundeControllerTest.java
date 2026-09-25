package de.hafni.minierp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
class KundeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void kundeSollPerRestAngelegtWerden()
            throws Exception {

        String json = """
                {
                  "kundenNummer": "K-REST-001",
                  "firmenName": "Musterbau GmbH"
                }
                """;

        mockMvc.perform(
                        post("/api/kunden")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )

                .andExpect(
                        status().isCreated())

                .andExpect(
                        header().string(
                                "Location",
                                "/api/kunden/K-REST-001"))

                .andExpect(
                        jsonPath("$.kundenNummer")
                                .value("K-REST-001"))

                .andExpect(
                        jsonPath("$.firmenName")
                                .value("Musterbau GmbH"));
    }

    @Test
    void kundeSollPerRestGelesenWerden()
            throws Exception {

        kundeAnlegen(
                "K-REST-002",
                "Testkunde GmbH"
        );

        mockMvc.perform(
                        get(
                                "/api/kunden/{kundenNummer}",
                                "K-REST-002")
                )

                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.kundenNummer")
                                .value("K-REST-002"))

                .andExpect(
                        jsonPath("$.firmenName")
                                .value("Testkunde GmbH"));
    }

    @Test
    void alleKundenSollenGeladenWerden()
            throws Exception {

        kundeAnlegen(
                "K-REST-003",
                "Firma Drei GmbH"
        );

        kundeAnlegen(
                "K-REST-004",
                "Firma Vier GmbH"
        );

        mockMvc.perform(
                        get("/api/kunden")
                )

                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$[0].kundenNummer")
                                .exists())

                .andExpect(
                        jsonPath("$[1].kundenNummer")
                                .exists());
    }

    @Test
    void kundeSollPerRestAktualisiertWerden()
            throws Exception {

        kundeAnlegen(
                "K-REST-005",
                "Alter Firmenname GmbH"
        );

        String json = """
                {
                  "kundenNummer": "K-REST-005",
                  "firmenName": "Neuer Firmenname GmbH"
                }
                """;

        mockMvc.perform(
                        put(
                                "/api/kunden/{kundenNummer}",
                                "K-REST-005")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )

                .andExpect(
                        status().isOk())

                .andExpect(
                        jsonPath("$.firmenName")
                                .value(
                                        "Neuer Firmenname GmbH"));
    }

    @Test
    void kundeSollPerRestGeloeschtWerden()
            throws Exception {

        kundeAnlegen(
                "K-REST-006",
                "Loeschbare Firma GmbH"
        );

        mockMvc.perform(
                        delete(
                                "/api/kunden/{kundenNummer}",
                                "K-REST-006")
                )

                .andExpect(
                        status().isNoContent());

        mockMvc.perform(
                        get(
                                "/api/kunden/{kundenNummer}",
                                "K-REST-006")
                )

                .andExpect(
                        status().isNotFound());
    }

    @Test
    void unbekannterKundeSoll404Liefern()
            throws Exception {

        mockMvc.perform(
                        get(
                                "/api/kunden/{kundenNummer}",
                                "K-NICHT-DA")
                )

                .andExpect(
                        status().isNotFound())

                .andExpect(
                        jsonPath("$.status")
                                .value(404))

                .andExpect(
                        jsonPath("$.title")
                                .value(
                                        "Ressource nicht gefunden"));
    }

    @Test
    void leererFirmennameSoll400Liefern()
            throws Exception {

        String json = """
                {
                  "kundenNummer": "K-REST-007",
                  "firmenName": ""
                }
                """;

        mockMvc.perform(
                        post("/api/kunden")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )

                .andExpect(
                        status().isBadRequest());
    }

    @Test
    void doppelteKundennummerSoll409Liefern()
            throws Exception {

        kundeAnlegen(
                "K-REST-008",
                "Erste Firma GmbH"
        );

        String json = """
                {
                  "kundenNummer": "K-REST-008",
                  "firmenName": "Andere Firma GmbH"
                }
                """;

        mockMvc.perform(
                        post("/api/kunden")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )

                .andExpect(
                        status().isConflict());
    }

    // ============================================================
    // TEST-HILFSMETHODE
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
                        firmenName
                );

        mockMvc.perform(
                        post("/api/kunden")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(json)
                )

                .andExpect(
                        status().isCreated());
    }
}