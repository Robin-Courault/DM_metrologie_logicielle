package com.robin_courault.assista_crise.service.mapper;

import static com.robin_courault.assista_crise.domain.CriseAsserts.*;
import static com.robin_courault.assista_crise.domain.CriseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CriseMapperTest {

    private CriseMapper criseMapper;

    @BeforeEach
    void setUp() {
        criseMapper = new CriseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCriseSample1();
        var actual = criseMapper.toEntity(criseMapper.toDto(expected));
        assertCriseAllPropertiesEquals(expected, actual);
    }
}
