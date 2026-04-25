package com.robin_courault.assista_crise.service.mapper;

import static com.robin_courault.assista_crise.domain.CitoyenAsserts.*;
import static com.robin_courault.assista_crise.domain.CitoyenTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CitoyenMapperTest {

    private CitoyenMapper citoyenMapper;

    @BeforeEach
    void setUp() {
        citoyenMapper = new CitoyenMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCitoyenSample1();
        var actual = citoyenMapper.toEntity(citoyenMapper.toDto(expected));
        assertCitoyenAllPropertiesEquals(expected, actual);
    }
}
