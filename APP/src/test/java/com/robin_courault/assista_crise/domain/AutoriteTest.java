package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.AutoriteTestSamples.*;
import static com.robin_courault.assista_crise.domain.CriseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AutoriteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Autorite.class);
        Autorite autorite1 = getAutoriteSample1();
        Autorite autorite2 = new Autorite();
        assertThat(autorite1).isNotEqualTo(autorite2);

        autorite2.setId(autorite1.getId());
        assertThat(autorite1).isEqualTo(autorite2);

        autorite2 = getAutoriteSample2();
        assertThat(autorite1).isNotEqualTo(autorite2);
    }

    @Test
    void crisesTest() {
        Autorite autorite = getAutoriteRandomSampleGenerator();
        Crise criseBack = getCriseRandomSampleGenerator();

        autorite.addCrises(criseBack);
        assertThat(autorite.getCriseses()).containsOnly(criseBack);
        assertThat(criseBack.getAutorite()).isEqualTo(autorite);

        autorite.removeCrises(criseBack);
        assertThat(autorite.getCriseses()).doesNotContain(criseBack);
        assertThat(criseBack.getAutorite()).isNull();

        autorite.criseses(new HashSet<>(Set.of(criseBack)));
        assertThat(autorite.getCriseses()).containsOnly(criseBack);
        assertThat(criseBack.getAutorite()).isEqualTo(autorite);

        autorite.setCriseses(new HashSet<>());
        assertThat(autorite.getCriseses()).doesNotContain(criseBack);
        assertThat(criseBack.getAutorite()).isNull();
    }
}
