package com.robin_courault.assista_crise.web.rest;

import com.robin_courault.assista_crise.repository.AdministrateurRepository;
import com.robin_courault.assista_crise.service.AdministrateurService;
import com.robin_courault.assista_crise.service.dto.AdministrateurDTO;
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
 * REST controller for managing {@link com.robin_courault.assista_crise.domain.Administrateur}.
 */
@RestController
@RequestMapping("/api/administrateurs")
public class AdministrateurResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdministrateurResource.class);

    private static final String ENTITY_NAME = "administrateur";

    @Value("${jhipster.clientApp.name:assistacrise}")
    private String applicationName;

    private final AdministrateurService administrateurService;

    private final AdministrateurRepository administrateurRepository;

    public AdministrateurResource(AdministrateurService administrateurService, AdministrateurRepository administrateurRepository) {
        this.administrateurService = administrateurService;
        this.administrateurRepository = administrateurRepository;
    }

    /**
     * {@code POST  /administrateurs} : Create a new administrateur.
     *
     * @param administrateurDTO the administrateurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new administrateurDTO, or with status {@code 400 (Bad Request)} if the administrateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdministrateurDTO> createAdministrateur(@Valid @RequestBody AdministrateurDTO administrateurDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Administrateur : {}", administrateurDTO);
        if (administrateurDTO.getId() != null) {
            throw new BadRequestAlertException("A new administrateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        administrateurDTO = administrateurService.save(administrateurDTO);
        return ResponseEntity.created(new URI("/api/administrateurs/" + administrateurDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, administrateurDTO.getId().toString()))
            .body(administrateurDTO);
    }

    /**
     * {@code PUT  /administrateurs/:id} : Updates an existing administrateur.
     *
     * @param id the id of the administrateurDTO to save.
     * @param administrateurDTO the administrateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administrateurDTO,
     * or with status {@code 400 (Bad Request)} if the administrateurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the administrateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdministrateurDTO> updateAdministrateur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdministrateurDTO administrateurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Administrateur : {}, {}", id, administrateurDTO);
        if (administrateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administrateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administrateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        administrateurDTO = administrateurService.update(administrateurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administrateurDTO.getId().toString()))
            .body(administrateurDTO);
    }

    /**
     * {@code PATCH  /administrateurs/:id} : Partial updates given fields of an existing administrateur, field will ignore if it is null
     *
     * @param id the id of the administrateurDTO to save.
     * @param administrateurDTO the administrateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administrateurDTO,
     * or with status {@code 400 (Bad Request)} if the administrateurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the administrateurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the administrateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdministrateurDTO> partialUpdateAdministrateur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdministrateurDTO administrateurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Administrateur partially : {}, {}", id, administrateurDTO);
        if (administrateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administrateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administrateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdministrateurDTO> result = administrateurService.partialUpdate(administrateurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administrateurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /administrateurs} : get all the Administrateurs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Administrateurs in body.
     */
    @GetMapping("")
    public List<AdministrateurDTO> getAllAdministrateurs(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Administrateurs");
        return administrateurService.findAll();
    }

    /**
     * {@code GET  /administrateurs/:id} : get the "id" administrateur.
     *
     * @param id the id of the administrateurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the administrateurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdministrateurDTO> getAdministrateur(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Administrateur : {}", id);
        Optional<AdministrateurDTO> administrateurDTO = administrateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(administrateurDTO);
    }

    /**
     * {@code DELETE  /administrateurs/:id} : delete the "id" administrateur.
     *
     * @param id the id of the administrateurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrateur(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Administrateur : {}", id);
        administrateurService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
