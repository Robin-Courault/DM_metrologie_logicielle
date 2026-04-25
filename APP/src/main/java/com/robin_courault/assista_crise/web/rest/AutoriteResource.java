package com.robin_courault.assista_crise.web.rest;

import com.robin_courault.assista_crise.repository.AutoriteRepository;
import com.robin_courault.assista_crise.service.AutoriteService;
import com.robin_courault.assista_crise.service.dto.AutoriteDTO;
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
 * REST controller for managing {@link com.robin_courault.assista_crise.domain.Autorite}.
 */
@RestController
@RequestMapping("/api/autorites")
public class AutoriteResource {

    private static final Logger LOG = LoggerFactory.getLogger(AutoriteResource.class);

    private static final String ENTITY_NAME = "autorite";

    @Value("${jhipster.clientApp.name:assistacrise}")
    private String applicationName;

    private final AutoriteService autoriteService;

    private final AutoriteRepository autoriteRepository;

    public AutoriteResource(AutoriteService autoriteService, AutoriteRepository autoriteRepository) {
        this.autoriteService = autoriteService;
        this.autoriteRepository = autoriteRepository;
    }

    /**
     * {@code POST  /autorites} : Create a new autorite.
     *
     * @param autoriteDTO the autoriteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autoriteDTO, or with status {@code 400 (Bad Request)} if the autorite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AutoriteDTO> createAutorite(@Valid @RequestBody AutoriteDTO autoriteDTO) throws URISyntaxException {
        LOG.debug("REST request to save Autorite : {}", autoriteDTO);
        if (autoriteDTO.getId() != null) {
            throw new BadRequestAlertException("A new autorite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        autoriteDTO = autoriteService.save(autoriteDTO);
        return ResponseEntity.created(new URI("/api/autorites/" + autoriteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, autoriteDTO.getId().toString()))
            .body(autoriteDTO);
    }

    /**
     * {@code PUT  /autorites/:id} : Updates an existing autorite.
     *
     * @param id the id of the autoriteDTO to save.
     * @param autoriteDTO the autoriteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autoriteDTO,
     * or with status {@code 400 (Bad Request)} if the autoriteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autoriteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AutoriteDTO> updateAutorite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AutoriteDTO autoriteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Autorite : {}, {}", id, autoriteDTO);
        if (autoriteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autoriteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autoriteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        autoriteDTO = autoriteService.update(autoriteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autoriteDTO.getId().toString()))
            .body(autoriteDTO);
    }

    /**
     * {@code PATCH  /autorites/:id} : Partial updates given fields of an existing autorite, field will ignore if it is null
     *
     * @param id the id of the autoriteDTO to save.
     * @param autoriteDTO the autoriteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autoriteDTO,
     * or with status {@code 400 (Bad Request)} if the autoriteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the autoriteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the autoriteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AutoriteDTO> partialUpdateAutorite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AutoriteDTO autoriteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Autorite partially : {}, {}", id, autoriteDTO);
        if (autoriteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autoriteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autoriteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AutoriteDTO> result = autoriteService.partialUpdate(autoriteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autoriteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /autorites} : get all the Autorites.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Autorites in body.
     */
    @GetMapping("")
    public List<AutoriteDTO> getAllAutorites() {
        LOG.debug("REST request to get all Autorites");
        return autoriteService.findAll();
    }

    /**
     * {@code GET  /autorites/:id} : get the "id" autorite.
     *
     * @param id the id of the autoriteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autoriteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AutoriteDTO> getAutorite(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Autorite : {}", id);
        Optional<AutoriteDTO> autoriteDTO = autoriteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autoriteDTO);
    }

    /**
     * {@code DELETE  /autorites/:id} : delete the "id" autorite.
     *
     * @param id the id of the autoriteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutorite(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Autorite : {}", id);
        autoriteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
