package com.robin_courault.assista_crise.web.rest;

import static com.robin_courault.assista_crise.domain.CitoyenAsserts.*;
import static com.robin_courault.assista_crise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin_courault.assista_crise.IntegrationTest;
import com.robin_courault.assista_crise.domain.Citoyen;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.repository.CitoyenRepository;
import com.robin_courault.assista_crise.service.CitoyenService;
import com.robin_courault.assista_crise.service.dto.CitoyenDTO;
import com.robin_courault.assista_crise.service.mapper.CitoyenMapper;
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
 * Integration tests for the {@link CitoyenResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CitoyenResourceIT {

    private static final String ENTITY_API_URL = "/api/citoyens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CitoyenRepository citoyenRepository;

    @Mock
    private CitoyenRepository citoyenRepositoryMock;

    @Autowired
    private CitoyenMapper citoyenMapper;

    @Mock
    private CitoyenService citoyenServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCitoyenMockMvc;

    private Citoyen citoyen;

    private Citoyen insertedCitoyen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Citoyen createEntity(EntityManager em) {
        Citoyen citoyen = new Citoyen();
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        citoyen.setUtilisateur(utilisateur);
        return citoyen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Citoyen createUpdatedEntity(EntityManager em) {
        Citoyen updatedCitoyen = new Citoyen();
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createUpdatedEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        updatedCitoyen.setUtilisateur(utilisateur);
        return updatedCitoyen;
    }

    @BeforeEach
    void initTest() {
        citoyen = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCitoyen != null) {
            citoyenRepository.delete(insertedCitoyen);
            insertedCitoyen = null;
        }
    }

    @Test
    @Transactional
    void createCitoyen() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Citoyen
        CitoyenDTO citoyenDTO = citoyenMapper.toDto(citoyen);
        var returnedCitoyenDTO = om.readValue(
            restCitoyenMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citoyenDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CitoyenDTO.class
        );

        // Validate the Citoyen in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCitoyen = citoyenMapper.toEntity(returnedCitoyenDTO);
        assertCitoyenUpdatableFieldsEquals(returnedCitoyen, getPersistedCitoyen(returnedCitoyen));

        insertedCitoyen = returnedCitoyen;
    }

    @Test
    @Transactional
    void createCitoyenWithExistingId() throws Exception {
        // Create the Citoyen with an existing ID
        citoyen.setId(1L);
        CitoyenDTO citoyenDTO = citoyenMapper.toDto(citoyen);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitoyenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citoyenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Citoyen in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCitoyens() throws Exception {
        // Initialize the database
        insertedCitoyen = citoyenRepository.saveAndFlush(citoyen);

        // Get all the citoyenList
        restCitoyenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(citoyen.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCitoyensWithEagerRelationshipsIsEnabled() throws Exception {
        when(citoyenServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCitoyenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(citoyenServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCitoyensWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(citoyenServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCitoyenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(citoyenRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCitoyen() throws Exception {
        // Initialize the database
        insertedCitoyen = citoyenRepository.saveAndFlush(citoyen);

        // Get the citoyen
        restCitoyenMockMvc
            .perform(get(ENTITY_API_URL_ID, citoyen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(citoyen.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCitoyen() throws Exception {
        // Get the citoyen
        restCitoyenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCitoyen() throws Exception {
        // Initialize the database
        insertedCitoyen = citoyenRepository.saveAndFlush(citoyen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the citoyen
        Citoyen updatedCitoyen = citoyenRepository.findById(citoyen.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCitoyen are not directly saved in db
        em.detach(updatedCitoyen);
        CitoyenDTO citoyenDTO = citoyenMapper.toDto(updatedCitoyen);

        restCitoyenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citoyenDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citoyenDTO))
            )
            .andExpect(status().isOk());

        // Validate the Citoyen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCitoyenToMatchAllProperties(updatedCitoyen);
    }

    @Test
    @Transactional
    void putNonExistingCitoyen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citoyen.setId(longCount.incrementAndGet());

        // Create the Citoyen
        CitoyenDTO citoyenDTO = citoyenMapper.toDto(citoyen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitoyenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citoyenDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citoyenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Citoyen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCitoyen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citoyen.setId(longCount.incrementAndGet());

        // Create the Citoyen
        CitoyenDTO citoyenDTO = citoyenMapper.toDto(citoyen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitoyenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(citoyenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Citoyen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCitoyen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citoyen.setId(longCount.incrementAndGet());

        // Create the Citoyen
        CitoyenDTO citoyenDTO = citoyenMapper.toDto(citoyen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitoyenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(citoyenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Citoyen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCitoyenWithPatch() throws Exception {
        // Initialize the database
        insertedCitoyen = citoyenRepository.saveAndFlush(citoyen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the citoyen using partial update
        Citoyen partialUpdatedCitoyen = new Citoyen();
        partialUpdatedCitoyen.setId(citoyen.getId());

        restCitoyenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCitoyen.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCitoyen))
            )
            .andExpect(status().isOk());

        // Validate the Citoyen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitoyenUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCitoyen, citoyen), getPersistedCitoyen(citoyen));
    }

    @Test
    @Transactional
    void fullUpdateCitoyenWithPatch() throws Exception {
        // Initialize the database
        insertedCitoyen = citoyenRepository.saveAndFlush(citoyen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the citoyen using partial update
        Citoyen partialUpdatedCitoyen = new Citoyen();
        partialUpdatedCitoyen.setId(citoyen.getId());

        restCitoyenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCitoyen.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCitoyen))
            )
            .andExpect(status().isOk());

        // Validate the Citoyen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCitoyenUpdatableFieldsEquals(partialUpdatedCitoyen, getPersistedCitoyen(partialUpdatedCitoyen));
    }

    @Test
    @Transactional
    void patchNonExistingCitoyen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citoyen.setId(longCount.incrementAndGet());

        // Create the Citoyen
        CitoyenDTO citoyenDTO = citoyenMapper.toDto(citoyen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitoyenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, citoyenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(citoyenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Citoyen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCitoyen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citoyen.setId(longCount.incrementAndGet());

        // Create the Citoyen
        CitoyenDTO citoyenDTO = citoyenMapper.toDto(citoyen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitoyenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(citoyenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Citoyen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCitoyen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        citoyen.setId(longCount.incrementAndGet());

        // Create the Citoyen
        CitoyenDTO citoyenDTO = citoyenMapper.toDto(citoyen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitoyenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(citoyenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Citoyen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCitoyen() throws Exception {
        // Initialize the database
        insertedCitoyen = citoyenRepository.saveAndFlush(citoyen);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the citoyen
        restCitoyenMockMvc
            .perform(delete(ENTITY_API_URL_ID, citoyen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return citoyenRepository.count();
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

    protected Citoyen getPersistedCitoyen(Citoyen citoyen) {
        return citoyenRepository.findById(citoyen.getId()).orElseThrow();
    }

    protected void assertPersistedCitoyenToMatchAllProperties(Citoyen expectedCitoyen) {
        assertCitoyenAllPropertiesEquals(expectedCitoyen, getPersistedCitoyen(expectedCitoyen));
    }

    protected void assertPersistedCitoyenToMatchUpdatableProperties(Citoyen expectedCitoyen) {
        assertCitoyenAllUpdatablePropertiesEquals(expectedCitoyen, getPersistedCitoyen(expectedCitoyen));
    }
}
