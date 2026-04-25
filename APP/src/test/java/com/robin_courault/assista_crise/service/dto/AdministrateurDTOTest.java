package com.robin_courault.assista_crise.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdministrateurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdministrateurDTO.class);
        AdministrateurDTO administrateurDTO1 = new AdministrateurDTO();
        administrateurDTO1.setId(1L);
        AdministrateurDTO administrateurDTO2 = new AdministrateurDTO();
        assertThat(administrateurDTO1).isNotEqualTo(administrateurDTO2);
        administrateurDTO2.setId(administrateurDTO1.getId());
        assertThat(administrateurDTO1).isEqualTo(administrateurDTO2);
        administrateurDTO2.setId(2L);
        assertThat(administrateurDTO1).isNotEqualTo(administrateurDTO2);
        administrateurDTO1.setId(null);
        assertThat(administrateurDTO1).isNotEqualTo(administrateurDTO2);
    }
}
