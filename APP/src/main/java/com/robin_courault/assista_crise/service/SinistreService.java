package com.robin_courault.assista_crise.service;

import com.robin_courault.assista_crise.domain.Sinistre;
import com.robin_courault.assista_crise.repository.SinistreRepository;
import com.robin_courault.assista_crise.service.dto.SinistreDTO;
import com.robin_courault.assista_crise.service.mapper.SinistreMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.robin_courault.assista_crise.domain.Sinistre}.
 */
@Service
@Transactional
public class SinistreService {

    private static final Logger LOG = LoggerFactory.getLogger(SinistreService.class);

    private final SinistreRepository sinistreRepository;

    private final SinistreMapper sinistreMapper;

    public SinistreService(SinistreRepository sinistreRepository, SinistreMapper sinistreMapper) {
        this.sinistreRepository = sinistreRepository;
        this.sinistreMapper = sinistreMapper;
    }

    /**
     * Save a sinistre.
     *
     * @param sinistreDTO the entity to save.
     * @return the persisted entity.
     */
    public SinistreDTO save(SinistreDTO sinistreDTO) {
        LOG.debug("Request to save Sinistre : {}", sinistreDTO);
        Sinistre sinistre = sinistreMapper.toEntity(sinistreDTO);
        sinistre = sinistreRepository.save(sinistre);
        return sinistreMapper.toDto(sinistre);
    }

    /**
     * Update a sinistre.
     *
     * @param sinistreDTO the entity to save.
     * @return the persisted entity.
     */
    public SinistreDTO update(SinistreDTO sinistreDTO) {
        LOG.debug("Request to update Sinistre : {}", sinistreDTO);
        Sinistre sinistre = sinistreMapper.toEntity(sinistreDTO);
        sinistre = sinistreRepository.save(sinistre);
        return sinistreMapper.toDto(sinistre);
    }

    /**
     * Partially update a sinistre.
     *
     * @param sinistreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SinistreDTO> partialUpdate(SinistreDTO sinistreDTO) {
        LOG.debug("Request to partially update Sinistre : {}", sinistreDTO);

        return sinistreRepository
            .findById(sinistreDTO.getId())
            .map(existingSinistre -> {
                sinistreMapper.partialUpdate(existingSinistre, sinistreDTO);

                return existingSinistre;
            })
            .map(sinistreRepository::save)
            .map(sinistreMapper::toDto);
    }

    /**
     * Get all the sinistres.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SinistreDTO> findAll() {
        LOG.debug("Request to get all Sinistres");
        return sinistreRepository.findAll().stream().map(sinistreMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the sinistres with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SinistreDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sinistreRepository.findAllWithEagerRelationships(pageable).map(sinistreMapper::toDto);
    }

    /**
     * Get one sinistre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SinistreDTO> findOne(Long id) {
        LOG.debug("Request to get Sinistre : {}", id);
        return sinistreRepository.findOneWithEagerRelationships(id).map(sinistreMapper::toDto);
    }

    /**
     * Delete the sinistre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Sinistre : {}", id);
        sinistreRepository.deleteById(id);
    }
}
