package com.robin_courault.assista_crise.domain;

import static com.robin_courault.assista_crise.domain.MessageTestSamples.*;
import static com.robin_courault.assista_crise.domain.SalonDiscussionTestSamples.*;
import static com.robin_courault.assista_crise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.robin_courault.assista_crise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Message.class);
        Message message1 = getMessageSample1();
        Message message2 = new Message();
        assertThat(message1).isNotEqualTo(message2);

        message2.setId(message1.getId());
        assertThat(message1).isEqualTo(message2);

        message2 = getMessageSample2();
        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    void utilisateurTest() {
        Message message = getMessageRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        message.setUtilisateur(utilisateurBack);
        assertThat(message.getUtilisateur()).isEqualTo(utilisateurBack);

        message.utilisateur(null);
        assertThat(message.getUtilisateur()).isNull();
    }

    @Test
    void salonDiscussionTest() {
        Message message = getMessageRandomSampleGenerator();
        SalonDiscussion salonDiscussionBack = getSalonDiscussionRandomSampleGenerator();

        message.setSalonDiscussion(salonDiscussionBack);
        assertThat(message.getSalonDiscussion()).isEqualTo(salonDiscussionBack);

        message.salonDiscussion(null);
        assertThat(message.getSalonDiscussion()).isNull();
    }
}
