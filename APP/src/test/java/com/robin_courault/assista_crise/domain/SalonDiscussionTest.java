package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.DemandeTestSamples.*;
import static com.robin_courault.assista_crise.domain.SalonDiscussionTestSamples.*;
import static com.robin_courault.assista_crise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SalonDiscussionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalonDiscussion.class);
        SalonDiscussion salonDiscussion1 = getSalonDiscussionSample1();
        SalonDiscussion salonDiscussion2 = new SalonDiscussion();
        assertThat(salonDiscussion1).isNotEqualTo(salonDiscussion2);

        salonDiscussion2.setId(salonDiscussion1.getId());
        assertThat(salonDiscussion1).isEqualTo(salonDiscussion2);

        salonDiscussion2 = getSalonDiscussionSample2();
        assertThat(salonDiscussion1).isNotEqualTo(salonDiscussion2);
    }

    @Test
    void participantsTest() {
        SalonDiscussion salonDiscussion = getSalonDiscussionRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        salonDiscussion.addParticipants(utilisateurBack);
        assertThat(salonDiscussion.getParticipantses()).containsOnly(utilisateurBack);

        salonDiscussion.removeParticipants(utilisateurBack);
        assertThat(salonDiscussion.getParticipantses()).doesNotContain(utilisateurBack);

        salonDiscussion.participantses(new HashSet<>(Set.of(utilisateurBack)));
        assertThat(salonDiscussion.getParticipantses()).containsOnly(utilisateurBack);

        salonDiscussion.setParticipantses(new HashSet<>());
        assertThat(salonDiscussion.getParticipantses()).doesNotContain(utilisateurBack);
    }

    @Test
    void demandeTest() {
        SalonDiscussion salonDiscussion = getSalonDiscussionRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        salonDiscussion.setDemande(demandeBack);
        assertThat(salonDiscussion.getDemande()).isEqualTo(demandeBack);
        assertThat(demandeBack.getSalonDiscussion()).isEqualTo(salonDiscussion);

        salonDiscussion.demande(null);
        assertThat(salonDiscussion.getDemande()).isNull();
        assertThat(demandeBack.getSalonDiscussion()).isNull();
    }
}
