package com.robin_courault.assista_crise.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalonDiscussionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalonDiscussionDTO.class);
        SalonDiscussionDTO salonDiscussionDTO1 = new SalonDiscussionDTO();
        salonDiscussionDTO1.setId(1L);
        SalonDiscussionDTO salonDiscussionDTO2 = new SalonDiscussionDTO();
        assertThat(salonDiscussionDTO1).isNotEqualTo(salonDiscussionDTO2);
        salonDiscussionDTO2.setId(salonDiscussionDTO1.getId());
        assertThat(salonDiscussionDTO1).isEqualTo(salonDiscussionDTO2);
        salonDiscussionDTO2.setId(2L);
        assertThat(salonDiscussionDTO1).isNotEqualTo(salonDiscussionDTO2);
        salonDiscussionDTO1.setId(null);
        assertThat(salonDiscussionDTO1).isNotEqualTo(salonDiscussionDTO2);
    }
}
