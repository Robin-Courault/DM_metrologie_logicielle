package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.Agent;
import com.robin_courault.assista_crise.domain.Autorite;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.service.dto.AgentDTO;
import com.robin_courault.assista_crise.service.dto.AutoriteDTO;
import com.robin_courault.assista_crise.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agent} and its DTO {@link AgentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgentMapper extends EntityMapper<AgentDTO, Agent> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurLogin")
    @Mapping(target = "autorite", source = "autorite", qualifiedByName = "autoriteNom")
    AgentDTO toDto(Agent s);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);

    @Named("autoriteNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    AutoriteDTO toDtoAutoriteNom(Autorite autorite);
}
