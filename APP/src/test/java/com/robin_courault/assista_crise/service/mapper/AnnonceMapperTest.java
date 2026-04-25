package com.robin_courault.assista_crise.service.mapper;

import static com.robin_courault.assista_crise.domain.AnnonceAsserts.*;
import static com.robin_courault.assista_crise.domain.AnnonceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnonceMapperTest {

    private AnnonceMapper annonceMapper;

    @BeforeEach
    void setUp() {
        annonceMapper = new AnnonceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAnnonceSample1();
        var actual = annonceMapper.toEntity(annonceMapper.toDto(expected));
        assertAnnonceAllPropertiesEquals(expected, actual);
    }
}
