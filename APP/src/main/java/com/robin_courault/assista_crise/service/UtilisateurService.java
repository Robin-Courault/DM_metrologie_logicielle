package com.robin_courault.assista_crise.service;

import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.repository.UtilisateurRepository;
import com.robin_courault.assista_crise.service.dto.UtilisateurDTO;
import com.robin_courault.assista_crise.service.mapper.UtilisateurMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.robin_courault.assista_crise.domain.Utilisateur}.
 */
@Service
@Transactional
public class UtilisateurService {

    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurService.class);

    private final UtilisateurRepository utilisateurRepository;

    private final UtilisateurMapper utilisateurMapper;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    /**
     * Save a utilisateur.
     *
     * @param utilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    public UtilisateurDTO save(UtilisateurDTO utilisateurDTO) {
        LOG.debug("Request to save Utilisateur : {}", utilisateurDTO);
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);
        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toDto(utilisateur);
    }

    /**
     * Update a utilisateur.
     *
     * @param utilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    public UtilisateurDTO update(UtilisateurDTO utilisateurDTO) {
        LOG.debug("Request to update Utilisateur : {}", utilisateurDTO);
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);
        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toDto(utilisateur);
    }

    /**
     * Partially update a utilisateur.
     *
     * @param utilisateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UtilisateurDTO> partialUpdate(UtilisateurDTO utilisateurDTO) {
        LOG.debug("Request to partially update Utilisateur : {}", utilisateurDTO);

        return utilisateurRepository
            .findById(utilisateurDTO.getId())
            .map(existingUtilisateur -> {
                utilisateurMapper.partialUpdate(existingUtilisateur, utilisateurDTO);

                return existingUtilisateur;
            })
            .map(utilisateurRepository::save)
            .map(utilisateurMapper::toDto);
    }

    /**
     * Get all the utilisateurs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAll() {
        LOG.debug("Request to get all Utilisateurs");
        return utilisateurRepository.findAll().stream().map(utilisateurMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the utilisateurs where Sinistre is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAllWhereSinistreIsNull() {
        LOG.debug("Request to get all utilisateurs where Sinistre is null");
        return StreamSupport.stream(utilisateurRepository.findAll().spliterator(), false)
            .filter(utilisateur -> utilisateur.getSinistre() == null)
            .map(utilisateurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the utilisateurs where Citoyen is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAllWhereCitoyenIsNull() {
        LOG.debug("Request to get all utilisateurs where Citoyen is null");
        return StreamSupport.stream(utilisateurRepository.findAll().spliterator(), false)
            .filter(utilisateur -> utilisateur.getCitoyen() == null)
            .map(utilisateurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the utilisateurs where Agent is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAllWhereAgentIsNull() {
        LOG.debug("Request to get all utilisateurs where Agent is null");
        return StreamSupport.stream(utilisateurRepository.findAll().spliterator(), false)
            .filter(utilisateur -> utilisateur.getAgent() == null)
            .map(utilisateurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the utilisateurs where Administrateur is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAllWhereAdministrateurIsNull() {
        LOG.debug("Request to get all utilisateurs where Administrateur is null");
        return StreamSupport.stream(utilisateurRepository.findAll().spliterator(), false)
            .filter(utilisateur -> utilisateur.getAdministrateur() == null)
            .map(utilisateurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one utilisateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UtilisateurDTO> findOne(Long id) {
        LOG.debug("Request to get Utilisateur : {}", id);
        return utilisateurRepository.findById(id).map(utilisateurMapper::toDto);
    }

    /**
     * Delete the utilisateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Utilisateur : {}", id);
        utilisateurRepository.deleteById(id);
    }
}
