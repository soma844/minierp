package de.hafni.minierp.domain.buchhaltung;

import java.math.BigDecimal;

public enum TvaSatz {

    STANDARD_20(new BigDecimal("20.0")),
    REDUZIERT_10(new BigDecimal("10.0")),
    REDUZIERT_5_5(new BigDecimal("5.5")),
    SONDER_2_1(new BigDecimal("2.1"));

    private final BigDecimal prozent;

    TvaSatz(BigDecimal prozent) {
        this.prozent = prozent;
    }

    public BigDecimal getProzent() {
        return prozent;
    }
}