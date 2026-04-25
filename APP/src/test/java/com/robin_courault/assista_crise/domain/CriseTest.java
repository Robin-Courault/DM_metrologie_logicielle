package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.AutoriteTestSamples.*;
import static com.robin_courault.assista_crise.domain.CriseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CriseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Crise.class);
        Crise crise1 = getCriseSample1();
        Crise crise2 = new Crise();
        assertThat(crise1).isNotEqualTo(crise2);

        crise2.setId(crise1.getId());
        assertThat(crise1).isEqualTo(crise2);

        crise2 = getCriseSample2();
        assertThat(crise1).isNotEqualTo(crise2);
    }

    @Test
    void autoriteTest() {
        Crise crise = getCriseRandomSampleGenerator();
        Autorite autoriteBack = getAutoriteRandomSampleGenerator();

        crise.setAutorite(autoriteBack);
        assertThat(crise.getAutorite()).isEqualTo(autoriteBack);

        crise.autorite(null);
        assertThat(crise.getAutorite()).isNull();
    }
}
