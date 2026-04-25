package com.robin_courault.assista_crise.web.rest;

import static com.robin_courault.assista_crise.domain.AdministrateurAsserts.*;
import static com.robin_courault.assista_crise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin_courault.assista_crise.IntegrationTest;
import com.robin_courault.assista_crise.domain.Administrateur;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.repository.AdministrateurRepository;
import com.robin_courault.assista_crise.service.AdministrateurService;
import com.robin_courault.assista_crise.service.dto.AdministrateurDTO;
import com.robin_courault.assista_crise.service.mapper.AdministrateurMapper;
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
 * Integration tests for the {@link AdministrateurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AdministrateurResourceIT {

    private static final String ENTITY_API_URL = "/api/administrateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Mock
    private AdministrateurRepository administrateurRepositoryMock;

    @Autowired
    private AdministrateurMapper administrateurMapper;

    @Mock
    private AdministrateurService administrateurServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdministrateurMockMvc;

    private Administrateur administrateur;

    private Administrateur insertedAdministrateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrateur createEntity(EntityManager em) {
        Administrateur administrateur = new Administrateur();
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        administrateur.setUtilisateur(utilisateur);
        return administrateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrateur createUpdatedEntity(EntityManager em) {
        Administrateur updatedAdministrateur = new Administrateur();
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createUpdatedEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        updatedAdministrateur.setUtilisateur(utilisateur);
        return updatedAdministrateur;
    }

    @BeforeEach
    void initTest() {
        administrateur = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAdministrateur != null) {
            administrateurRepository.delete(insertedAdministrateur);
            insertedAdministrateur = null;
        }
    }

    @Test
    @Transactional
    void createAdministrateur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Administrateur
        AdministrateurDTO administrateurDTO = administrateurMapper.toDto(administrateur);
        var returnedAdministrateurDTO = om.readValue(
            restAdministrateurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrateurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdministrateurDTO.class
        );

        // Validate the Administrateur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdministrateur = administrateurMapper.toEntity(returnedAdministrateurDTO);
        assertAdministrateurUpdatableFieldsEquals(returnedAdministrateur, getPersistedAdministrateur(returnedAdministrateur));

        insertedAdministrateur = returnedAdministrateur;
    }

    @Test
    @Transactional
    void createAdministrateurWithExistingId() throws Exception {
        // Create the Administrateur with an existing ID
        administrateur.setId(1L);
        AdministrateurDTO administrateurDTO = administrateurMapper.toDto(administrateur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministrateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdministrateurs() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        // Get all the administrateurList
        restAdministrateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrateur.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdministrateursWithEagerRelationshipsIsEnabled() throws Exception {
        when(administrateurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdministrateurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(administrateurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdministrateursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(administrateurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdministrateurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(administrateurRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAdministrateur() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        // Get the administrateur
        restAdministrateurMockMvc
            .perform(get(ENTITY_API_URL_ID, administrateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrateur.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAdministrateur() throws Exception {
        // Get the administrateur
        restAdministrateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdministrateur() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrateur
        Administrateur updatedAdministrateur = administrateurRepository.findById(administrateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdministrateur are not directly saved in db
        em.detach(updatedAdministrateur);
        AdministrateurDTO administrateurDTO = administrateurMapper.toDto(updatedAdministrateur);

        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administrateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administrateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdministrateurToMatchAllProperties(updatedAdministrateur);
    }

    @Test
    @Transactional
    void putNonExistingAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // Create the Administrateur
        AdministrateurDTO administrateurDTO = administrateurMapper.toDto(administrateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administrateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administrateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // Create the Administrateur
        AdministrateurDTO administrateurDTO = administrateurMapper.toDto(administrateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administrateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // Create the Administrateur
        AdministrateurDTO administrateurDTO = administrateurMapper.toDto(administrateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministrateurWithPatch() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrateur using partial update
        Administrateur partialUpdatedAdministrateur = new Administrateur();
        partialUpdatedAdministrateur.setId(administrateur.getId());

        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdministrateur))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdministrateurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdministrateur, administrateur),
            getPersistedAdministrateur(administrateur)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdministrateurWithPatch() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrateur using partial update
        Administrateur partialUpdatedAdministrateur = new Administrateur();
        partialUpdatedAdministrateur.setId(administrateur.getId());

        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdministrateur))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdministrateurUpdatableFieldsEquals(partialUpdatedAdministrateur, getPersistedAdministrateur(partialUpdatedAdministrateur));
    }

    @Test
    @Transactional
    void patchNonExistingAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // Create the Administrateur
        AdministrateurDTO administrateurDTO = administrateurMapper.toDto(administrateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administrateurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(administrateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // Create the Administrateur
        AdministrateurDTO administrateurDTO = administrateurMapper.toDto(administrateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(administrateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // Create the Administrateur
        AdministrateurDTO administrateurDTO = administrateurMapper.toDto(administrateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(administrateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrateur() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the administrateur
        restAdministrateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return administrateurRepository.count();
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

    protected Administrateur getPersistedAdministrateur(Administrateur administrateur) {
        return administrateurRepository.findById(administrateur.getId()).orElseThrow();
    }

    protected void assertPersistedAdministrateurToMatchAllProperties(Administrateur expectedAdministrateur) {
        assertAdministrateurAllPropertiesEquals(expectedAdministrateur, getPersistedAdministrateur(expectedAdministrateur));
    }

    protected void assertPersistedAdministrateurToMatchUpdatableProperties(Administrateur expectedAdministrateur) {
        assertAdministrateurAllUpdatablePropertiesEquals(expectedAdministrateur, getPersistedAdministrateur(expectedAdministrateur));
    }
}
