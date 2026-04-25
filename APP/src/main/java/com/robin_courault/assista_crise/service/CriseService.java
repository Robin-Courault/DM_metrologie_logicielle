package com.robin_courault.assista_crise.service;

import com.robin_courault.assista_crise.domain.Crise;
import com.robin_courault.assista_crise.repository.CriseRepository;
import com.robin_courault.assista_crise.service.dto.CriseDTO;
import com.robin_courault.assista_crise.service.mapper.CriseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.robin_courault.assista_crise.domain.Crise}.
 */
@Service
@Transactional
public class CriseService {

    private static final Logger LOG = LoggerFactory.getLogger(CriseService.class);

    private final CriseRepository criseRepository;

    private final CriseMapper criseMapper;

    public CriseService(CriseRepository criseRepository, CriseMapper criseMapper) {
        this.criseRepository = criseRepository;
        this.criseMapper = criseMapper;
    }

    /**
     * Save a crise.
     *
     * @param criseDTO the entity to save.
     * @return the persisted entity.
     */
    public CriseDTO save(CriseDTO criseDTO) {
        LOG.debug("Request to save Crise : {}", criseDTO);
        Crise crise = criseMapper.toEntity(criseDTO);
        crise = criseRepository.save(crise);
        return criseMapper.toDto(crise);
    }

    /**
     * Update a crise.
     *
     * @param criseDTO the entity to save.
     * @return the persisted entity.
     */
    public CriseDTO update(CriseDTO criseDTO) {
        LOG.debug("Request to update Crise : {}", criseDTO);
        Crise crise = criseMapper.toEntity(criseDTO);
        crise = criseRepository.save(crise);
        return criseMapper.toDto(crise);
    }

    /**
     * Partially update a crise.
     *
     * @param criseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CriseDTO> partialUpdate(CriseDTO criseDTO) {
        LOG.debug("Request to partially update Crise : {}", criseDTO);

        return criseRepository
            .findById(criseDTO.getId())
            .map(existingCrise -> {
                criseMapper.partialUpdate(existingCrise, criseDTO);

                return existingCrise;
            })
            .map(criseRepository::save)
            .map(criseMapper::toDto);
    }

    /**
     * Get all the crises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CriseDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Crises");
        return criseRepository.findAll(pageable).map(criseMapper::toDto);
    }

    /**
     * Get one crise by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CriseDTO> findOne(Long id) {
        LOG.debug("Request to get Crise : {}", id);
        return criseRepository.findById(id).map(criseMapper::toDto);
    }

    /**
     * Delete the crise by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Crise : {}", id);
        criseRepository.deleteById(id);
    }
}
