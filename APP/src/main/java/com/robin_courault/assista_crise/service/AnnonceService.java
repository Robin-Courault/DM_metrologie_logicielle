package com.robin_courault.assista_crise.service;

import com.robin_courault.assista_crise.domain.Annonce;
import com.robin_courault.assista_crise.repository.AnnonceRepository;
import com.robin_courault.assista_crise.service.dto.AnnonceDTO;
import com.robin_courault.assista_crise.service.mapper.AnnonceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.robin_courault.assista_crise.domain.Annonce}.
 */
@Service
@Transactional
public class AnnonceService {

    private static final Logger LOG = LoggerFactory.getLogger(AnnonceService.class);

    private final AnnonceRepository annonceRepository;

    private final AnnonceMapper annonceMapper;

    public AnnonceService(AnnonceRepository annonceRepository, AnnonceMapper annonceMapper) {
        this.annonceRepository = annonceRepository;
        this.annonceMapper = annonceMapper;
    }

    /**
     * Save a annonce.
     *
     * @param annonceDTO the entity to save.
     * @return the persisted entity.
     */
    public AnnonceDTO save(AnnonceDTO annonceDTO) {
        LOG.debug("Request to save Annonce : {}", annonceDTO);
        Annonce annonce = annonceMapper.toEntity(annonceDTO);
        annonce = annonceRepository.save(annonce);
        return annonceMapper.toDto(annonce);
    }

    /**
     * Update a annonce.
     *
     * @param annonceDTO the entity to save.
     * @return the persisted entity.
     */
    public AnnonceDTO update(AnnonceDTO annonceDTO) {
        LOG.debug("Request to update Annonce : {}", annonceDTO);
        Annonce annonce = annonceMapper.toEntity(annonceDTO);
        annonce = annonceRepository.save(annonce);
        return annonceMapper.toDto(annonce);
    }

    /**
     * Partially update a annonce.
     *
     * @param annonceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AnnonceDTO> partialUpdate(AnnonceDTO annonceDTO) {
        LOG.debug("Request to partially update Annonce : {}", annonceDTO);

        return annonceRepository
            .findById(annonceDTO.getId())
            .map(existingAnnonce -> {
                annonceMapper.partialUpdate(existingAnnonce, annonceDTO);

                return existingAnnonce;
            })
            .map(annonceRepository::save)
            .map(annonceMapper::toDto);
    }

    /**
     * Get all the annonces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AnnonceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Annonces");
        return annonceRepository.findAll(pageable).map(annonceMapper::toDto);
    }

    /**
     *  Get all the annonces where Demande is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AnnonceDTO> findAllWhereDemandeIsNull() {
        LOG.debug("Request to get all annonces where Demande is null");
        return StreamSupport.stream(annonceRepository.findAll().spliterator(), false)
            .filter(annonce -> annonce.getDemande() == null)
            .map(annonceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the annonces where Offre is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AnnonceDTO> findAllWhereOffreIsNull() {
        LOG.debug("Request to get all annonces where Offre is null");
        return StreamSupport.stream(annonceRepository.findAll().spliterator(), false)
            .filter(annonce -> annonce.getOffre() == null)
            .map(annonceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one annonce by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AnnonceDTO> findOne(Long id) {
        LOG.debug("Request to get Annonce : {}", id);
        return annonceRepository.findById(id).map(annonceMapper::toDto);
    }

    /**
     * Delete the annonce by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Annonce : {}", id);
        annonceRepository.deleteById(id);
    }
}
