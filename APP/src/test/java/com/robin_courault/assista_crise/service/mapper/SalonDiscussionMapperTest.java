package com.robin_courault.assista_crise.service.mapper;

import static com.robin_courault.assista_crise.domain.SalonDiscussionAsserts.*;
import static com.robin_courault.assista_crise.domain.SalonDiscussionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalonDiscussionMapperTest {

    private SalonDiscussionMapper salonDiscussionMapper;

    @BeforeEach
    void setUp() {
        salonDiscussionMapper = new SalonDiscussionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSalonDiscussionSample1();
        var actual = salonDiscussionMapper.toEntity(salonDiscussionMapper.toDto(expected));
        assertSalonDiscussionAllPropertiesEquals(expected, actual);
    }
}
