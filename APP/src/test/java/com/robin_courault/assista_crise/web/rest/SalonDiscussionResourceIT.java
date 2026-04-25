package com.robin_courault.assista_crise.web.rest;

import static com.robin_courault.assista_crise.domain.SalonDiscussionAsserts.*;
import static com.robin_courault.assista_crise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin_courault.assista_crise.IntegrationTest;
import com.robin_courault.assista_crise.domain.SalonDiscussion;
import com.robin_courault.assista_crise.repository.SalonDiscussionRepository;
import com.robin_courault.assista_crise.service.SalonDiscussionService;
import com.robin_courault.assista_crise.service.dto.SalonDiscussionDTO;
import com.robin_courault.assista_crise.service.mapper.SalonDiscussionMapper;
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
 * Integration tests for the {@link SalonDiscussionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SalonDiscussionResourceIT {

    private static final Instant DEFAULT_DATE_OUVERTURE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OUVERTURE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_OUVERT = false;
    private static final Boolean UPDATED_OUVERT = true;

    private static final String ENTITY_API_URL = "/api/salon-discussions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SalonDiscussionRepository salonDiscussionRepository;

    @Mock
    private SalonDiscussionRepository salonDiscussionRepositoryMock;

    @Autowired
    private SalonDiscussionMapper salonDiscussionMapper;

    @Mock
    private SalonDiscussionService salonDiscussionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalonDiscussionMockMvc;

    private SalonDiscussion salonDiscussion;

    private SalonDiscussion insertedSalonDiscussion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalonDiscussion createEntity() {
        return new SalonDiscussion().dateOuverture(DEFAULT_DATE_OUVERTURE).ouvert(DEFAULT_OUVERT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalonDiscussion createUpdatedEntity() {
        return new SalonDiscussion().dateOuverture(UPDATED_DATE_OUVERTURE).ouvert(UPDATED_OUVERT);
    }

    @BeforeEach
    void initTest() {
        salonDiscussion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSalonDiscussion != null) {
            salonDiscussionRepository.delete(insertedSalonDiscussion);
            insertedSalonDiscussion = null;
        }
    }

    @Test
    @Transactional
    void createSalonDiscussion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SalonDiscussion
        SalonDiscussionDTO salonDiscussionDTO = salonDiscussionMapper.toDto(salonDiscussion);
        var returnedSalonDiscussionDTO = om.readValue(
            restSalonDiscussionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salonDiscussionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SalonDiscussionDTO.class
        );

        // Validate the SalonDiscussion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSalonDiscussion = salonDiscussionMapper.toEntity(returnedSalonDiscussionDTO);
        assertSalonDiscussionUpdatableFieldsEquals(returnedSalonDiscussion, getPersistedSalonDiscussion(returnedSalonDiscussion));

        insertedSalonDiscussion = returnedSalonDiscussion;
    }

    @Test
    @Transactional
    void createSalonDiscussionWithExistingId() throws Exception {
        // Create the SalonDiscussion with an existing ID
        salonDiscussion.setId(1L);
        SalonDiscussionDTO salonDiscussionDTO = salonDiscussionMapper.toDto(salonDiscussion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalonDiscussionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salonDiscussionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SalonDiscussion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSalonDiscussions() throws Exception {
        // Initialize the database
        insertedSalonDiscussion = salonDiscussionRepository.saveAndFlush(salonDiscussion);

        // Get all the salonDiscussionList
        restSalonDiscussionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salonDiscussion.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOuverture").value(hasItem(DEFAULT_DATE_OUVERTURE.toString())))
            .andExpect(jsonPath("$.[*].ouvert").value(hasItem(DEFAULT_OUVERT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSalonDiscussionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(salonDiscussionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSalonDiscussionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(salonDiscussionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSalonDiscussionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(salonDiscussionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSalonDiscussionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(salonDiscussionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSalonDiscussion() throws Exception {
        // Initialize the database
        insertedSalonDiscussion = salonDiscussionRepository.saveAndFlush(salonDiscussion);

        // Get the salonDiscussion
        restSalonDiscussionMockMvc
            .perform(get(ENTITY_API_URL_ID, salonDiscussion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salonDiscussion.getId().intValue()))
            .andExpect(jsonPath("$.dateOuverture").value(DEFAULT_DATE_OUVERTURE.toString()))
            .andExpect(jsonPath("$.ouvert").value(DEFAULT_OUVERT));
    }

    @Test
    @Transactional
    void getNonExistingSalonDiscussion() throws Exception {
        // Get the salonDiscussion
        restSalonDiscussionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalonDiscussion() throws Exception {
        // Initialize the database
        insertedSalonDiscussion = salonDiscussionRepository.saveAndFlush(salonDiscussion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salonDiscussion
        SalonDiscussion updatedSalonDiscussion = salonDiscussionRepository.findById(salonDiscussion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSalonDiscussion are not directly saved in db
        em.detach(updatedSalonDiscussion);
        updatedSalonDiscussion.dateOuverture(UPDATED_DATE_OUVERTURE).ouvert(UPDATED_OUVERT);
        SalonDiscussionDTO salonDiscussionDTO = salonDiscussionMapper.toDto(updatedSalonDiscussion);

        restSalonDiscussionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salonDiscussionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(salonDiscussionDTO))
            )
            .andExpect(status().isOk());

        // Validate the SalonDiscussion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSalonDiscussionToMatchAllProperties(updatedSalonDiscussion);
    }

    @Test
    @Transactional
    void putNonExistingSalonDiscussion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salonDiscussion.setId(longCount.incrementAndGet());

        // Create the SalonDiscussion
        SalonDiscussionDTO salonDiscussionDTO = salonDiscussionMapper.toDto(salonDiscussion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalonDiscussionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salonDiscussionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(salonDiscussionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalonDiscussion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalonDiscussion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salonDiscussion.setId(longCount.incrementAndGet());

        // Create the SalonDiscussion
        SalonDiscussionDTO salonDiscussionDTO = salonDiscussionMapper.toDto(salonDiscussion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonDiscussionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(salonDiscussionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalonDiscussion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalonDiscussion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salonDiscussion.setId(longCount.incrementAndGet());

        // Create the SalonDiscussion
        SalonDiscussionDTO salonDiscussionDTO = salonDiscussionMapper.toDto(salonDiscussion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonDiscussionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salonDiscussionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalonDiscussion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalonDiscussionWithPatch() throws Exception {
        // Initialize the database
        insertedSalonDiscussion = salonDiscussionRepository.saveAndFlush(salonDiscussion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salonDiscussion using partial update
        SalonDiscussion partialUpdatedSalonDiscussion = new SalonDiscussion();
        partialUpdatedSalonDiscussion.setId(salonDiscussion.getId());

        restSalonDiscussionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalonDiscussion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSalonDiscussion))
            )
            .andExpect(status().isOk());

        // Validate the SalonDiscussion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSalonDiscussionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSalonDiscussion, salonDiscussion),
            getPersistedSalonDiscussion(salonDiscussion)
        );
    }

    @Test
    @Transactional
    void fullUpdateSalonDiscussionWithPatch() throws Exception {
        // Initialize the database
        insertedSalonDiscussion = salonDiscussionRepository.saveAndFlush(salonDiscussion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salonDiscussion using partial update
        SalonDiscussion partialUpdatedSalonDiscussion = new SalonDiscussion();
        partialUpdatedSalonDiscussion.setId(salonDiscussion.getId());

        partialUpdatedSalonDiscussion.dateOuverture(UPDATED_DATE_OUVERTURE).ouvert(UPDATED_OUVERT);

        restSalonDiscussionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalonDiscussion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSalonDiscussion))
            )
            .andExpect(status().isOk());

        // Validate the SalonDiscussion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSalonDiscussionUpdatableFieldsEquals(
            partialUpdatedSalonDiscussion,
            getPersistedSalonDiscussion(partialUpdatedSalonDiscussion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSalonDiscussion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salonDiscussion.setId(longCount.incrementAndGet());

        // Create the SalonDiscussion
        SalonDiscussionDTO salonDiscussionDTO = salonDiscussionMapper.toDto(salonDiscussion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalonDiscussionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salonDiscussionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(salonDiscussionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalonDiscussion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalonDiscussion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salonDiscussion.setId(longCount.incrementAndGet());

        // Create the SalonDiscussion
        SalonDiscussionDTO salonDiscussionDTO = salonDiscussionMapper.toDto(salonDiscussion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonDiscussionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(salonDiscussionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalonDiscussion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalonDiscussion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salonDiscussion.setId(longCount.incrementAndGet());

        // Create the SalonDiscussion
        SalonDiscussionDTO salonDiscussionDTO = salonDiscussionMapper.toDto(salonDiscussion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonDiscussionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(salonDiscussionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalonDiscussion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalonDiscussion() throws Exception {
        // Initialize the database
        insertedSalonDiscussion = salonDiscussionRepository.saveAndFlush(salonDiscussion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the salonDiscussion
        restSalonDiscussionMockMvc
            .perform(delete(ENTITY_API_URL_ID, salonDiscussion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return salonDiscussionRepository.count();
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

    protected SalonDiscussion getPersistedSalonDiscussion(SalonDiscussion salonDiscussion) {
        return salonDiscussionRepository.findById(salonDiscussion.getId()).orElseThrow();
    }

    protected void assertPersistedSalonDiscussionToMatchAllProperties(SalonDiscussion expectedSalonDiscussion) {
        assertSalonDiscussionAllPropertiesEquals(expectedSalonDiscussion, getPersistedSalonDiscussion(expectedSalonDiscussion));
    }

    protected void assertPersistedSalonDiscussionToMatchUpdatableProperties(SalonDiscussion expectedSalonDiscussion) {
        assertSalonDiscussionAllUpdatablePropertiesEquals(expectedSalonDiscussion, getPersistedSalonDiscussion(expectedSalonDiscussion));
    }
}
