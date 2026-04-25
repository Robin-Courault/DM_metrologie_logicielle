package com.robin_courault.assista_crise.service.mapper;

import static com.robin_courault.assista_crise.domain.ModerationActionAsserts.*;
import static com.robin_courault.assista_crise.domain.ModerationActionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModerationActionMapperTest {

    private ModerationActionMapper moderationActionMapper;

    @BeforeEach
    void setUp() {
        moderationActionMapper = new ModerationActionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getModerationActionSample1();
        var actual = moderationActionMapper.toEntity(moderationActionMapper.toDto(expected));
        assertModerationActionAllPropertiesEquals(expected, actual);
    }
}
