package com.robin_courault.assista_crise.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CitoyenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CitoyenDTO.class);
        CitoyenDTO citoyenDTO1 = new CitoyenDTO();
        citoyenDTO1.setId(1L);
        CitoyenDTO citoyenDTO2 = new CitoyenDTO();
        assertThat(citoyenDTO1).isNotEqualTo(citoyenDTO2);
        citoyenDTO2.setId(citoyenDTO1.getId());
        assertThat(citoyenDTO1).isEqualTo(citoyenDTO2);
        citoyenDTO2.setId(2L);
        assertThat(citoyenDTO1).isNotEqualTo(citoyenDTO2);
        citoyenDTO1.setId(null);
        assertThat(citoyenDTO1).isNotEqualTo(citoyenDTO2);
    }
}
