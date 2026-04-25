package com.robin_courault.assista_crise.web.rest;

import static com.robin_courault.assista_crise.domain.ModerationActionAsserts.*;
import static com.robin_courault.assista_crise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin_courault.assista_crise.IntegrationTest;
import com.robin_courault.assista_crise.domain.Administrateur;
import com.robin_courault.assista_crise.domain.ModerationAction;
import com.robin_courault.assista_crise.domain.enumeration.TypeModeration;
import com.robin_courault.assista_crise.repository.ModerationActionRepository;
import com.robin_courault.assista_crise.service.ModerationActionService;
import com.robin_courault.assista_crise.service.dto.ModerationActionDTO;
import com.robin_courault.assista_crise.service.mapper.ModerationActionMapper;
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
 * Integration tests for the {@link ModerationActionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ModerationActionResourceIT {

    private static final Instant DEFAULT_DATE_ACTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ACTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MOTIF = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBB";

    private static final TypeModeration DEFAULT_TYPE = TypeModeration.AVERTISSEMENT;
    private static final TypeModeration UPDATED_TYPE = TypeModeration.MASQUAGE;

    private static final String ENTITY_API_URL = "/api/moderation-actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModerationActionRepository moderationActionRepository;

    @Mock
    private ModerationActionRepository moderationActionRepositoryMock;

    @Autowired
    private ModerationActionMapper moderationActionMapper;

    @Mock
    private ModerationActionService moderationActionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModerationActionMockMvc;

    private ModerationAction moderationAction;

    private ModerationAction insertedModerationAction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModerationAction createEntity(EntityManager em) {
        ModerationAction moderationAction = new ModerationAction().dateAction(DEFAULT_DATE_ACTION).motif(DEFAULT_MOTIF).type(DEFAULT_TYPE);
        // Add required entity
        Administrateur administrateur;
        if (TestUtil.findAll(em, Administrateur.class).isEmpty()) {
            administrateur = AdministrateurResourceIT.createEntity(em);
            em.persist(administrateur);
            em.flush();
        } else {
            administrateur = TestUtil.findAll(em, Administrateur.class).get(0);
        }
        moderationAction.setAdministrateur(administrateur);
        return moderationAction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModerationAction createUpdatedEntity(EntityManager em) {
        ModerationAction updatedModerationAction = new ModerationAction()
            .dateAction(UPDATED_DATE_ACTION)
            .motif(UPDATED_MOTIF)
            .type(UPDATED_TYPE);
        // Add required entity
        Administrateur administrateur;
        if (TestUtil.findAll(em, Administrateur.class).isEmpty()) {
            administrateur = AdministrateurResourceIT.createUpdatedEntity(em);
            em.persist(administrateur);
            em.flush();
        } else {
            administrateur = TestUtil.findAll(em, Administrateur.class).get(0);
        }
        updatedModerationAction.setAdministrateur(administrateur);
        return updatedModerationAction;
    }

    @BeforeEach
    void initTest() {
        moderationAction = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedModerationAction != null) {
            moderationActionRepository.delete(insertedModerationAction);
            insertedModerationAction = null;
        }
    }

    @Test
    @Transactional
    void createModerationAction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);
        var returnedModerationActionDTO = om.readValue(
            restModerationActionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationActionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ModerationActionDTO.class
        );

        // Validate the ModerationAction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedModerationAction = moderationActionMapper.toEntity(returnedModerationActionDTO);
        assertModerationActionUpdatableFieldsEquals(returnedModerationAction, getPersistedModerationAction(returnedModerationAction));

        insertedModerationAction = returnedModerationAction;
    }

    @Test
    @Transactional
    void createModerationActionWithExistingId() throws Exception {
        // Create the ModerationAction with an existing ID
        moderationAction.setId(1L);
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModerationActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationActionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moderationAction.setDateAction(null);

        // Create the ModerationAction, which fails.
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        restModerationActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationActionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moderationAction.setType(null);

        // Create the ModerationAction, which fails.
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        restModerationActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationActionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllModerationActions() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        // Get all the moderationActionList
        restModerationActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moderationAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAction").value(hasItem(DEFAULT_DATE_ACTION.toString())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModerationActionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(moderationActionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restModerationActionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(moderationActionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModerationActionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(moderationActionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restModerationActionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(moderationActionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getModerationAction() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        // Get the moderationAction
        restModerationActionMockMvc
            .perform(get(ENTITY_API_URL_ID, moderationAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moderationAction.getId().intValue()))
            .andExpect(jsonPath("$.dateAction").value(DEFAULT_DATE_ACTION.toString()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingModerationAction() throws Exception {
        // Get the moderationAction
        restModerationActionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingModerationAction() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moderationAction
        ModerationAction updatedModerationAction = moderationActionRepository.findById(moderationAction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedModerationAction are not directly saved in db
        em.detach(updatedModerationAction);
        updatedModerationAction.dateAction(UPDATED_DATE_ACTION).motif(UPDATED_MOTIF).type(UPDATED_TYPE);
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(updatedModerationAction);

        restModerationActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moderationActionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moderationActionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedModerationActionToMatchAllProperties(updatedModerationAction);
    }

    @Test
    @Transactional
    void putNonExistingModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moderationActionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moderationActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moderationActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationActionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModerationActionWithPatch() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moderationAction using partial update
        ModerationAction partialUpdatedModerationAction = new ModerationAction();
        partialUpdatedModerationAction.setId(moderationAction.getId());

        partialUpdatedModerationAction.dateAction(UPDATED_DATE_ACTION).motif(UPDATED_MOTIF);

        restModerationActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModerationAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModerationAction))
            )
            .andExpect(status().isOk());

        // Validate the ModerationAction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModerationActionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedModerationAction, moderationAction),
            getPersistedModerationAction(moderationAction)
        );
    }

    @Test
    @Transactional
    void fullUpdateModerationActionWithPatch() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moderationAction using partial update
        ModerationAction partialUpdatedModerationAction = new ModerationAction();
        partialUpdatedModerationAction.setId(moderationAction.getId());

        partialUpdatedModerationAction.dateAction(UPDATED_DATE_ACTION).motif(UPDATED_MOTIF).type(UPDATED_TYPE);

        restModerationActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModerationAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModerationAction))
            )
            .andExpect(status().isOk());

        // Validate the ModerationAction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModerationActionUpdatableFieldsEquals(
            partialUpdatedModerationAction,
            getPersistedModerationAction(partialUpdatedModerationAction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moderationActionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moderationActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moderationActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModerationAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderationAction.setId(longCount.incrementAndGet());

        // Create the ModerationAction
        ModerationActionDTO moderationActionDTO = moderationActionMapper.toDto(moderationAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationActionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moderationActionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ModerationAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModerationAction() throws Exception {
        // Initialize the database
        insertedModerationAction = moderationActionRepository.saveAndFlush(moderationAction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the moderationAction
        restModerationActionMockMvc
            .perform(delete(ENTITY_API_URL_ID, moderationAction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return moderationActionRepository.count();
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

    protected ModerationAction getPersistedModerationAction(ModerationAction moderationAction) {
        return moderationActionRepository.findById(moderationAction.getId()).orElseThrow();
    }

    protected void assertPersistedModerationActionToMatchAllProperties(ModerationAction expectedModerationAction) {
        assertModerationActionAllPropertiesEquals(expectedModerationAction, getPersistedModerationAction(expectedModerationAction));
    }

    protected void assertPersistedModerationActionToMatchUpdatableProperties(ModerationAction expectedModerationAction) {
        assertModerationActionAllUpdatablePropertiesEquals(
            expectedModerationAction,
            getPersistedModerationAction(expectedModerationAction)
        );
    }
}
