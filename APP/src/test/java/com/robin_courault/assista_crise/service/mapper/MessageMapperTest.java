package com.robin_courault.assista_crise.service.mapper;

import static com.robin_courault.assista_crise.domain.MessageAsserts.*;
import static com.robin_courault.assista_crise.domain.MessageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageMapperTest {

    private MessageMapper messageMapper;

    @BeforeEach
    void setUp() {
        messageMapper = new MessageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMessageSample1();
        var actual = messageMapper.toEntity(messageMapper.toDto(expected));
        assertMessageAllPropertiesEquals(expected, actual);
    }
}
