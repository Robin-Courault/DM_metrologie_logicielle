package com.robin_courault.assista_crise.web.rest;

import static com.robin_courault.assista_crise.domain.SinistreAsserts.*;
import static com.robin_courault.assista_crise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin_courault.assista_crise.IntegrationTest;
import com.robin_courault.assista_crise.domain.Sinistre;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.repository.SinistreRepository;
import com.robin_courault.assista_crise.service.SinistreService;
import com.robin_courault.assista_crise.service.dto.SinistreDTO;
import com.robin_courault.assista_crise.service.mapper.SinistreMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SinistreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SinistreResourceIT {

    private static final String ENTITY_API_URL = "/api/sinistres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SinistreRepository sinistreRepository;

    @Mock
    private SinistreRepository sinistreRepositoryMock;

    @Autowired
    private SinistreMapper sinistreMapper;

    @Mock
    private SinistreService sinistreServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSinistreMockMvc;

    private Sinistre sinistre;

    private Sinistre insertedSinistre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sinistre createEntity(EntityManager em) {
        Sinistre sinistre = new Sinistre();
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        sinistre.setUtilisateur(utilisateur);
        return sinistre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sinistre createUpdatedEntity(EntityManager em) {
        Sinistre updatedSinistre = new Sinistre();
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createUpdatedEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        updatedSinistre.setUtilisateur(utilisateur);
        return updatedSinistre;
    }

    @BeforeEach
    void initTest() {
        sinistre = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSinistre != null) {
            sinistreRepository.delete(insertedSinistre);
            insertedSinistre = null;
        }
    }

    @Test
    @Transactional
    void createSinistre() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);
        var returnedSinistreDTO = om.readValue(
            restSinistreMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SinistreDTO.class
        );

        // Validate the Sinistre in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSinistre = sinistreMapper.toEntity(returnedSinistreDTO);
        assertSinistreUpdatableFieldsEquals(returnedSinistre, getPersistedSinistre(returnedSinistre));

        insertedSinistre = returnedSinistre;
    }

    @Test
    @Transactional
    void createSinistreWithExistingId() throws Exception {
        // Create the Sinistre with an existing ID
        sinistre.setId(1L);
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSinistreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSinistres() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        // Get all the sinistreList
        restSinistreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sinistre.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSinistresWithEagerRelationshipsIsEnabled() throws Exception {
        when(sinistreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSinistreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sinistreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSinistresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sinistreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSinistreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(sinistreRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSinistre() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        // Get the sinistre
        restSinistreMockMvc
            .perform(get(ENTITY_API_URL_ID, sinistre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sinistre.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSinistre() throws Exception {
        // Get the sinistre
        restSinistreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSinistre() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sinistre
        Sinistre updatedSinistre = sinistreRepository.findById(sinistre.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSinistre are not directly saved in db
        em.detach(updatedSinistre);
        SinistreDTO sinistreDTO = sinistreMapper.toDto(updatedSinistre);

        restSinistreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sinistreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sinistreDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSinistreToMatchAllProperties(updatedSinistre);
    }

    @Test
    @Transactional
    void putNonExistingSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sinistreDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSinistreWithPatch() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sinistre using partial update
        Sinistre partialUpdatedSinistre = new Sinistre();
        partialUpdatedSinistre.setId(sinistre.getId());

        restSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSinistre.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSinistre))
            )
            .andExpect(status().isOk());

        // Validate the Sinistre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSinistreUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSinistre, sinistre), getPersistedSinistre(sinistre));
    }

    @Test
    @Transactional
    void fullUpdateSinistreWithPatch() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sinistre using partial update
        Sinistre partialUpdatedSinistre = new Sinistre();
        partialUpdatedSinistre.setId(sinistre.getId());

        restSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSinistre.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSinistre))
            )
            .andExpect(status().isOk());

        // Validate the Sinistre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSinistreUpdatableFieldsEquals(partialUpdatedSinistre, getPersistedSinistre(partialUpdatedSinistre));
    }

    @Test
    @Transactional
    void patchNonExistingSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sinistreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sinistreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSinistre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sinistre.setId(longCount.incrementAndGet());

        // Create the Sinistre
        SinistreDTO sinistreDTO = sinistreMapper.toDto(sinistre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSinistreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sinistreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sinistre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSinistre() throws Exception {
        // Initialize the database
        insertedSinistre = sinistreRepository.saveAndFlush(sinistre);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sinistre
        restSinistreMockMvc
            .perform(delete(ENTITY_API_URL_ID, sinistre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sinistreRepository.count();
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

    protected Sinistre getPersistedSinistre(Sinistre sinistre) {
        return sinistreRepository.findById(sinistre.getId()).orElseThrow();
    }

    protected void assertPersistedSinistreToMatchAllProperties(Sinistre expectedSinistre) {
        assertSinistreAllPropertiesEquals(expectedSinistre, getPersistedSinistre(expectedSinistre));
    }

    protected void assertPersistedSinistreToMatchUpdatableProperties(Sinistre expectedSinistre) {
        assertSinistreAllUpdatablePropertiesEquals(expectedSinistre, getPersistedSinistre(expectedSinistre));
    }
}
