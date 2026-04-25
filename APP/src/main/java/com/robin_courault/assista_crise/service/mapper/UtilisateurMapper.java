package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.SalonDiscussion;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.service.dto.SalonDiscussionDTO;
import com.robin_courault.assista_crise.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utilisateur} and its DTO {@link UtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {
    @Mapping(target = "salonses", source = "salonses", qualifiedByName = "salonDiscussionIdSet")
    UtilisateurDTO toDto(Utilisateur s);

    @Mapping(target = "salonses", ignore = true)
    @Mapping(target = "removeSalons", ignore = true)
    Utilisateur toEntity(UtilisateurDTO utilisateurDTO);

    @Named("salonDiscussionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SalonDiscussionDTO toDtoSalonDiscussionId(SalonDiscussion salonDiscussion);

    @Named("salonDiscussionIdSet")
    default Set<SalonDiscussionDTO> toDtoSalonDiscussionIdSet(Set<SalonDiscussion> salonDiscussion) {
        return salonDiscussion.stream().map(this::toDtoSalonDiscussionId).collect(Collectors.toSet());
    }
}
