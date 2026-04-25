package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.AnnonceTestSamples.*;
import static com.robin_courault.assista_crise.domain.DemandeTestSamples.*;
import static com.robin_courault.assista_crise.domain.OffreTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnnonceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Annonce.class);
        Annonce annonce1 = getAnnonceSample1();
        Annonce annonce2 = new Annonce();
        assertThat(annonce1).isNotEqualTo(annonce2);

        annonce2.setId(annonce1.getId());
        assertThat(annonce1).isEqualTo(annonce2);

        annonce2 = getAnnonceSample2();
        assertThat(annonce1).isNotEqualTo(annonce2);
    }

    @Test
    void demandeTest() {
        Annonce annonce = getAnnonceRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        annonce.setDemande(demandeBack);
        assertThat(annonce.getDemande()).isEqualTo(demandeBack);
        assertThat(demandeBack.getAnnonce()).isEqualTo(annonce);

        annonce.demande(null);
        assertThat(annonce.getDemande()).isNull();
        assertThat(demandeBack.getAnnonce()).isNull();
    }

    @Test
    void offreTest() {
        Annonce annonce = getAnnonceRandomSampleGenerator();
        Offre offreBack = getOffreRandomSampleGenerator();

        annonce.setOffre(offreBack);
        assertThat(annonce.getOffre()).isEqualTo(offreBack);
        assertThat(offreBack.getAnnonce()).isEqualTo(annonce);

        annonce.offre(null);
        assertThat(annonce.getOffre()).isNull();
        assertThat(offreBack.getAnnonce()).isNull();
    }
}
