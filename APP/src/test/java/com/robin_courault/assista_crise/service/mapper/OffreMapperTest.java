package com.robin_courault.assista_crise.service.mapper;

import static com.robin_courault.assista_crise.domain.OffreAsserts.*;
import static com.robin_courault.assista_crise.domain.OffreTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OffreMapperTest {

    private OffreMapper offreMapper;

    @BeforeEach
    void setUp() {
        offreMapper = new OffreMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOffreSample1();
        var actual = offreMapper.toEntity(offreMapper.toDto(expected));
        assertOffreAllPropertiesEquals(expected, actual);
    }
}
