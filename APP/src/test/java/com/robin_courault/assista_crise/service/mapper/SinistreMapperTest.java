package com.robin_courault.assista_crise.service.mapper;

import static com.robin_courault.assista_crise.domain.SinistreAsserts.*;
import static com.robin_courault.assista_crise.domain.SinistreTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SinistreMapperTest {

    private SinistreMapper sinistreMapper;

    @BeforeEach
    void setUp() {
        sinistreMapper = new SinistreMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSinistreSample1();
        var actual = sinistreMapper.toEntity(sinistreMapper.toDto(expected));
        assertSinistreAllPropertiesEquals(expected, actual);
    }
}
