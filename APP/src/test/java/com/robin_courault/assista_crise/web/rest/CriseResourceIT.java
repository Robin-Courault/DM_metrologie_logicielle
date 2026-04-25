package com.robin_courault.assista_crise.web.rest;

import static com.robin_courault.assista_crise.domain.CriseAsserts.*;
import static com.robin_courault.assista_crise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin_courault.assista_crise.IntegrationTest;
import com.robin_courault.assista_crise.domain.Autorite;
import com.robin_courault.assista_crise.domain.Crise;
import com.robin_courault.assista_crise.domain.enumeration.TypeCrise;
import com.robin_courault.assista_crise.repository.CriseRepository;
import com.robin_courault.assista_crise.service.dto.CriseDTO;
import com.robin_courault.assista_crise.service.mapper.CriseMapper;
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
 * Integration tests for the {@link CriseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CriseResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TypeCrise DEFAULT_TYPE = TypeCrise.INCENDIE;
    private static final TypeCrise UPDATED_TYPE = TypeCrise.INONDATION;

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ZONE_GEOGRAPHIQUE = "AAAAAAAAAA";
    private static final String UPDATED_ZONE_GEOGRAPHIQUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CLOTUREE = false;
    private static final Boolean UPDATED_CLOTUREE = true;

    private static final String ENTITY_API_URL = "/api/crises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CriseRepository criseRepository;

    @Autowired
    private CriseMapper criseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCriseMockMvc;

    private Crise crise;

    private Crise insertedCrise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crise createEntity(EntityManager em) {
        Crise crise = new Crise()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .zoneGeographique(DEFAULT_ZONE_GEOGRAPHIQUE)
            .cloturee(DEFAULT_CLOTUREE);
        // Add required entity
        Autorite autorite;
        if (TestUtil.findAll(em, Autorite.class).isEmpty()) {
            autorite = AutoriteResourceIT.createEntity();
            em.persist(autorite);
            em.flush();
        } else {
            autorite = TestUtil.findAll(em, Autorite.class).get(0);
        }
        crise.setAutorite(autorite);
        return crise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crise createUpdatedEntity(EntityManager em) {
        Crise updatedCrise = new Crise()
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .zoneGeographique(UPDATED_ZONE_GEOGRAPHIQUE)
            .cloturee(UPDATED_CLOTUREE);
        // Add required entity
        Autorite autorite;
        if (TestUtil.findAll(em, Autorite.class).isEmpty()) {
            autorite = AutoriteResourceIT.createUpdatedEntity();
            em.persist(autorite);
            em.flush();
        } else {
            autorite = TestUtil.findAll(em, Autorite.class).get(0);
        }
        updatedCrise.setAutorite(autorite);
        return updatedCrise;
    }

    @BeforeEach
    void initTest() {
        crise = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCrise != null) {
            criseRepository.delete(insertedCrise);
            insertedCrise = null;
        }
    }

    @Test
    @Transactional
    void createCrise() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Crise
        CriseDTO criseDTO = criseMapper.toDto(crise);
        var returnedCriseDTO = om.readValue(
            restCriseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CriseDTO.class
        );

        // Validate the Crise in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCrise = criseMapper.toEntity(returnedCriseDTO);
        assertCriseUpdatableFieldsEquals(returnedCrise, getPersistedCrise(returnedCrise));

        insertedCrise = returnedCrise;
    }

    @Test
    @Transactional
    void createCriseWithExistingId() throws Exception {
        // Create the Crise with an existing ID
        crise.setId(1L);
        CriseDTO criseDTO = criseMapper.toDto(crise);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Crise in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        crise.setTitre(null);

        // Create the Crise, which fails.
        CriseDTO criseDTO = criseMapper.toDto(crise);

        restCriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        crise.setType(null);

        // Create the Crise, which fails.
        CriseDTO criseDTO = criseMapper.toDto(crise);

        restCriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        crise.setDateDebut(null);

        // Create the Crise, which fails.
        CriseDTO criseDTO = criseMapper.toDto(crise);

        restCriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrises() throws Exception {
        // Initialize the database
        insertedCrise = criseRepository.saveAndFlush(crise);

        // Get all the criseList
        restCriseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crise.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].zoneGeographique").value(hasItem(DEFAULT_ZONE_GEOGRAPHIQUE)))
            .andExpect(jsonPath("$.[*].cloturee").value(hasItem(DEFAULT_CLOTUREE)));
    }

    @Test
    @Transactional
    void getCrise() throws Exception {
        // Initialize the database
        insertedCrise = criseRepository.saveAndFlush(crise);

        // Get the crise
        restCriseMockMvc
            .perform(get(ENTITY_API_URL_ID, crise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crise.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.zoneGeographique").value(DEFAULT_ZONE_GEOGRAPHIQUE))
            .andExpect(jsonPath("$.cloturee").value(DEFAULT_CLOTUREE));
    }

    @Test
    @Transactional
    void getNonExistingCrise() throws Exception {
        // Get the crise
        restCriseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCrise() throws Exception {
        // Initialize the database
        insertedCrise = criseRepository.saveAndFlush(crise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the crise
        Crise updatedCrise = criseRepository.findById(crise.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCrise are not directly saved in db
        em.detach(updatedCrise);
        updatedCrise
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .zoneGeographique(UPDATED_ZONE_GEOGRAPHIQUE)
            .cloturee(UPDATED_CLOTUREE);
        CriseDTO criseDTO = criseMapper.toDto(updatedCrise);

        restCriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, criseDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Crise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCriseToMatchAllProperties(updatedCrise);
    }

    @Test
    @Transactional
    void putNonExistingCrise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crise.setId(longCount.incrementAndGet());

        // Create the Crise
        CriseDTO criseDTO = criseMapper.toDto(crise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, criseDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crise.setId(longCount.incrementAndGet());

        // Create the Crise
        CriseDTO criseDTO = criseMapper.toDto(crise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(criseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crise.setId(longCount.incrementAndGet());

        // Create the Crise
        CriseDTO criseDTO = criseMapper.toDto(crise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(criseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Crise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCriseWithPatch() throws Exception {
        // Initialize the database
        insertedCrise = criseRepository.saveAndFlush(crise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the crise using partial update
        Crise partialUpdatedCrise = new Crise();
        partialUpdatedCrise.setId(crise.getId());

        partialUpdatedCrise.titre(UPDATED_TITRE).cloturee(UPDATED_CLOTUREE);

        restCriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCrise))
            )
            .andExpect(status().isOk());

        // Validate the Crise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCriseUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCrise, crise), getPersistedCrise(crise));
    }

    @Test
    @Transactional
    void fullUpdateCriseWithPatch() throws Exception {
        // Initialize the database
        insertedCrise = criseRepository.saveAndFlush(crise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the crise using partial update
        Crise partialUpdatedCrise = new Crise();
        partialUpdatedCrise.setId(crise.getId());

        partialUpdatedCrise
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .zoneGeographique(UPDATED_ZONE_GEOGRAPHIQUE)
            .cloturee(UPDATED_CLOTUREE);

        restCriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCrise))
            )
            .andExpect(status().isOk());

        // Validate the Crise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCriseUpdatableFieldsEquals(partialUpdatedCrise, getPersistedCrise(partialUpdatedCrise));
    }

    @Test
    @Transactional
    void patchNonExistingCrise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crise.setId(longCount.incrementAndGet());

        // Create the Crise
        CriseDTO criseDTO = criseMapper.toDto(crise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, criseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(criseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crise.setId(longCount.incrementAndGet());

        // Create the Crise
        CriseDTO criseDTO = criseMapper.toDto(crise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(criseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crise.setId(longCount.incrementAndGet());

        // Create the Crise
        CriseDTO criseDTO = criseMapper.toDto(crise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(criseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Crise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCrise() throws Exception {
        // Initialize the database
        insertedCrise = criseRepository.saveAndFlush(crise);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the crise
        restCriseMockMvc
            .perform(delete(ENTITY_API_URL_ID, crise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return criseRepository.count();
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

    protected Crise getPersistedCrise(Crise crise) {
        return criseRepository.findById(crise.getId()).orElseThrow();
    }

    protected void assertPersistedCriseToMatchAllProperties(Crise expectedCrise) {
        assertCriseAllPropertiesEquals(expectedCrise, getPersistedCrise(expectedCrise));
    }

    protected void assertPersistedCriseToMatchUpdatableProperties(Crise expectedCrise) {
        assertCriseAllUpdatablePropertiesEquals(expectedCrise, getPersistedCrise(expectedCrise));
    }
}
