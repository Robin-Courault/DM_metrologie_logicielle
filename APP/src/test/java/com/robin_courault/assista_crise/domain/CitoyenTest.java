package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.CitoyenTestSamples.*;
import static com.robin_courault.assista_crise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CitoyenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Citoyen.class);
        Citoyen citoyen1 = getCitoyenSample1();
        Citoyen citoyen2 = new Citoyen();
        assertThat(citoyen1).isNotEqualTo(citoyen2);

        citoyen2.setId(citoyen1.getId());
        assertThat(citoyen1).isEqualTo(citoyen2);

        citoyen2 = getCitoyenSample2();
        assertThat(citoyen1).isNotEqualTo(citoyen2);
    }

    @Test
    void utilisateurTest() {
        Citoyen citoyen = getCitoyenRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        citoyen.setUtilisateur(utilisateurBack);
        assertThat(citoyen.getUtilisateur()).isEqualTo(utilisateurBack);

        citoyen.utilisateur(null);
        assertThat(citoyen.getUtilisateur()).isNull();
    }
}
