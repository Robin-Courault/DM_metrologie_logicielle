package com.robin_courault.assista_crise.service;

import com.robin_courault.assista_crise.domain.Autorite;
import com.robin_courault.assista_crise.repository.AutoriteRepository;
import com.robin_courault.assista_crise.service.dto.AutoriteDTO;
import com.robin_courault.assista_crise.service.mapper.AutoriteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.robin_courault.assista_crise.domain.Autorite}.
 */
@Service
@Transactional
public class AutoriteService {

    private static final Logger LOG = LoggerFactory.getLogger(AutoriteService.class);

    private final AutoriteRepository autoriteRepository;

    private final AutoriteMapper autoriteMapper;

    public AutoriteService(AutoriteRepository autoriteRepository, AutoriteMapper autoriteMapper) {
        this.autoriteRepository = autoriteRepository;
        this.autoriteMapper = autoriteMapper;
    }

    /**
     * Save a autorite.
     *
     * @param autoriteDTO the entity to save.
     * @return the persisted entity.
     */
    public AutoriteDTO save(AutoriteDTO autoriteDTO) {
        LOG.debug("Request to save Autorite : {}", autoriteDTO);
        Autorite autorite = autoriteMapper.toEntity(autoriteDTO);
        autorite = autoriteRepository.save(autorite);
        return autoriteMapper.toDto(autorite);
    }

    /**
     * Update a autorite.
     *
     * @param autoriteDTO the entity to save.
     * @return the persisted entity.
     */
    public AutoriteDTO update(AutoriteDTO autoriteDTO) {
        LOG.debug("Request to update Autorite : {}", autoriteDTO);
        Autorite autorite = autoriteMapper.toEntity(autoriteDTO);
        autorite = autoriteRepository.save(autorite);
        return autoriteMapper.toDto(autorite);
    }

    /**
     * Partially update a autorite.
     *
     * @param autoriteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AutoriteDTO> partialUpdate(AutoriteDTO autoriteDTO) {
        LOG.debug("Request to partially update Autorite : {}", autoriteDTO);

        return autoriteRepository
            .findById(autoriteDTO.getId())
            .map(existingAutorite -> {
                autoriteMapper.partialUpdate(existingAutorite, autoriteDTO);

                return existingAutorite;
            })
            .map(autoriteRepository::save)
            .map(autoriteMapper::toDto);
    }

    /**
     * Get all the autorites.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AutoriteDTO> findAll() {
        LOG.debug("Request to get all Autorites");
        return autoriteRepository.findAll().stream().map(autoriteMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one autorite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AutoriteDTO> findOne(Long id) {
        LOG.debug("Request to get Autorite : {}", id);
        return autoriteRepository.findById(id).map(autoriteMapper::toDto);
    }

    /**
     * Delete the autorite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Autorite : {}", id);
        autoriteRepository.deleteById(id);
    }
}
