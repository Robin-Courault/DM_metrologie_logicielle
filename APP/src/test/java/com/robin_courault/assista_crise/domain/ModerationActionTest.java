package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.AdministrateurTestSamples.*;
import static com.robin_courault.assista_crise.domain.AnnonceTestSamples.*;
import static com.robin_courault.assista_crise.domain.ModerationActionTestSamples.*;
import static com.robin_courault.assista_crise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModerationActionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModerationAction.class);
        ModerationAction moderationAction1 = getModerationActionSample1();
        ModerationAction moderationAction2 = new ModerationAction();
        assertThat(moderationAction1).isNotEqualTo(moderationAction2);

        moderationAction2.setId(moderationAction1.getId());
        assertThat(moderationAction1).isEqualTo(moderationAction2);

        moderationAction2 = getModerationActionSample2();
        assertThat(moderationAction1).isNotEqualTo(moderationAction2);
    }

    @Test
    void administrateurTest() {
        ModerationAction moderationAction = getModerationActionRandomSampleGenerator();
        Administrateur administrateurBack = getAdministrateurRandomSampleGenerator();

        moderationAction.setAdministrateur(administrateurBack);
        assertThat(moderationAction.getAdministrateur()).isEqualTo(administrateurBack);

        moderationAction.administrateur(null);
        assertThat(moderationAction.getAdministrateur()).isNull();
    }

    @Test
    void annonceTest() {
        ModerationAction moderationAction = getModerationActionRandomSampleGenerator();
        Annonce annonceBack = getAnnonceRandomSampleGenerator();

        moderationAction.setAnnonce(annonceBack);
        assertThat(moderationAction.getAnnonce()).isEqualTo(annonceBack);

        moderationAction.annonce(null);
        assertThat(moderationAction.getAnnonce()).isNull();
    }

    @Test
    void utilisateurCibleTest() {
        ModerationAction moderationAction = getModerationActionRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        moderationAction.setUtilisateurCible(utilisateurBack);
        assertThat(moderationAction.getUtilisateurCible()).isEqualTo(utilisateurBack);

        moderationAction.utilisateurCible(null);
        assertThat(moderationAction.getUtilisateurCible()).isNull();
    }
}
