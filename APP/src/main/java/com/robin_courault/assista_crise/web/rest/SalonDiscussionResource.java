package com.robin_courault.assista_crise.web.rest;

import com.robin_courault.assista_crise.repository.SalonDiscussionRepository;
import com.robin_courault.assista_crise.service.SalonDiscussionService;
import com.robin_courault.assista_crise.service.dto.SalonDiscussionDTO;
import com.robin_courault.assista_crise.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.robin_courault.assista_crise.domain.SalonDiscussion}.
 */
@RestController
@RequestMapping("/api/salon-discussions")
public class SalonDiscussionResource {

    private static final Logger LOG = LoggerFactory.getLogger(SalonDiscussionResource.class);

    private static final String ENTITY_NAME = "salonDiscussion";

    @Value("${jhipster.clientApp.name:assistacrise}")
    private String applicationName;

    private final SalonDiscussionService salonDiscussionService;

    private final SalonDiscussionRepository salonDiscussionRepository;

    public SalonDiscussionResource(SalonDiscussionService salonDiscussionService, SalonDiscussionRepository salonDiscussionRepository) {
        this.salonDiscussionService = salonDiscussionService;
        this.salonDiscussionRepository = salonDiscussionRepository;
    }

    /**
     * {@code POST  /salon-discussions} : Create a new salonDiscussion.
     *
     * @param salonDiscussionDTO the salonDiscussionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salonDiscussionDTO, or with status {@code 400 (Bad Request)} if the salonDiscussion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SalonDiscussionDTO> createSalonDiscussion(@RequestBody SalonDiscussionDTO salonDiscussionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SalonDiscussion : {}", salonDiscussionDTO);
        if (salonDiscussionDTO.getId() != null) {
            throw new BadRequestAlertException("A new salonDiscussion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        salonDiscussionDTO = salonDiscussionService.save(salonDiscussionDTO);
        return ResponseEntity.created(new URI("/api/salon-discussions/" + salonDiscussionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, salonDiscussionDTO.getId().toString()))
            .body(salonDiscussionDTO);
    }

    /**
     * {@code PUT  /salon-discussions/:id} : Updates an existing salonDiscussion.
     *
     * @param id the id of the salonDiscussionDTO to save.
     * @param salonDiscussionDTO the salonDiscussionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salonDiscussionDTO,
     * or with status {@code 400 (Bad Request)} if the salonDiscussionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salonDiscussionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SalonDiscussionDTO> updateSalonDiscussion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SalonDiscussionDTO salonDiscussionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SalonDiscussion : {}, {}", id, salonDiscussionDTO);
        if (salonDiscussionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salonDiscussionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salonDiscussionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        salonDiscussionDTO = salonDiscussionService.update(salonDiscussionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salonDiscussionDTO.getId().toString()))
            .body(salonDiscussionDTO);
    }

    /**
     * {@code PATCH  /salon-discussions/:id} : Partial updates given fields of an existing salonDiscussion, field will ignore if it is null
     *
     * @param id the id of the salonDiscussionDTO to save.
     * @param salonDiscussionDTO the salonDiscussionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salonDiscussionDTO,
     * or with status {@code 400 (Bad Request)} if the salonDiscussionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the salonDiscussionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the salonDiscussionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SalonDiscussionDTO> partialUpdateSalonDiscussion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SalonDiscussionDTO salonDiscussionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SalonDiscussion partially : {}, {}", id, salonDiscussionDTO);
        if (salonDiscussionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salonDiscussionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salonDiscussionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SalonDiscussionDTO> result = salonDiscussionService.partialUpdate(salonDiscussionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salonDiscussionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /salon-discussions} : get all the Salon Discussions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Salon Discussions in body.
     */
    @GetMapping("")
    public List<SalonDiscussionDTO> getAllSalonDiscussions(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("demande-is-null".equals(filter)) {
            LOG.debug("REST request to get all SalonDiscussions where demande is null");
            return salonDiscussionService.findAllWhereDemandeIsNull();
        }
        LOG.debug("REST request to get all SalonDiscussions");
        return salonDiscussionService.findAll();
    }

    /**
     * {@code GET  /salon-discussions/:id} : get the "id" salonDiscussion.
     *
     * @param id the id of the salonDiscussionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salonDiscussionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalonDiscussionDTO> getSalonDiscussion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SalonDiscussion : {}", id);
        Optional<SalonDiscussionDTO> salonDiscussionDTO = salonDiscussionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salonDiscussionDTO);
    }

    /**
     * {@code DELETE  /salon-discussions/:id} : delete the "id" salonDiscussion.
     *
     * @param id the id of the salonDiscussionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalonDiscussion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SalonDiscussion : {}", id);
        salonDiscussionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
