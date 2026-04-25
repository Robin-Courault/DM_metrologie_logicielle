package com.robin_courault.assista_crise.web.rest;

import static com.robin_courault.assista_crise.domain.AnnonceAsserts.*;
import static com.robin_courault.assista_crise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin_courault.assista_crise.IntegrationTest;
import com.robin_courault.assista_crise.domain.Annonce;
import com.robin_courault.assista_crise.domain.enumeration.CategorieBesoin;
import com.robin_courault.assista_crise.domain.enumeration.EtatAnnonce;
import com.robin_courault.assista_crise.repository.AnnonceRepository;
import com.robin_courault.assista_crise.service.dto.AnnonceDTO;
import com.robin_courault.assista_crise.service.mapper.AnnonceMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AnnonceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnnonceResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final CategorieBesoin DEFAULT_CATEGORIE = CategorieBesoin.PRET_MATERIEL;
    private static final CategorieBesoin UPDATED_CATEGORIE = CategorieBesoin.PRET_VEHICULE;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MA_J = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MA_J = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final EtatAnnonce DEFAULT_ETAT = EtatAnnonce.PUBLIEE;
    private static final EtatAnnonce UPDATED_ETAT = EtatAnnonce.MODEREE;

    private static final String ENTITY_API_URL = "/api/annonces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AnnonceRepository annonceRepository;

    @Autowired
    private AnnonceMapper annonceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnnonceMockMvc;

    private Annonce annonce;

    private Annonce insertedAnnonce;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annonce createEntity() {
        return new Annonce()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION)
            .categorie(DEFAULT_CATEGORIE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .adresse(DEFAULT_ADRESSE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateMaJ(DEFAULT_DATE_MA_J)
            .etat(DEFAULT_ETAT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annonce createUpdatedEntity() {
        return new Annonce()
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .categorie(UPDATED_CATEGORIE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .adresse(UPDATED_ADRESSE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateMaJ(UPDATED_DATE_MA_J)
            .etat(UPDATED_ETAT);
    }

    @BeforeEach
    void initTest() {
        annonce = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAnnonce != null) {
            annonceRepository.delete(insertedAnnonce);
            insertedAnnonce = null;
        }
    }

    @Test
    @Transactional
    void createAnnonce() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Annonce
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);
        var returnedAnnonceDTO = om.readValue(
            restAnnonceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annonceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AnnonceDTO.class
        );

        // Validate the Annonce in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAnnonce = annonceMapper.toEntity(returnedAnnonceDTO);
        assertAnnonceUpdatableFieldsEquals(returnedAnnonce, getPersistedAnnonce(returnedAnnonce));

        insertedAnnonce = returnedAnnonce;
    }

    @Test
    @Transactional
    void createAnnonceWithExistingId() throws Exception {
        // Create the Annonce with an existing ID
        annonce.setId(1L);
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnonceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annonceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        annonce.setTitre(null);

        // Create the Annonce, which fails.
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        restAnnonceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annonceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategorieIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        annonce.setCategorie(null);

        // Create the Annonce, which fails.
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        restAnnonceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annonceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAnnonces() throws Exception {
        // Initialize the database
        insertedAnnonce = annonceRepository.saveAndFlush(annonce);

        // Get all the annonceList
        restAnnonceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annonce.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateMaJ").value(hasItem(DEFAULT_DATE_MA_J.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    void getAnnonce() throws Exception {
        // Initialize the database
        insertedAnnonce = annonceRepository.saveAndFlush(annonce);

        // Get the annonce
        restAnnonceMockMvc
            .perform(get(ENTITY_API_URL_ID, annonce.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annonce.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateMaJ").value(DEFAULT_DATE_MA_J.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAnnonce() throws Exception {
        // Get the annonce
        restAnnonceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnnonce() throws Exception {
        // Initialize the database
        insertedAnnonce = annonceRepository.saveAndFlush(annonce);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the annonce
        Annonce updatedAnnonce = annonceRepository.findById(annonce.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnnonce are not directly saved in db
        em.detach(updatedAnnonce);
        updatedAnnonce
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .categorie(UPDATED_CATEGORIE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .adresse(UPDATED_ADRESSE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateMaJ(UPDATED_DATE_MA_J)
            .etat(UPDATED_ETAT);
        AnnonceDTO annonceDTO = annonceMapper.toDto(updatedAnnonce);

        restAnnonceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, annonceDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annonceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Annonce in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAnnonceToMatchAllProperties(updatedAnnonce);
    }

    @Test
    @Transactional
    void putNonExistingAnnonce() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annonce.setId(longCount.incrementAndGet());

        // Create the Annonce
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, annonceDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annonceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnnonce() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annonce.setId(longCount.incrementAndGet());

        // Create the Annonce
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(annonceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnnonce() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annonce.setId(longCount.incrementAndGet());

        // Create the Annonce
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annonceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Annonce in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnnonceWithPatch() throws Exception {
        // Initialize the database
        insertedAnnonce = annonceRepository.saveAndFlush(annonce);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the annonce using partial update
        Annonce partialUpdatedAnnonce = new Annonce();
        partialUpdatedAnnonce.setId(annonce.getId());

        partialUpdatedAnnonce.titre(UPDATED_TITRE).longitude(UPDATED_LONGITUDE).etat(UPDATED_ETAT);

        restAnnonceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnonce.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnnonce))
            )
            .andExpect(status().isOk());

        // Validate the Annonce in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnnonceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAnnonce, annonce), getPersistedAnnonce(annonce));
    }

    @Test
    @Transactional
    void fullUpdateAnnonceWithPatch() throws Exception {
        // Initialize the database
        insertedAnnonce = annonceRepository.saveAndFlush(annonce);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the annonce using partial update
        Annonce partialUpdatedAnnonce = new Annonce();
        partialUpdatedAnnonce.setId(annonce.getId());

        partialUpdatedAnnonce
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .categorie(UPDATED_CATEGORIE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .adresse(UPDATED_ADRESSE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateMaJ(UPDATED_DATE_MA_J)
            .etat(UPDATED_ETAT);

        restAnnonceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnonce.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnnonce))
            )
            .andExpect(status().isOk());

        // Validate the Annonce in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnnonceUpdatableFieldsEquals(partialUpdatedAnnonce, getPersistedAnnonce(partialUpdatedAnnonce));
    }

    @Test
    @Transactional
    void patchNonExistingAnnonce() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annonce.setId(longCount.incrementAndGet());

        // Create the Annonce
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, annonceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(annonceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnnonce() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annonce.setId(longCount.incrementAndGet());

        // Create the Annonce
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(annonceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnnonce() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annonce.setId(longCount.incrementAndGet());

        // Create the Annonce
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnonceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(annonceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Annonce in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnnonce() throws Exception {
        // Initialize the database
        insertedAnnonce = annonceRepository.saveAndFlush(annonce);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the annonce
        restAnnonceMockMvc
            .perform(delete(ENTITY_API_URL_ID, annonce.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return annonceRepository.count();
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

    protected Annonce getPersistedAnnonce(Annonce annonce) {
        return annonceRepository.findById(annonce.getId()).orElseThrow();
    }

    protected void assertPersistedAnnonceToMatchAllProperties(Annonce expectedAnnonce) {
        assertAnnonceAllPropertiesEquals(expectedAnnonce, getPersistedAnnonce(expectedAnnonce));
    }

    protected void assertPersistedAnnonceToMatchUpdatableProperties(Annonce expectedAnnonce) {
        assertAnnonceAllUpdatablePropertiesEquals(expectedAnnonce, getPersistedAnnonce(expectedAnnonce));
    }
}
