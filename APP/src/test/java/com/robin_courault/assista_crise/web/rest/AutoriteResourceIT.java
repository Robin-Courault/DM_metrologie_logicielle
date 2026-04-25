package com.robin_courault.assista_crise.web.rest;

import static com.robin_courault.assista_crise.domain.AutoriteAsserts.*;
import static com.robin_courault.assista_crise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robin_courault.assista_crise.IntegrationTest;
import com.robin_courault.assista_crise.domain.Autorite;
import com.robin_courault.assista_crise.domain.enumeration.TypeAutorite;
import com.robin_courault.assista_crise.repository.AutoriteRepository;
import com.robin_courault.assista_crise.service.dto.AutoriteDTO;
import com.robin_courault.assista_crise.service.mapper.AutoriteMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AutoriteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AutoriteResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final TypeAutorite DEFAULT_TYPE = TypeAutorite.MAIRE;
    private static final TypeAutorite UPDATED_TYPE = TypeAutorite.PRESIDENT_METROPOLE;

    private static final String DEFAULT_TERRITOIRE = "AAAAAAAAAA";
    private static final String UPDATED_TERRITOIRE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/autorites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AutoriteRepository autoriteRepository;

    @Autowired
    private AutoriteMapper autoriteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAutoriteMockMvc;

    private Autorite autorite;

    private Autorite insertedAutorite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autorite createEntity() {
        return new Autorite().nom(DEFAULT_NOM).type(DEFAULT_TYPE).territoire(DEFAULT_TERRITOIRE).contact(DEFAULT_CONTACT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Autorite createUpdatedEntity() {
        return new Autorite().nom(UPDATED_NOM).type(UPDATED_TYPE).territoire(UPDATED_TERRITOIRE).contact(UPDATED_CONTACT);
    }

    @BeforeEach
    void initTest() {
        autorite = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAutorite != null) {
            autoriteRepository.delete(insertedAutorite);
            insertedAutorite = null;
        }
    }

    @Test
    @Transactional
    void createAutorite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Autorite
        AutoriteDTO autoriteDTO = autoriteMapper.toDto(autorite);
        var returnedAutoriteDTO = om.readValue(
            restAutoriteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoriteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AutoriteDTO.class
        );

        // Validate the Autorite in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAutorite = autoriteMapper.toEntity(returnedAutoriteDTO);
        assertAutoriteUpdatableFieldsEquals(returnedAutorite, getPersistedAutorite(returnedAutorite));

        insertedAutorite = returnedAutorite;
    }

    @Test
    @Transactional
    void createAutoriteWithExistingId() throws Exception {
        // Create the Autorite with an existing ID
        autorite.setId(1L);
        AutoriteDTO autoriteDTO = autoriteMapper.toDto(autorite);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutoriteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoriteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Autorite in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        autorite.setNom(null);

        // Create the Autorite, which fails.
        AutoriteDTO autoriteDTO = autoriteMapper.toDto(autorite);

        restAutoriteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoriteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        autorite.setType(null);

        // Create the Autorite, which fails.
        AutoriteDTO autoriteDTO = autoriteMapper.toDto(autorite);

        restAutoriteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoriteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAutorites() throws Exception {
        // Initialize the database
        insertedAutorite = autoriteRepository.saveAndFlush(autorite);

        // Get all the autoriteList
        restAutoriteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autorite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].territoire").value(hasItem(DEFAULT_TERRITOIRE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));
    }

    @Test
    @Transactional
    void getAutorite() throws Exception {
        // Initialize the database
        insertedAutorite = autoriteRepository.saveAndFlush(autorite);

        // Get the autorite
        restAutoriteMockMvc
            .perform(get(ENTITY_API_URL_ID, autorite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(autorite.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.territoire").value(DEFAULT_TERRITOIRE))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT));
    }

    @Test
    @Transactional
    void getNonExistingAutorite() throws Exception {
        // Get the autorite
        restAutoriteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAutorite() throws Exception {
        // Initialize the database
        insertedAutorite = autoriteRepository.saveAndFlush(autorite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autorite
        Autorite updatedAutorite = autoriteRepository.findById(autorite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAutorite are not directly saved in db
        em.detach(updatedAutorite);
        updatedAutorite.nom(UPDATED_NOM).type(UPDATED_TYPE).territoire(UPDATED_TERRITOIRE).contact(UPDATED_CONTACT);
        AutoriteDTO autoriteDTO = autoriteMapper.toDto(updatedAutorite);

        restAutoriteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autoriteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(autoriteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Autorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAutoriteToMatchAllProperties(updatedAutorite);
    }

    @Test
    @Transactional
    void putNonExistingAutorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorite.setId(longCount.incrementAndGet());

        // Create the Autorite
        AutoriteDTO autoriteDTO = autoriteMapper.toDto(autorite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoriteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autoriteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(autoriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAutorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorite.setId(longCount.incrementAndGet());

        // Create the Autorite
        AutoriteDTO autoriteDTO = autoriteMapper.toDto(autorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoriteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(autoriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAutorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorite.setId(longCount.incrementAndGet());

        // Create the Autorite
        AutoriteDTO autoriteDTO = autoriteMapper.toDto(autorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoriteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoriteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Autorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAutoriteWithPatch() throws Exception {
        // Initialize the database
        insertedAutorite = autoriteRepository.saveAndFlush(autorite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autorite using partial update
        Autorite partialUpdatedAutorite = new Autorite();
        partialUpdatedAutorite.setId(autorite.getId());

        partialUpdatedAutorite.type(UPDATED_TYPE).territoire(UPDATED_TERRITOIRE).contact(UPDATED_CONTACT);

        restAutoriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutorite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAutorite))
            )
            .andExpect(status().isOk());

        // Validate the Autorite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAutoriteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAutorite, autorite), getPersistedAutorite(autorite));
    }

    @Test
    @Transactional
    void fullUpdateAutoriteWithPatch() throws Exception {
        // Initialize the database
        insertedAutorite = autoriteRepository.saveAndFlush(autorite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autorite using partial update
        Autorite partialUpdatedAutorite = new Autorite();
        partialUpdatedAutorite.setId(autorite.getId());

        partialUpdatedAutorite.nom(UPDATED_NOM).type(UPDATED_TYPE).territoire(UPDATED_TERRITOIRE).contact(UPDATED_CONTACT);

        restAutoriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutorite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAutorite))
            )
            .andExpect(status().isOk());

        // Validate the Autorite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAutoriteUpdatableFieldsEquals(partialUpdatedAutorite, getPersistedAutorite(partialUpdatedAutorite));
    }

    @Test
    @Transactional
    void patchNonExistingAutorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorite.setId(longCount.incrementAndGet());

        // Create the Autorite
        AutoriteDTO autoriteDTO = autoriteMapper.toDto(autorite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, autoriteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(autoriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAutorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorite.setId(longCount.incrementAndGet());

        // Create the Autorite
        AutoriteDTO autoriteDTO = autoriteMapper.toDto(autorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoriteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(autoriteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Autorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAutorite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autorite.setId(longCount.incrementAndGet());

        // Create the Autorite
        AutoriteDTO autoriteDTO = autoriteMapper.toDto(autorite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoriteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(autoriteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Autorite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAutorite() throws Exception {
        // Initialize the database
        insertedAutorite = autoriteRepository.saveAndFlush(autorite);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the autorite
        restAutoriteMockMvc
            .perform(delete(ENTITY_API_URL_ID, autorite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return autoriteRepository.count();
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

    protected Autorite getPersistedAutorite(Autorite autorite) {
        return autoriteRepository.findById(autorite.getId()).orElseThrow();
    }

    protected void assertPersistedAutoriteToMatchAllProperties(Autorite expectedAutorite) {
        assertAutoriteAllPropertiesEquals(expectedAutorite, getPersistedAutorite(expectedAutorite));
    }

    protected void assertPersistedAutoriteToMatchUpdatableProperties(Autorite expectedAutorite) {
        assertAutoriteAllUpdatablePropertiesEquals(expectedAutorite, getPersistedAutorite(expectedAutorite));
    }
}
