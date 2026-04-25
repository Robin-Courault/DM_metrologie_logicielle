package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.Sinistre;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.service.dto.SinistreDTO;
import com.robin_courault.assista_crise.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sinistre} and its DTO {@link SinistreDTO}.
 */
@Mapper(componentModel = "spring")
public interface SinistreMapper extends EntityMapper<SinistreDTO, Sinistre> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurLogin")
    SinistreDTO toDto(Sinistre s);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);
}
