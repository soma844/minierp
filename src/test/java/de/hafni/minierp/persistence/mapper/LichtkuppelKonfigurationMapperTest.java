package de.hafni.minierp.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.hafni.minierp.TestSzenarien;
import de.hafni.minierp.domain.konfiguration.LichtkuppelKonfiguration;
import de.hafni.minierp.domain.konfiguration.LichtkuppelFunktion;
import de.hafni.minierp.domain.konfiguration.TechnischerStatus;
import de.hafni.minierp.persistence.entity.LichtkuppelKonfigurationEntity;

class LichtkuppelKonfigurationMapperTest {

    @Test
    void freigegebeneKonfigurationSollHinUndZurueckGemapptWerden() {

        LichtkuppelKonfiguration original =
                TestSzenarien.freigegebeneFesteKonfiguration();

        LichtkuppelKonfigurationEntity entity =
                LichtkuppelKonfigurationMapper.toEntity(original);

        LichtkuppelKonfiguration geladen =
                LichtkuppelKonfigurationMapper.toDomain(entity);

        assertThat(geladen.getKonfigurationsNummer())
                .isEqualTo(original.getKonfigurationsNummer());

        assertThat(geladen.getFunktion())
                .isEqualTo(LichtkuppelFunktion.FEST);

        assertThat(geladen.getTechnischerStatus())
                .isEqualTo(TechnischerStatus.FREIGEGEBEN);

        assertThat(geladen.getProduktFamilie().getCode())
                .isEqualTo(original.getProduktFamilie().getCode());

        assertThat(geladen.getAufsetzkranz().getMaterial())
                .isEqualTo(original.getAufsetzkranz().getMaterial());

        assertThat(geladen.getFuellung().getDickeMm())
                .isEqualTo(original.getFuellung().getDickeMm());

        assertThat(geladen.getOeffnungsRahmen())
                .isNull();

        assertThat(geladen.getAntriebsSystem())
                .isNull();
    }
}