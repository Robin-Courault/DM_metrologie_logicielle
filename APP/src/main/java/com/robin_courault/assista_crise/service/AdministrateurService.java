package com.robin_courault.assista_crise.service;

import com.robin_courault.assista_crise.domain.Administrateur;
import com.robin_courault.assista_crise.repository.AdministrateurRepository;
import com.robin_courault.assista_crise.service.dto.AdministrateurDTO;
import com.robin_courault.assista_crise.service.mapper.AdministrateurMapper;
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
 * Service Implementation for managing {@link com.robin_courault.assista_crise.domain.Administrateur}.
 */
@Service
@Transactional
public class AdministrateurService {

    private static final Logger LOG = LoggerFactory.getLogger(AdministrateurService.class);

    private final AdministrateurRepository administrateurRepository;

    private final AdministrateurMapper administrateurMapper;

    public AdministrateurService(AdministrateurRepository administrateurRepository, AdministrateurMapper administrateurMapper) {
        this.administrateurRepository = administrateurRepository;
        this.administrateurMapper = administrateurMapper;
    }

    /**
     * Save a administrateur.
     *
     * @param administrateurDTO the entity to save.
     * @return the persisted entity.
     */
    public AdministrateurDTO save(AdministrateurDTO administrateurDTO) {
        LOG.debug("Request to save Administrateur : {}", administrateurDTO);
        Administrateur administrateur = administrateurMapper.toEntity(administrateurDTO);
        administrateur = administrateurRepository.save(administrateur);
        return administrateurMapper.toDto(administrateur);
    }

    /**
     * Update a administrateur.
     *
     * @param administrateurDTO the entity to save.
     * @return the persisted entity.
     */
    public AdministrateurDTO update(AdministrateurDTO administrateurDTO) {
        LOG.debug("Request to update Administrateur : {}", administrateurDTO);
        Administrateur administrateur = administrateurMapper.toEntity(administrateurDTO);
        administrateur = administrateurRepository.save(administrateur);
        return administrateurMapper.toDto(administrateur);
    }

    /**
     * Partially update a administrateur.
     *
     * @param administrateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AdministrateurDTO> partialUpdate(AdministrateurDTO administrateurDTO) {
        LOG.debug("Request to partially update Administrateur : {}", administrateurDTO);

        return administrateurRepository
            .findById(administrateurDTO.getId())
            .map(existingAdministrateur -> {
                administrateurMapper.partialUpdate(existingAdministrateur, administrateurDTO);

                return existingAdministrateur;
            })
            .map(administrateurRepository::save)
            .map(administrateurMapper::toDto);
    }

    /**
     * Get all the administrateurs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AdministrateurDTO> findAll() {
        LOG.debug("Request to get all Administrateurs");
        return administrateurRepository
            .findAll()
            .stream()
            .map(administrateurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the administrateurs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AdministrateurDTO> findAllWithEagerRelationships(Pageable pageable) {
        return administrateurRepository.findAllWithEagerRelationships(pageable).map(administrateurMapper::toDto);
    }

    /**
     * Get one administrateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdministrateurDTO> findOne(Long id) {
        LOG.debug("Request to get Administrateur : {}", id);
        return administrateurRepository.findOneWithEagerRelationships(id).map(administrateurMapper::toDto);
    }

    /**
     * Delete the administrateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Administrateur : {}", id);
        administrateurRepository.deleteById(id);
    }
}
