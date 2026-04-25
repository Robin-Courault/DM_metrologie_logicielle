package com.robin_courault.assista_crise.service;

import com.robin_courault.assista_crise.domain.SalonDiscussion;
import com.robin_courault.assista_crise.repository.SalonDiscussionRepository;
import com.robin_courault.assista_crise.service.dto.SalonDiscussionDTO;
import com.robin_courault.assista_crise.service.mapper.SalonDiscussionMapper;
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
 * Service Implementation for managing {@link com.robin_courault.assista_crise.domain.SalonDiscussion}.
 */
@Service
@Transactional
public class SalonDiscussionService {

    private static final Logger LOG = LoggerFactory.getLogger(SalonDiscussionService.class);

    private final SalonDiscussionRepository salonDiscussionRepository;

    private final SalonDiscussionMapper salonDiscussionMapper;

    public SalonDiscussionService(SalonDiscussionRepository salonDiscussionRepository, SalonDiscussionMapper salonDiscussionMapper) {
        this.salonDiscussionRepository = salonDiscussionRepository;
        this.salonDiscussionMapper = salonDiscussionMapper;
    }

    /**
     * Save a salonDiscussion.
     *
     * @param salonDiscussionDTO the entity to save.
     * @return the persisted entity.
     */
    public SalonDiscussionDTO save(SalonDiscussionDTO salonDiscussionDTO) {
        LOG.debug("Request to save SalonDiscussion : {}", salonDiscussionDTO);
        SalonDiscussion salonDiscussion = salonDiscussionMapper.toEntity(salonDiscussionDTO);
        salonDiscussion = salonDiscussionRepository.save(salonDiscussion);
        return salonDiscussionMapper.toDto(salonDiscussion);
    }

    /**
     * Update a salonDiscussion.
     *
     * @param salonDiscussionDTO the entity to save.
     * @return the persisted entity.
     */
    public SalonDiscussionDTO update(SalonDiscussionDTO salonDiscussionDTO) {
        LOG.debug("Request to update SalonDiscussion : {}", salonDiscussionDTO);
        SalonDiscussion salonDiscussion = salonDiscussionMapper.toEntity(salonDiscussionDTO);
        salonDiscussion = salonDiscussionRepository.save(salonDiscussion);
        return salonDiscussionMapper.toDto(salonDiscussion);
    }

    /**
     * Partially update a salonDiscussion.
     *
     * @param salonDiscussionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SalonDiscussionDTO> partialUpdate(SalonDiscussionDTO salonDiscussionDTO) {
        LOG.debug("Request to partially update SalonDiscussion : {}", salonDiscussionDTO);

        return salonDiscussionRepository
            .findById(salonDiscussionDTO.getId())
            .map(existingSalonDiscussion -> {
                salonDiscussionMapper.partialUpdate(existingSalonDiscussion, salonDiscussionDTO);

                return existingSalonDiscussion;
            })
            .map(salonDiscussionRepository::save)
            .map(salonDiscussionMapper::toDto);
    }

    /**
     * Get all the salonDiscussions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SalonDiscussionDTO> findAll() {
        LOG.debug("Request to get all SalonDiscussions");
        return salonDiscussionRepository
            .findAll()
            .stream()
            .map(salonDiscussionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the salonDiscussions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SalonDiscussionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return salonDiscussionRepository.findAllWithEagerRelationships(pageable).map(salonDiscussionMapper::toDto);
    }

    /**
     *  Get all the salonDiscussions where Demande is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SalonDiscussionDTO> findAllWhereDemandeIsNull() {
        LOG.debug("Request to get all salonDiscussions where Demande is null");
        return StreamSupport.stream(salonDiscussionRepository.findAll().spliterator(), false)
            .filter(salonDiscussion -> salonDiscussion.getDemande() == null)
            .map(salonDiscussionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one salonDiscussion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SalonDiscussionDTO> findOne(Long id) {
        LOG.debug("Request to get SalonDiscussion : {}", id);
        return salonDiscussionRepository.findOneWithEagerRelationships(id).map(salonDiscussionMapper::toDto);
    }

    /**
     * Delete the salonDiscussion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SalonDiscussion : {}", id);
        salonDiscussionRepository.deleteById(id);
    }
}
