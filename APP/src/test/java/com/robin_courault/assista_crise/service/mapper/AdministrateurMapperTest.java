package com.robin_courault.assista_crise.service.mapper;

import static com.robin_courault.assista_crise.domain.AdministrateurAsserts.*;
import static com.robin_courault.assista_crise.domain.AdministrateurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdministrateurMapperTest {

    private AdministrateurMapper administrateurMapper;

    @BeforeEach
    void setUp() {
        administrateurMapper = new AdministrateurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdministrateurSample1();
        var actual = administrateurMapper.toEntity(administrateurMapper.toDto(expected));
        assertAdministrateurAllPropertiesEquals(expected, actual);
    }
}
