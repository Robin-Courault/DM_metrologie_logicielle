package com.robin_courault.assista_crise.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AutoriteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutoriteDTO.class);
        AutoriteDTO autoriteDTO1 = new AutoriteDTO();
        autoriteDTO1.setId(1L);
        AutoriteDTO autoriteDTO2 = new AutoriteDTO();
        assertThat(autoriteDTO1).isNotEqualTo(autoriteDTO2);
        autoriteDTO2.setId(autoriteDTO1.getId());
        assertThat(autoriteDTO1).isEqualTo(autoriteDTO2);
        autoriteDTO2.setId(2L);
        assertThat(autoriteDTO1).isNotEqualTo(autoriteDTO2);
        autoriteDTO1.setId(null);
        assertThat(autoriteDTO1).isNotEqualTo(autoriteDTO2);
    }
}
