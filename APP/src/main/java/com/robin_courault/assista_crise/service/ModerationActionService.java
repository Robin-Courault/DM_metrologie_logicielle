package com.robin_courault.assista_crise.service;

import com.robin_courault.assista_crise.domain.ModerationAction;
import com.robin_courault.assista_crise.repository.ModerationActionRepository;
import com.robin_courault.assista_crise.service.dto.ModerationActionDTO;
import com.robin_courault.assista_crise.service.mapper.ModerationActionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.robin_courault.assista_crise.domain.ModerationAction}.
 */
@Service
@Transactional
public class ModerationActionService {

    private static final Logger LOG = LoggerFactory.getLogger(ModerationActionService.class);

    private final ModerationActionRepository moderationActionRepository;

    private final ModerationActionMapper moderationActionMapper;

    public ModerationActionService(ModerationActionRepository moderationActionRepository, ModerationActionMapper moderationActionMapper) {
        this.moderationActionRepository = moderationActionRepository;
        this.moderationActionMapper = moderationActionMapper;
    }

    /**
     * Save a moderationAction.
     *
     * @param moderationActionDTO the entity to save.
     * @return the persisted entity.
     */
    public ModerationActionDTO save(ModerationActionDTO moderationActionDTO) {
        LOG.debug("Request to save ModerationAction : {}", moderationActionDTO);
        ModerationAction moderationAction = moderationActionMapper.toEntity(moderationActionDTO);
        moderationAction = moderationActionRepository.save(moderationAction);
        return moderationActionMapper.toDto(moderationAction);
    }

    /**
     * Update a moderationAction.
     *
     * @param moderationActionDTO the entity to save.
     * @return the persisted entity.
     */
    public ModerationActionDTO update(ModerationActionDTO moderationActionDTO) {
        LOG.debug("Request to update ModerationAction : {}", moderationActionDTO);
        ModerationAction moderationAction = moderationActionMapper.toEntity(moderationActionDTO);
        moderationAction = moderationActionRepository.save(moderationAction);
        return moderationActionMapper.toDto(moderationAction);
    }

    /**
     * Partially update a moderationAction.
     *
     * @param moderationActionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ModerationActionDTO> partialUpdate(ModerationActionDTO moderationActionDTO) {
        LOG.debug("Request to partially update ModerationAction : {}", moderationActionDTO);

        return moderationActionRepository
            .findById(moderationActionDTO.getId())
            .map(existingModerationAction -> {
                moderationActionMapper.partialUpdate(existingModerationAction, moderationActionDTO);

                return existingModerationAction;
            })
            .map(moderationActionRepository::save)
            .map(moderationActionMapper::toDto);
    }

    /**
     * Get all the moderationActions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ModerationActionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ModerationActions");
        return moderationActionRepository.findAll(pageable).map(moderationActionMapper::toDto);
    }

    /**
     * Get all the moderationActions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ModerationActionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return moderationActionRepository.findAllWithEagerRelationships(pageable).map(moderationActionMapper::toDto);
    }

    /**
     * Get one moderationAction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModerationActionDTO> findOne(Long id) {
        LOG.debug("Request to get ModerationAction : {}", id);
        return moderationActionRepository.findOneWithEagerRelationships(id).map(moderationActionMapper::toDto);
    }

    /**
     * Delete the moderationAction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ModerationAction : {}", id);
        moderationActionRepository.deleteById(id);
    }
}
