package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.AgentTestSamples.*;
import static com.robin_courault.assista_crise.domain.AutoriteTestSamples.*;
import static com.robin_courault.assista_crise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agent.class);
        Agent agent1 = getAgentSample1();
        Agent agent2 = new Agent();
        assertThat(agent1).isNotEqualTo(agent2);

        agent2.setId(agent1.getId());
        assertThat(agent1).isEqualTo(agent2);

        agent2 = getAgentSample2();
        assertThat(agent1).isNotEqualTo(agent2);
    }

    @Test
    void utilisateurTest() {
        Agent agent = getAgentRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        agent.setUtilisateur(utilisateurBack);
        assertThat(agent.getUtilisateur()).isEqualTo(utilisateurBack);

        agent.utilisateur(null);
        assertThat(agent.getUtilisateur()).isNull();
    }

    @Test
    void autoriteTest() {
        Agent agent = getAgentRandomSampleGenerator();
        Autorite autoriteBack = getAutoriteRandomSampleGenerator();

        agent.setAutorite(autoriteBack);
        assertThat(agent.getAutorite()).isEqualTo(autoriteBack);

        agent.autorite(null);
        assertThat(agent.getAutorite()).isNull();
    }
}
