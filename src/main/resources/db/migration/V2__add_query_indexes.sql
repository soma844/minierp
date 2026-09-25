CREATE INDEX idx_konfiguration_status
    ON lichtkuppel_konfigurationen(technischer_status);

CREATE INDEX idx_konfiguration_funktion
    ON lichtkuppel_konfigurationen(funktion);

CREATE INDEX idx_auftrag_status
    ON auftraege(status);