package com.robin_courault.assista_crise.web.rest;

import static com.robin_courault.assista_crise.domain.DemandeAsserts.*;
import static com.robin_courault.assista_crise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin_courault.assista_crise.IntegrationTest;
import com.robin_courault.assista_crise.domain.Annonce;
import com.robin_courault.assista_crise.domain.Crise;
import com.robin_courault.assista_crise.domain.Demande;
import com.robin_courault.assista_crise.domain.Sinistre;
import com.robin_courault.assista_crise.domain.enumeration.EtatDemande;
import com.robin_courault.assista_crise.repository.DemandeRepository;
import com.robin_courault.assista_crise.service.DemandeService;
import com.robin_courault.assista_crise.service.dto.DemandeDTO;
import com.robin_courault.assista_crise.service.mapper.DemandeMapper;
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
 * Integration tests for the {@link DemandeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DemandeResourceIT {

    private static final EtatDemande DEFAULT_ETAT_DEMANDE = EtatDemande.OUVERTE;
    private static final EtatDemande UPDATED_ETAT_DEMANDE = EtatDemande.EN_COURS;

    private static final Instant DEFAULT_DATE_FERMETURE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FERMETURE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final String ENTITY_API_URL = "/api/demandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DemandeRepository demandeRepository;

    @Mock
    private DemandeRepository demandeRepositoryMock;

    @Autowired
    private DemandeMapper demandeMapper;

    @Mock
    private DemandeService demandeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeMockMvc;

    private Demande demande;

    private Demande insertedDemande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Demande createEntity(EntityManager em) {
        Demande demande = new Demande().etatDemande(DEFAULT_ETAT_DEMANDE).dateFermeture(DEFAULT_DATE_FERMETURE).quantite(DEFAULT_QUANTITE);
        // Add required entity
        Annonce annonce;
        if (TestUtil.findAll(em, Annonce.class).isEmpty()) {
            annonce = AnnonceResourceIT.createEntity();
            em.persist(annonce);
            em.flush();
        } else {
            annonce = TestUtil.findAll(em, Annonce.class).get(0);
        }
        demande.setAnnonce(annonce);
        // Add required entity
        Sinistre sinistre;
        if (TestUtil.findAll(em, Sinistre.class).isEmpty()) {
            sinistre = SinistreResourceIT.createEntity(em);
            em.persist(sinistre);
            em.flush();
        } else {
            sinistre = TestUtil.findAll(em, Sinistre.class).get(0);
        }
        demande.setSinistre(sinistre);
        // Add required entity
        Crise crise;
        if (TestUtil.findAll(em, Crise.class).isEmpty()) {
            crise = CriseResourceIT.createEntity(em);
            em.persist(crise);
            em.flush();
        } else {
            crise = TestUtil.findAll(em, Crise.class).get(0);
        }
        demande.setCrise(crise);
        return demande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Demande createUpdatedEntity(EntityManager em) {
        Demande updatedDemande = new Demande()
            .etatDemande(UPDATED_ETAT_DEMANDE)
            .dateFermeture(UPDATED_DATE_FERMETURE)
            .quantite(UPDATED_QUANTITE);
        // Add required entity
        Annonce annonce;
        if (TestUtil.findAll(em, Annonce.class).isEmpty()) {
            annonce = AnnonceResourceIT.createUpdatedEntity();
            em.persist(annonce);
            em.flush();
        } else {
            annonce = TestUtil.findAll(em, Annonce.class).get(0);
        }
        updatedDemande.setAnnonce(annonce);
        // Add required entity
        Sinistre sinistre;
        if (TestUtil.findAll(em, Sinistre.class).isEmpty()) {
            sinistre = SinistreResourceIT.createUpdatedEntity(em);
            em.persist(sinistre);
            em.flush();
        } else {
            sinistre = TestUtil.findAll(em, Sinistre.class).get(0);
        }
        updatedDemande.setSinistre(sinistre);
        // Add required entity
        Crise crise;
        if (TestUtil.findAll(em, Crise.class).isEmpty()) {
            crise = CriseResourceIT.createUpdatedEntity(em);
            em.persist(crise);
            em.flush();
        } else {
            crise = TestUtil.findAll(em, Crise.class).get(0);
        }
        updatedDemande.setCrise(crise);
        return updatedDemande;
    }

    @BeforeEach
    void initTest() {
        demande = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDemande != null) {
            demandeRepository.delete(insertedDemande);
            insertedDemande = null;
        }
    }

    @Test
    @Transactional
    void createDemande() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);
        var returnedDemandeDTO = om.readValue(
            restDemandeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DemandeDTO.class
        );

        // Validate the Demande in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDemande = demandeMapper.toEntity(returnedDemandeDTO);
        assertDemandeUpdatableFieldsEquals(returnedDemande, getPersistedDemande(returnedDemande));

        insertedDemande = returnedDemande;
    }

    @Test
    @Transactional
    void createDemandeWithExistingId() throws Exception {
        // Create the Demande with an existing ID
        demande.setId(1L);
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDemandes() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get all the demandeList
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demande.getId().intValue())))
            .andExpect(jsonPath("$.[*].etatDemande").value(hasItem(DEFAULT_ETAT_DEMANDE.toString())))
            .andExpect(jsonPath("$.[*].dateFermeture").value(hasItem(DEFAULT_DATE_FERMETURE.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandesWithEagerRelationshipsIsEnabled() throws Exception {
        when(demandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(demandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(demandeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDemande() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get the demande
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL_ID, demande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demande.getId().intValue()))
            .andExpect(jsonPath("$.etatDemande").value(DEFAULT_ETAT_DEMANDE.toString()))
            .andExpect(jsonPath("$.dateFermeture").value(DEFAULT_DATE_FERMETURE.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE));
    }

    @Test
    @Transactional
    void getNonExistingDemande() throws Exception {
        // Get the demande
        restDemandeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDemande() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the demande
        Demande updatedDemande = demandeRepository.findById(demande.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDemande are not directly saved in db
        em.detach(updatedDemande);
        updatedDemande.etatDemande(UPDATED_ETAT_DEMANDE).dateFermeture(UPDATED_DATE_FERMETURE).quantite(UPDATED_QUANTITE);
        DemandeDTO demandeDTO = demandeMapper.toDto(updatedDemande);

        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDemandeToMatchAllProperties(updatedDemande);
    }

    @Test
    @Transactional
    void putNonExistingDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(longCount.incrementAndGet());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(longCount.incrementAndGet());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(demandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(longCount.incrementAndGet());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the demande using partial update
        Demande partialUpdatedDemande = new Demande();
        partialUpdatedDemande.setId(demande.getId());

        partialUpdatedDemande.etatDemande(UPDATED_ETAT_DEMANDE).dateFermeture(UPDATED_DATE_FERMETURE);

        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDemande))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDemandeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDemande, demande), getPersistedDemande(demande));
    }

    @Test
    @Transactional
    void fullUpdateDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the demande using partial update
        Demande partialUpdatedDemande = new Demande();
        partialUpdatedDemande.setId(demande.getId());

        partialUpdatedDemande.etatDemande(UPDATED_ETAT_DEMANDE).dateFermeture(UPDATED_DATE_FERMETURE).quantite(UPDATED_QUANTITE);

        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDemande))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDemandeUpdatableFieldsEquals(partialUpdatedDemande, getPersistedDemande(partialUpdatedDemande));
    }

    @Test
    @Transactional
    void patchNonExistingDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(longCount.incrementAndGet());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(demandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(longCount.incrementAndGet());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(demandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(longCount.incrementAndGet());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(demandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemande() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the demande
        restDemandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, demande.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return demandeRepository.count();
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

    protected Demande getPersistedDemande(Demande demande) {
        return demandeRepository.findById(demande.getId()).orElseThrow();
    }

    protected void assertPersistedDemandeToMatchAllProperties(Demande expectedDemande) {
        assertDemandeAllPropertiesEquals(expectedDemande, getPersistedDemande(expectedDemande));
    }

    protected void assertPersistedDemandeToMatchUpdatableProperties(Demande expectedDemande) {
        assertDemandeAllUpdatablePropertiesEquals(expectedDemande, getPersistedDemande(expectedDemande));
    }
}
