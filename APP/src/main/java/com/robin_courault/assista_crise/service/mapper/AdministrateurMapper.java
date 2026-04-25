package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.Administrateur;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.service.dto.AdministrateurDTO;
import com.robin_courault.assista_crise.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Administrateur} and its DTO {@link AdministrateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdministrateurMapper extends EntityMapper<AdministrateurDTO, Administrateur> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurLogin")
    AdministrateurDTO toDto(Administrateur s);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);
}
