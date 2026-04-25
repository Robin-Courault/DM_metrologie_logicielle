package com.robin_courault.assista_crise.service.mapper;

import static com.robin_courault.assista_crise.domain.UtilisateurAsserts.*;
import static com.robin_courault.assista_crise.domain.UtilisateurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilisateurMapperTest {

    private UtilisateurMapper utilisateurMapper;

    @BeforeEach
    void setUp() {
        utilisateurMapper = new UtilisateurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUtilisateurSample1();
        var actual = utilisateurMapper.toEntity(utilisateurMapper.toDto(expected));
        assertUtilisateurAllPropertiesEquals(expected, actual);
    }
}
