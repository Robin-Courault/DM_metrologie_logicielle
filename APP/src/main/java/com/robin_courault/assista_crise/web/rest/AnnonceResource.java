package com.robin_courault.assista_crise.web.rest;

import com.robin_courault.assista_crise.repository.AnnonceRepository;
import com.robin_courault.assista_crise.service.AnnonceService;
import com.robin_courault.assista_crise.service.dto.AnnonceDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.robin_courault.assista_crise.domain.Annonce}.
 */
@RestController
@RequestMapping("/api/annonces")
public class AnnonceResource {

    private static final Logger LOG = LoggerFactory.getLogger(AnnonceResource.class);

    private static final String ENTITY_NAME = "annonce";

    @Value("${jhipster.clientApp.name:assistacrise}")
    private String applicationName;

    private final AnnonceService annonceService;

    private final AnnonceRepository annonceRepository;

    public AnnonceResource(AnnonceService annonceService, AnnonceRepository annonceRepository) {
        this.annonceService = annonceService;
        this.annonceRepository = annonceRepository;
    }

    /**
     * {@code POST  /annonces} : Create a new annonce.
     *
     * @param annonceDTO the annonceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annonceDTO, or with status {@code 400 (Bad Request)} if the annonce has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AnnonceDTO> createAnnonce(@Valid @RequestBody AnnonceDTO annonceDTO) throws URISyntaxException {
        LOG.debug("REST request to save Annonce : {}", annonceDTO);
        if (annonceDTO.getId() != null) {
            throw new BadRequestAlertException("A new annonce cannot already have an ID", ENTITY_NAME, "idexists");
        }
        annonceDTO = annonceService.save(annonceDTO);
        return ResponseEntity.created(new URI("/api/annonces/" + annonceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, annonceDTO.getId().toString()))
            .body(annonceDTO);
    }

    /**
     * {@code PUT  /annonces/:id} : Updates an existing annonce.
     *
     * @param id the id of the annonceDTO to save.
     * @param annonceDTO the annonceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annonceDTO,
     * or with status {@code 400 (Bad Request)} if the annonceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annonceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnnonceDTO> updateAnnonce(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnnonceDTO annonceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Annonce : {}, {}", id, annonceDTO);
        if (annonceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annonceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annonceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        annonceDTO = annonceService.update(annonceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annonceDTO.getId().toString()))
            .body(annonceDTO);
    }

    /**
     * {@code PATCH  /annonces/:id} : Partial updates given fields of an existing annonce, field will ignore if it is null
     *
     * @param id the id of the annonceDTO to save.
     * @param annonceDTO the annonceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annonceDTO,
     * or with status {@code 400 (Bad Request)} if the annonceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the annonceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the annonceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AnnonceDTO> partialUpdateAnnonce(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnnonceDTO annonceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Annonce partially : {}, {}", id, annonceDTO);
        if (annonceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annonceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annonceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnnonceDTO> result = annonceService.partialUpdate(annonceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annonceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /annonces} : get all the Annonces.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Annonces in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AnnonceDTO>> getAllAnnonces(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("demande-is-null".equals(filter)) {
            LOG.debug("REST request to get all Annonces where demande is null");
            return new ResponseEntity<>(annonceService.findAllWhereDemandeIsNull(), HttpStatus.OK);
        }

        if ("offre-is-null".equals(filter)) {
            LOG.debug("REST request to get all Annonces where offre is null");
            return new ResponseEntity<>(annonceService.findAllWhereOffreIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of Annonces");
        Page<AnnonceDTO> page = annonceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annonces/:id} : get the "id" annonce.
     *
     * @param id the id of the annonceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annonceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnnonceDTO> getAnnonce(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Annonce : {}", id);
        Optional<AnnonceDTO> annonceDTO = annonceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annonceDTO);
    }

    /**
     * {@code DELETE  /annonces/:id} : delete the "id" annonce.
     *
     * @param id the id of the annonceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Annonce : {}", id);
        annonceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
