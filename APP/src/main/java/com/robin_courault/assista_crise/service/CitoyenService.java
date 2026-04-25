package com.robin_courault.assista_crise.service;

import com.robin_courault.assista_crise.domain.Citoyen;
import com.robin_courault.assista_crise.repository.CitoyenRepository;
import com.robin_courault.assista_crise.service.dto.CitoyenDTO;
import com.robin_courault.assista_crise.service.mapper.CitoyenMapper;
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
 * Service Implementation for managing {@link com.robin_courault.assista_crise.domain.Citoyen}.
 */
@Service
@Transactional
public class CitoyenService {

    private static final Logger LOG = LoggerFactory.getLogger(CitoyenService.class);

    private final CitoyenRepository citoyenRepository;

    private final CitoyenMapper citoyenMapper;

    public CitoyenService(CitoyenRepository citoyenRepository, CitoyenMapper citoyenMapper) {
        this.citoyenRepository = citoyenRepository;
        this.citoyenMapper = citoyenMapper;
    }

    /**
     * Save a citoyen.
     *
     * @param citoyenDTO the entity to save.
     * @return the persisted entity.
     */
    public CitoyenDTO save(CitoyenDTO citoyenDTO) {
        LOG.debug("Request to save Citoyen : {}", citoyenDTO);
        Citoyen citoyen = citoyenMapper.toEntity(citoyenDTO);
        citoyen = citoyenRepository.save(citoyen);
        return citoyenMapper.toDto(citoyen);
    }

    /**
     * Update a citoyen.
     *
     * @param citoyenDTO the entity to save.
     * @return the persisted entity.
     */
    public CitoyenDTO update(CitoyenDTO citoyenDTO) {
        LOG.debug("Request to update Citoyen : {}", citoyenDTO);
        Citoyen citoyen = citoyenMapper.toEntity(citoyenDTO);
        citoyen = citoyenRepository.save(citoyen);
        return citoyenMapper.toDto(citoyen);
    }

    /**
     * Partially update a citoyen.
     *
     * @param citoyenDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CitoyenDTO> partialUpdate(CitoyenDTO citoyenDTO) {
        LOG.debug("Request to partially update Citoyen : {}", citoyenDTO);

        return citoyenRepository
            .findById(citoyenDTO.getId())
            .map(existingCitoyen -> {
                citoyenMapper.partialUpdate(existingCitoyen, citoyenDTO);

                return existingCitoyen;
            })
            .map(citoyenRepository::save)
            .map(citoyenMapper::toDto);
    }

    /**
     * Get all the citoyens.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CitoyenDTO> findAll() {
        LOG.debug("Request to get all Citoyens");
        return citoyenRepository.findAll().stream().map(citoyenMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the citoyens with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CitoyenDTO> findAllWithEagerRelationships(Pageable pageable) {
        return citoyenRepository.findAllWithEagerRelationships(pageable).map(citoyenMapper::toDto);
    }

    /**
     * Get one citoyen by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CitoyenDTO> findOne(Long id) {
        LOG.debug("Request to get Citoyen : {}", id);
        return citoyenRepository.findOneWithEagerRelationships(id).map(citoyenMapper::toDto);
    }

    /**
     * Delete the citoyen by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Citoyen : {}", id);
        citoyenRepository.deleteById(id);
    }
}
