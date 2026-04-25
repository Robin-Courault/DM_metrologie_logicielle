package com.robin_courault.assista_crise.web.rest;

import com.robin_courault.assista_crise.repository.CitoyenRepository;
import com.robin_courault.assista_crise.service.CitoyenService;
import com.robin_courault.assista_crise.service.dto.CitoyenDTO;
import com.robin_courault.assista_crise.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.robin_courault.assista_crise.domain.Citoyen}.
 */
@RestController
@RequestMapping("/api/citoyens")
public class CitoyenResource {

    private static final Logger LOG = LoggerFactory.getLogger(CitoyenResource.class);

    private static final String ENTITY_NAME = "citoyen";

    @Value("${jhipster.clientApp.name:assistacrise}")
    private String applicationName;

    private final CitoyenService citoyenService;

    private final CitoyenRepository citoyenRepository;

    public CitoyenResource(CitoyenService citoyenService, CitoyenRepository citoyenRepository) {
        this.citoyenService = citoyenService;
        this.citoyenRepository = citoyenRepository;
    }

    /**
     * {@code POST  /citoyens} : Create a new citoyen.
     *
     * @param citoyenDTO the citoyenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new citoyenDTO, or with status {@code 400 (Bad Request)} if the citoyen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CitoyenDTO> createCitoyen(@Valid @RequestBody CitoyenDTO citoyenDTO) throws URISyntaxException {
        LOG.debug("REST request to save Citoyen : {}", citoyenDTO);
        if (citoyenDTO.getId() != null) {
            throw new BadRequestAlertException("A new citoyen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        citoyenDTO = citoyenService.save(citoyenDTO);
        return ResponseEntity.created(new URI("/api/citoyens/" + citoyenDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, citoyenDTO.getId().toString()))
            .body(citoyenDTO);
    }

    /**
     * {@code PUT  /citoyens/:id} : Updates an existing citoyen.
     *
     * @param id the id of the citoyenDTO to save.
     * @param citoyenDTO the citoyenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citoyenDTO,
     * or with status {@code 400 (Bad Request)} if the citoyenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the citoyenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CitoyenDTO> updateCitoyen(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CitoyenDTO citoyenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Citoyen : {}, {}", id, citoyenDTO);
        if (citoyenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citoyenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citoyenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        citoyenDTO = citoyenService.update(citoyenDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citoyenDTO.getId().toString()))
            .body(citoyenDTO);
    }

    /**
     * {@code PATCH  /citoyens/:id} : Partial updates given fields of an existing citoyen, field will ignore if it is null
     *
     * @param id the id of the citoyenDTO to save.
     * @param citoyenDTO the citoyenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citoyenDTO,
     * or with status {@code 400 (Bad Request)} if the citoyenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the citoyenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the citoyenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CitoyenDTO> partialUpdateCitoyen(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CitoyenDTO citoyenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Citoyen partially : {}, {}", id, citoyenDTO);
        if (citoyenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citoyenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citoyenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CitoyenDTO> result = citoyenService.partialUpdate(citoyenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citoyenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /citoyens} : get all the Citoyens.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Citoyens in body.
     */
    @GetMapping("")
    public List<CitoyenDTO> getAllCitoyens(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Citoyens");
        return citoyenService.findAll();
    }

    /**
     * {@code GET  /citoyens/:id} : get the "id" citoyen.
     *
     * @param id the id of the citoyenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the citoyenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CitoyenDTO> getCitoyen(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Citoyen : {}", id);
        Optional<CitoyenDTO> citoyenDTO = citoyenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(citoyenDTO);
    }

    /**
     * {@code DELETE  /citoyens/:id} : delete the "id" citoyen.
     *
     * @param id the id of the citoyenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCitoyen(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Citoyen : {}", id);
        citoyenService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
