package de.hafni.minierp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
class LichtkuppelKonfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void festeKonfigurationSollPerRestAngelegtWerden()
            throws Exception {

        String json = """
                {
                  "konfigurationsNummer": "LK-REST-001",
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
                """;

        mockMvc.perform(
                        post("/api/konfigurationen")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))

                .andExpect(status().isCreated())

                .andExpect(
                        header().string(
                                "Location",
                                "/api/konfigurationen/LK-REST-001"))

                .andExpect(
                        content()
                                .contentTypeCompatibleWith(
                                        MediaType.APPLICATION_JSON))

                .andExpect(
                        jsonPath("$.konfigurationsNummer")
                                .value("LK-REST-001"))

                .andExpect(
                        jsonPath("$.funktion")
                                .value("FEST"))

                .andExpect(
                        jsonPath("$.technischerStatus")
                                .value("ENTWURF"));
    }

    @Test
    void gespeicherteKonfigurationSollPerRestGelesenWerden()
            throws Exception {

        festeKonfigurationAnlegen(
                "LK-REST-002");

        mockMvc.perform(
                        get(
                                "/api/konfigurationen/{nummer}",
                                "LK-REST-002"))

                .andExpect(status().isOk())

                .andExpect(
                        jsonPath("$.konfigurationsNummer")
                                .value("LK-REST-002"))

                .andExpect(
                        jsonPath("$.produktCode")
                                .value("ECOLUX-PREMIUM"))

                .andExpect(
                        jsonPath("$.technischerStatus")
                                .value("ENTWURF"));
    }

    @Test
    void konfigurationSollPerRestGeprueftWerden()
            throws Exception {

        festeKonfigurationAnlegen(
                "LK-REST-003");

        mockMvc.perform(
                        post(
                                "/api/konfigurationen/{nummer}/pruefen",
                                "LK-REST-003"))

                .andExpect(status().isOk())

                .andExpect(
                        content()
                                .contentTypeCompatibleWith(
                                        MediaType.APPLICATION_JSON));

        mockMvc.perform(
                        get(
                                "/api/konfigurationen/{nummer}",
                                "LK-REST-003"))

                .andExpect(status().isOk())

                .andExpect(
                        jsonPath("$.technischerStatus")
                                .value("FREIGABEBEREIT"));
    }

    @Test
    void gepruefteKonfigurationSollPerRestFreigegebenWerden()
            throws Exception {

        festeKonfigurationAnlegen(
                "LK-REST-004");

        mockMvc.perform(
                        post(
                                "/api/konfigurationen/{nummer}/pruefen",
                                "LK-REST-004"))
                .andExpect(status().isOk());

        mockMvc.perform(
                        post(
                                "/api/konfigurationen/{nummer}/freigeben",
                                "LK-REST-004"))

                .andExpect(status().isOk())

                .andExpect(
                        jsonPath("$.technischerStatus")
                                .value("FREIGEGEBEN"));
    }

    @Test
    void entwurfSollPerRestGeloeschtWerden()
            throws Exception {

        festeKonfigurationAnlegen(
                "LK-REST-005");

        mockMvc.perform(
                        delete(
                                "/api/konfigurationen/{nummer}",
                                "LK-REST-005"))

                .andExpect(
                        status().isNoContent());

        mockMvc.perform(
                        get(
                                "/api/konfigurationen/{nummer}",
                                "LK-REST-005"))

                .andExpect(
                        status().isNotFound());
    }

    // =========================================================
    // TEST-HILFSMETHODE
    // =========================================================

    private void festeKonfigurationAnlegen(
            String nummer)
            throws Exception {

        String json = """
                {
                  "konfigurationsNummer": "%s",
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
                                .content(json))

                .andExpect(
                        status().isCreated());
    }
    @Test
    void unbekannteKonfigurationLiefert404() throws Exception {

        mockMvc.perform(
                get("/api/konfigurationen/LK-NICHT-DA")
        )
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.title")
                .value("Ressource nicht gefunden"));
    }
}