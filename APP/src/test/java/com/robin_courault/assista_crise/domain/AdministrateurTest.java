package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.AdministrateurTestSamples.*;
import static com.robin_courault.assista_crise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdministrateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Administrateur.class);
        Administrateur administrateur1 = getAdministrateurSample1();
        Administrateur administrateur2 = new Administrateur();
        assertThat(administrateur1).isNotEqualTo(administrateur2);

        administrateur2.setId(administrateur1.getId());
        assertThat(administrateur1).isEqualTo(administrateur2);

        administrateur2 = getAdministrateurSample2();
        assertThat(administrateur1).isNotEqualTo(administrateur2);
    }

    @Test
    void utilisateurTest() {
        Administrateur administrateur = getAdministrateurRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        administrateur.setUtilisateur(utilisateurBack);
        assertThat(administrateur.getUtilisateur()).isEqualTo(utilisateurBack);

        administrateur.utilisateur(null);
        assertThat(administrateur.getUtilisateur()).isNull();
    }
}
