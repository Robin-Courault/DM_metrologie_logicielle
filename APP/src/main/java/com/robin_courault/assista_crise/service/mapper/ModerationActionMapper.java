package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.Administrateur;
import com.robin_courault.assista_crise.domain.Annonce;
import com.robin_courault.assista_crise.domain.ModerationAction;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.service.dto.AdministrateurDTO;
import com.robin_courault.assista_crise.service.dto.AnnonceDTO;
import com.robin_courault.assista_crise.service.dto.ModerationActionDTO;
import com.robin_courault.assista_crise.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ModerationAction} and its DTO {@link ModerationActionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModerationActionMapper extends EntityMapper<ModerationActionDTO, ModerationAction> {
    @Mapping(target = "administrateur", source = "administrateur", qualifiedByName = "administrateurId")
    @Mapping(target = "annonce", source = "annonce", qualifiedByName = "annonceId")
    @Mapping(target = "utilisateurCible", source = "utilisateurCible", qualifiedByName = "utilisateurLogin")
    ModerationActionDTO toDto(ModerationAction s);

    @Named("administrateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdministrateurDTO toDtoAdministrateurId(Administrateur administrateur);

    @Named("annonceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AnnonceDTO toDtoAnnonceId(Annonce annonce);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);
}
