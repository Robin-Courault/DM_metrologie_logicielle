package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.AnnonceTestSamples.*;
import static com.robin_courault.assista_crise.domain.CriseTestSamples.*;
import static com.robin_courault.assista_crise.domain.DemandeTestSamples.*;
import static com.robin_courault.assista_crise.domain.OffreTestSamples.*;
import static com.robin_courault.assista_crise.domain.SalonDiscussionTestSamples.*;
import static com.robin_courault.assista_crise.domain.SinistreTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DemandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Demande.class);
        Demande demande1 = getDemandeSample1();
        Demande demande2 = new Demande();
        assertThat(demande1).isNotEqualTo(demande2);

        demande2.setId(demande1.getId());
        assertThat(demande1).isEqualTo(demande2);

        demande2 = getDemandeSample2();
        assertThat(demande1).isNotEqualTo(demande2);
    }

    @Test
    void annonceTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Annonce annonceBack = getAnnonceRandomSampleGenerator();

        demande.setAnnonce(annonceBack);
        assertThat(demande.getAnnonce()).isEqualTo(annonceBack);

        demande.annonce(null);
        assertThat(demande.getAnnonce()).isNull();
    }

    @Test
    void salonDiscussionTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        SalonDiscussion salonDiscussionBack = getSalonDiscussionRandomSampleGenerator();

        demande.setSalonDiscussion(salonDiscussionBack);
        assertThat(demande.getSalonDiscussion()).isEqualTo(salonDiscussionBack);

        demande.salonDiscussion(null);
        assertThat(demande.getSalonDiscussion()).isNull();
    }

    @Test
    void sinistreTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Sinistre sinistreBack = getSinistreRandomSampleGenerator();

        demande.setSinistre(sinistreBack);
        assertThat(demande.getSinistre()).isEqualTo(sinistreBack);

        demande.sinistre(null);
        assertThat(demande.getSinistre()).isNull();
    }

    @Test
    void criseTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Crise criseBack = getCriseRandomSampleGenerator();

        demande.setCrise(criseBack);
        assertThat(demande.getCrise()).isEqualTo(criseBack);

        demande.crise(null);
        assertThat(demande.getCrise()).isNull();
    }

    @Test
    void offresTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Offre offreBack = getOffreRandomSampleGenerator();

        demande.addOffres(offreBack);
        assertThat(demande.getOffreses()).containsOnly(offreBack);

        demande.removeOffres(offreBack);
        assertThat(demande.getOffreses()).doesNotContain(offreBack);

        demande.offreses(new HashSet<>(Set.of(offreBack)));
        assertThat(demande.getOffreses()).containsOnly(offreBack);

        demande.setOffreses(new HashSet<>());
        assertThat(demande.getOffreses()).doesNotContain(offreBack);
    }
}
