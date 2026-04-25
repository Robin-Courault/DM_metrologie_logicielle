package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.SinistreTestSamples.*;
import static com.robin_courault.assista_crise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SinistreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sinistre.class);
        Sinistre sinistre1 = getSinistreSample1();
        Sinistre sinistre2 = new Sinistre();
        assertThat(sinistre1).isNotEqualTo(sinistre2);

        sinistre2.setId(sinistre1.getId());
        assertThat(sinistre1).isEqualTo(sinistre2);

        sinistre2 = getSinistreSample2();
        assertThat(sinistre1).isNotEqualTo(sinistre2);
    }

    @Test
    void utilisateurTest() {
        Sinistre sinistre = getSinistreRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        sinistre.setUtilisateur(utilisateurBack);
        assertThat(sinistre.getUtilisateur()).isEqualTo(utilisateurBack);

        sinistre.utilisateur(null);
        assertThat(sinistre.getUtilisateur()).isNull();
    }
}
