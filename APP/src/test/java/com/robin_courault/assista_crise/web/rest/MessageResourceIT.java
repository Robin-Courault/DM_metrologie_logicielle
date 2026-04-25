package com.robin_courault.assista_crise.web.rest;

import static com.robin_courault.assista_crise.domain.MessageAsserts.*;
import static com.robin_courault.assista_crise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin_courault.assista_crise.IntegrationTest;
import com.robin_courault.assista_crise.domain.Message;
import com.robin_courault.assista_crise.domain.SalonDiscussion;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.repository.MessageRepository;
import com.robin_courault.assista_crise.service.MessageService;
import com.robin_courault.assista_crise.service.dto.MessageDTO;
import com.robin_courault.assista_crise.service.mapper.MessageMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MessageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MessageResourceIT {

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_ENVOI = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ENVOI = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MessageRepository messageRepository;

    @Mock
    private MessageRepository messageRepositoryMock;

    @Autowired
    private MessageMapper messageMapper;

    @Mock
    private MessageService messageServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageMockMvc;

    private Message message;

    private Message insertedMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createEntity(EntityManager em) {
        Message message = new Message().contenu(DEFAULT_CONTENU).dateEnvoi(DEFAULT_DATE_ENVOI);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        message.setUtilisateur(utilisateur);
        // Add required entity
        SalonDiscussion salonDiscussion;
        if (TestUtil.findAll(em, SalonDiscussion.class).isEmpty()) {
            salonDiscussion = SalonDiscussionResourceIT.createEntity();
            em.persist(salonDiscussion);
            em.flush();
        } else {
            salonDiscussion = TestUtil.findAll(em, SalonDiscussion.class).get(0);
        }
        message.setSalonDiscussion(salonDiscussion);
        return message;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createUpdatedEntity(EntityManager em) {
        Message updatedMessage = new Message().contenu(UPDATED_CONTENU).dateEnvoi(UPDATED_DATE_ENVOI);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createUpdatedEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        updatedMessage.setUtilisateur(utilisateur);
        // Add required entity
        SalonDiscussion salonDiscussion;
        if (TestUtil.findAll(em, SalonDiscussion.class).isEmpty()) {
            salonDiscussion = SalonDiscussionResourceIT.createUpdatedEntity();
            em.persist(salonDiscussion);
            em.flush();
        } else {
            salonDiscussion = TestUtil.findAll(em, SalonDiscussion.class).get(0);
        }
        updatedMessage.setSalonDiscussion(salonDiscussion);
        return updatedMessage;
    }

    @BeforeEach
    void initTest() {
        message = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMessage != null) {
            messageRepository.delete(insertedMessage);
            insertedMessage = null;
        }
    }

    @Test
    @Transactional
    void createMessage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);
        var returnedMessageDTO = om.readValue(
            restMessageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MessageDTO.class
        );

        // Validate the Message in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMessage = messageMapper.toEntity(returnedMessageDTO);
        assertMessageUpdatableFieldsEquals(returnedMessage, getPersistedMessage(returnedMessage));

        insertedMessage = returnedMessage;
    }

    @Test
    @Transactional
    void createMessageWithExistingId() throws Exception {
        // Create the Message with an existing ID
        message.setId(1L);
        MessageDTO messageDTO = messageMapper.toDto(message);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContenuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        message.setContenu(null);

        // Create the Message, which fails.
        MessageDTO messageDTO = messageMapper.toDto(message);

        restMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateEnvoiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        message.setDateEnvoi(null);

        // Create the Message, which fails.
        MessageDTO messageDTO = messageMapper.toDto(message);

        restMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMessages() throws Exception {
        // Initialize the database
        insertedMessage = messageRepository.saveAndFlush(message);

        // Get all the messageList
        restMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].dateEnvoi").value(hasItem(DEFAULT_DATE_ENVOI.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMessagesWithEagerRelationshipsIsEnabled() throws Exception {
        when(messageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMessageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(messageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMessagesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(messageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMessageMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(messageRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMessage() throws Exception {
        // Initialize the database
        insertedMessage = messageRepository.saveAndFlush(message);

        // Get the message
        restMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(message.getId().intValue()))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU))
            .andExpect(jsonPath("$.dateEnvoi").value(DEFAULT_DATE_ENVOI.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMessage() throws Exception {
        // Initialize the database
        insertedMessage = messageRepository.saveAndFlush(message);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the message
        Message updatedMessage = messageRepository.findById(message.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMessage are not directly saved in db
        em.detach(updatedMessage);
        updatedMessage.contenu(UPDATED_CONTENU).dateEnvoi(UPDATED_DATE_ENVOI);
        MessageDTO messageDTO = messageMapper.toDto(updatedMessage);

        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMessageToMatchAllProperties(updatedMessage);
    }

    @Test
    @Transactional
    void putNonExistingMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        message.setId(longCount.incrementAndGet());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        message.setId(longCount.incrementAndGet());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        message.setId(longCount.incrementAndGet());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        insertedMessage = messageRepository.saveAndFlush(message);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        partialUpdatedMessage.contenu(UPDATED_CONTENU).dateEnvoi(UPDATED_DATE_ENVOI);

        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessage))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMessage, message), getPersistedMessage(message));
    }

    @Test
    @Transactional
    void fullUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        insertedMessage = messageRepository.saveAndFlush(message);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        partialUpdatedMessage.contenu(UPDATED_CONTENU).dateEnvoi(UPDATED_DATE_ENVOI);

        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessage))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessageUpdatableFieldsEquals(partialUpdatedMessage, getPersistedMessage(partialUpdatedMessage));
    }

    @Test
    @Transactional
    void patchNonExistingMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        message.setId(longCount.incrementAndGet());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, messageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        message.setId(longCount.incrementAndGet());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMessage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        message.setId(longCount.incrementAndGet());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(messageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Message in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMessage() throws Exception {
        // Initialize the database
        insertedMessage = messageRepository.saveAndFlush(message);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the message
        restMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, message.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return messageRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Message getPersistedMessage(Message message) {
        return messageRepository.findById(message.getId()).orElseThrow();
    }

    protected void assertPersistedMessageToMatchAllProperties(Message expectedMessage) {
        assertMessageAllPropertiesEquals(expectedMessage, getPersistedMessage(expectedMessage));
    }

    protected void assertPersistedMessageToMatchUpdatableProperties(Message expectedMessage) {
        assertMessageAllUpdatablePropertiesEquals(expectedMessage, getPersistedMessage(expectedMessage));
    }
}
