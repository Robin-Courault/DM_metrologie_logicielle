package com.robin_courault.assista_crise.web.rest;

import static com.robin_courault.assista_crise.domain.OffreAsserts.*;
import static com.robin_courault.assista_crise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin_courault.assista_crise.IntegrationTest;
import com.robin_courault.assista_crise.domain.Annonce;
import com.robin_courault.assista_crise.domain.Citoyen;
import com.robin_courault.assista_crise.domain.Crise;
import com.robin_courault.assista_crise.domain.Offre;
import com.robin_courault.assista_crise.repository.OffreRepository;
import com.robin_courault.assista_crise.service.OffreService;
import com.robin_courault.assista_crise.service.dto.OffreDTO;
import com.robin_courault.assista_crise.service.mapper.OffreMapper;
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
 * Integration tests for the {@link OffreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OffreResourceIT {

    private static final Instant DEFAULT_DISPONIBLE_DE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DISPONIBLE_DE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DISPONIBLE_JUSQUA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DISPONIBLE_JUSQUA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final String ENTITY_API_URL = "/api/offres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OffreRepository offreRepository;

    @Mock
    private OffreRepository offreRepositoryMock;

    @Autowired
    private OffreMapper offreMapper;

    @Mock
    private OffreService offreServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOffreMockMvc;

    private Offre offre;

    private Offre insertedOffre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offre createEntity(EntityManager em) {
        Offre offre = new Offre()
            .disponibleDe(DEFAULT_DISPONIBLE_DE)
            .disponibleJusqua(DEFAULT_DISPONIBLE_JUSQUA)
            .quantite(DEFAULT_QUANTITE);
        // Add required entity
        Annonce annonce;
        if (TestUtil.findAll(em, Annonce.class).isEmpty()) {
            annonce = AnnonceResourceIT.createEntity();
            em.persist(annonce);
            em.flush();
        } else {
            annonce = TestUtil.findAll(em, Annonce.class).get(0);
        }
        offre.setAnnonce(annonce);
        // Add required entity
        Citoyen citoyen;
        if (TestUtil.findAll(em, Citoyen.class).isEmpty()) {
            citoyen = CitoyenResourceIT.createEntity(em);
            em.persist(citoyen);
            em.flush();
        } else {
            citoyen = TestUtil.findAll(em, Citoyen.class).get(0);
        }
        offre.setCitoyen(citoyen);
        // Add required entity
        Crise crise;
        if (TestUtil.findAll(em, Crise.class).isEmpty()) {
            crise = CriseResourceIT.createEntity(em);
            em.persist(crise);
            em.flush();
        } else {
            crise = TestUtil.findAll(em, Crise.class).get(0);
        }
        offre.setCrise(crise);
        return offre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offre createUpdatedEntity(EntityManager em) {
        Offre updatedOffre = new Offre()
            .disponibleDe(UPDATED_DISPONIBLE_DE)
            .disponibleJusqua(UPDATED_DISPONIBLE_JUSQUA)
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
        updatedOffre.setAnnonce(annonce);
        // Add required entity
        Citoyen citoyen;
        if (TestUtil.findAll(em, Citoyen.class).isEmpty()) {
            citoyen = CitoyenResourceIT.createUpdatedEntity(em);
            em.persist(citoyen);
            em.flush();
        } else {
            citoyen = TestUtil.findAll(em, Citoyen.class).get(0);
        }
        updatedOffre.setCitoyen(citoyen);
        // Add required entity
        Crise crise;
        if (TestUtil.findAll(em, Crise.class).isEmpty()) {
            crise = CriseResourceIT.createUpdatedEntity(em);
            em.persist(crise);
            em.flush();
        } else {
            crise = TestUtil.findAll(em, Crise.class).get(0);
        }
        updatedOffre.setCrise(crise);
        return updatedOffre;
    }

    @BeforeEach
    void initTest() {
        offre = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOffre != null) {
            offreRepository.delete(insertedOffre);
            insertedOffre = null;
        }
    }

    @Test
    @Transactional
    void createOffre() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);
        var returnedOffreDTO = om.readValue(
            restOffreMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OffreDTO.class
        );

        // Validate the Offre in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOffre = offreMapper.toEntity(returnedOffreDTO);
        assertOffreUpdatableFieldsEquals(returnedOffre, getPersistedOffre(returnedOffre));

        insertedOffre = returnedOffre;
    }

    @Test
    @Transactional
    void createOffreWithExistingId() throws Exception {
        // Create the Offre with an existing ID
        offre.setId(1L);
        OffreDTO offreDTO = offreMapper.toDto(offre);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOffreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOffres() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        // Get all the offreList
        restOffreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offre.getId().intValue())))
            .andExpect(jsonPath("$.[*].disponibleDe").value(hasItem(DEFAULT_DISPONIBLE_DE.toString())))
            .andExpect(jsonPath("$.[*].disponibleJusqua").value(hasItem(DEFAULT_DISPONIBLE_JUSQUA.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOffresWithEagerRelationshipsIsEnabled() throws Exception {
        when(offreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOffreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(offreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOffresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(offreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOffreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(offreRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOffre() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        // Get the offre
        restOffreMockMvc
            .perform(get(ENTITY_API_URL_ID, offre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offre.getId().intValue()))
            .andExpect(jsonPath("$.disponibleDe").value(DEFAULT_DISPONIBLE_DE.toString()))
            .andExpect(jsonPath("$.disponibleJusqua").value(DEFAULT_DISPONIBLE_JUSQUA.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE));
    }

    @Test
    @Transactional
    void getNonExistingOffre() throws Exception {
        // Get the offre
        restOffreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOffre() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offre
        Offre updatedOffre = offreRepository.findById(offre.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOffre are not directly saved in db
        em.detach(updatedOffre);
        updatedOffre.disponibleDe(UPDATED_DISPONIBLE_DE).disponibleJusqua(UPDATED_DISPONIBLE_JUSQUA).quantite(UPDATED_QUANTITE);
        OffreDTO offreDTO = offreMapper.toDto(updatedOffre);

        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offreDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreDTO))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOffreToMatchAllProperties(updatedOffre);
    }

    @Test
    @Transactional
    void putNonExistingOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offreDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(offreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(offreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOffreWithPatch() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offre using partial update
        Offre partialUpdatedOffre = new Offre();
        partialUpdatedOffre.setId(offre.getId());

        partialUpdatedOffre.disponibleDe(UPDATED_DISPONIBLE_DE).disponibleJusqua(UPDATED_DISPONIBLE_JUSQUA).quantite(UPDATED_QUANTITE);

        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffre.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOffre))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOffreUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOffre, offre), getPersistedOffre(offre));
    }

    @Test
    @Transactional
    void fullUpdateOffreWithPatch() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the offre using partial update
        Offre partialUpdatedOffre = new Offre();
        partialUpdatedOffre.setId(offre.getId());

        partialUpdatedOffre.disponibleDe(UPDATED_DISPONIBLE_DE).disponibleJusqua(UPDATED_DISPONIBLE_JUSQUA).quantite(UPDATED_QUANTITE);

        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffre.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOffre))
            )
            .andExpect(status().isOk());

        // Validate the Offre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOffreUpdatableFieldsEquals(partialUpdatedOffre, getPersistedOffre(partialUpdatedOffre));
    }

    @Test
    @Transactional
    void patchNonExistingOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offreDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(offreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(offreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOffre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        offre.setId(longCount.incrementAndGet());

        // Create the Offre
        OffreDTO offreDTO = offreMapper.toDto(offre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(offreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOffre() throws Exception {
        // Initialize the database
        insertedOffre = offreRepository.saveAndFlush(offre);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the offre
        restOffreMockMvc
            .perform(delete(ENTITY_API_URL_ID, offre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return offreRepository.count();
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

    protected Offre getPersistedOffre(Offre offre) {
        return offreRepository.findById(offre.getId()).orElseThrow();
    }

    protected void assertPersistedOffreToMatchAllProperties(Offre expectedOffre) {
        assertOffreAllPropertiesEquals(expectedOffre, getPersistedOffre(expectedOffre));
    }

    protected void assertPersistedOffreToMatchUpdatableProperties(Offre expectedOffre) {
        assertOffreAllUpdatablePropertiesEquals(expectedOffre, getPersistedOffre(expectedOffre));
    }
}
