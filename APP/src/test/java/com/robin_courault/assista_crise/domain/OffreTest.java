package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.AnnonceTestSamples.*;
import static com.robin_courault.assista_crise.domain.CitoyenTestSamples.*;
import static com.robin_courault.assista_crise.domain.CriseTestSamples.*;
import static com.robin_courault.assista_crise.domain.DemandeTestSamples.*;
import static com.robin_courault.assista_crise.domain.OffreTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OffreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Offre.class);
        Offre offre1 = getOffreSample1();
        Offre offre2 = new Offre();
        assertThat(offre1).isNotEqualTo(offre2);

        offre2.setId(offre1.getId());
        assertThat(offre1).isEqualTo(offre2);

        offre2 = getOffreSample2();
        assertThat(offre1).isNotEqualTo(offre2);
    }

    @Test
    void annonceTest() {
        Offre offre = getOffreRandomSampleGenerator();
        Annonce annonceBack = getAnnonceRandomSampleGenerator();

        offre.setAnnonce(annonceBack);
        assertThat(offre.getAnnonce()).isEqualTo(annonceBack);

        offre.annonce(null);
        assertThat(offre.getAnnonce()).isNull();
    }

    @Test
    void citoyenTest() {
        Offre offre = getOffreRandomSampleGenerator();
        Citoyen citoyenBack = getCitoyenRandomSampleGenerator();

        offre.setCitoyen(citoyenBack);
        assertThat(offre.getCitoyen()).isEqualTo(citoyenBack);

        offre.citoyen(null);
        assertThat(offre.getCitoyen()).isNull();
    }

    @Test
    void criseTest() {
        Offre offre = getOffreRandomSampleGenerator();
        Crise criseBack = getCriseRandomSampleGenerator();

        offre.setCrise(criseBack);
        assertThat(offre.getCrise()).isEqualTo(criseBack);

        offre.crise(null);
        assertThat(offre.getCrise()).isNull();
    }

    @Test
    void demandesTest() {
        Offre offre = getOffreRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        offre.addDemandes(demandeBack);
        assertThat(offre.getDemandeses()).containsOnly(demandeBack);
        assertThat(demandeBack.getOffreses()).containsOnly(offre);

        offre.removeDemandes(demandeBack);
        assertThat(offre.getDemandeses()).doesNotContain(demandeBack);
        assertThat(demandeBack.getOffreses()).doesNotContain(offre);

        offre.demandeses(new HashSet<>(Set.of(demandeBack)));
        assertThat(offre.getDemandeses()).containsOnly(demandeBack);
        assertThat(demandeBack.getOffreses()).containsOnly(offre);

        offre.setDemandeses(new HashSet<>());
        assertThat(offre.getDemandeses()).doesNotContain(demandeBack);
        assertThat(demandeBack.getOffreses()).doesNotContain(offre);
    }
}
