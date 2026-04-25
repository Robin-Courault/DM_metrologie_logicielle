package com.robin_courault.assista_crise.service.mapper;

import static com.robin_courault.assista_crise.domain.AutoriteAsserts.*;
import static com.robin_courault.assista_crise.domain.AutoriteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutoriteMapperTest {

    private AutoriteMapper autoriteMapper;

    @BeforeEach
    void setUp() {
        autoriteMapper = new AutoriteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAutoriteSample1();
        var actual = autoriteMapper.toEntity(autoriteMapper.toDto(expected));
        assertAutoriteAllPropertiesEquals(expected, actual);
    }
}
