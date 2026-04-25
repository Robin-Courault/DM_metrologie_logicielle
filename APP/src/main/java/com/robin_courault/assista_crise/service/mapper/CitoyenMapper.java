package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.Citoyen;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.service.dto.CitoyenDTO;
import com.robin_courault.assista_crise.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Citoyen} and its DTO {@link CitoyenDTO}.
 */
@Mapper(componentModel = "spring")
public interface CitoyenMapper extends EntityMapper<CitoyenDTO, Citoyen> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurLogin")
    CitoyenDTO toDto(Citoyen s);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);
}
