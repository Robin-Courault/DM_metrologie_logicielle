package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.AdministrateurTestSamples.*;
import static com.robin_courault.assista_crise.domain.AgentTestSamples.*;
import static com.robin_courault.assista_crise.domain.CitoyenTestSamples.*;
import static com.robin_courault.assista_crise.domain.SalonDiscussionTestSamples.*;
import static com.robin_courault.assista_crise.domain.SinistreTestSamples.*;
import static com.robin_courault.assista_crise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilisateur.class);
        Utilisateur utilisateur1 = getUtilisateurSample1();
        Utilisateur utilisateur2 = new Utilisateur();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);

        utilisateur2.setId(utilisateur1.getId());
        assertThat(utilisateur1).isEqualTo(utilisateur2);

        utilisateur2 = getUtilisateurSample2();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);
    }

    @Test
    void salonsTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        SalonDiscussion salonDiscussionBack = getSalonDiscussionRandomSampleGenerator();

        utilisateur.addSalons(salonDiscussionBack);
        assertThat(utilisateur.getSalonses()).containsOnly(salonDiscussionBack);
        assertThat(salonDiscussionBack.getParticipantses()).containsOnly(utilisateur);

        utilisateur.removeSalons(salonDiscussionBack);
        assertThat(utilisateur.getSalonses()).doesNotContain(salonDiscussionBack);
        assertThat(salonDiscussionBack.getParticipantses()).doesNotContain(utilisateur);

        utilisateur.salonses(new HashSet<>(Set.of(salonDiscussionBack)));
        assertThat(utilisateur.getSalonses()).containsOnly(salonDiscussionBack);
        assertThat(salonDiscussionBack.getParticipantses()).containsOnly(utilisateur);

        utilisateur.setSalonses(new HashSet<>());
        assertThat(utilisateur.getSalonses()).doesNotContain(salonDiscussionBack);
        assertThat(salonDiscussionBack.getParticipantses()).doesNotContain(utilisateur);
    }

    @Test
    void sinistreTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Sinistre sinistreBack = getSinistreRandomSampleGenerator();

        utilisateur.setSinistre(sinistreBack);
        assertThat(utilisateur.getSinistre()).isEqualTo(sinistreBack);
        assertThat(sinistreBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.sinistre(null);
        assertThat(utilisateur.getSinistre()).isNull();
        assertThat(sinistreBack.getUtilisateur()).isNull();
    }

    @Test
    void citoyenTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Citoyen citoyenBack = getCitoyenRandomSampleGenerator();

        utilisateur.setCitoyen(citoyenBack);
        assertThat(utilisateur.getCitoyen()).isEqualTo(citoyenBack);
        assertThat(citoyenBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.citoyen(null);
        assertThat(utilisateur.getCitoyen()).isNull();
        assertThat(citoyenBack.getUtilisateur()).isNull();
    }

    @Test
    void agentTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Agent agentBack = getAgentRandomSampleGenerator();

        utilisateur.setAgent(agentBack);
        assertThat(utilisateur.getAgent()).isEqualTo(agentBack);
        assertThat(agentBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.agent(null);
        assertThat(utilisateur.getAgent()).isNull();
        assertThat(agentBack.getUtilisateur()).isNull();
    }

    @Test
    void administrateurTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Administrateur administrateurBack = getAdministrateurRandomSampleGenerator();

        utilisateur.setAdministrateur(administrateurBack);
        assertThat(utilisateur.getAdministrateur()).isEqualTo(administrateurBack);
        assertThat(administrateurBack.getUtilisateur()).isEqualTo(utilisateur);

        utilisateur.administrateur(null);
        assertThat(utilisateur.getAdministrateur()).isNull();
        assertThat(administrateurBack.getUtilisateur()).isNull();
    }
}
