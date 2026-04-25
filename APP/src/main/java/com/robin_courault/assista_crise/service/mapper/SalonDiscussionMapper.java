package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.SalonDiscussion;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.service.dto.SalonDiscussionDTO;
import com.robin_courault.assista_crise.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SalonDiscussion} and its DTO {@link SalonDiscussionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalonDiscussionMapper extends EntityMapper<SalonDiscussionDTO, SalonDiscussion> {
    @Mapping(target = "participantses", source = "participantses", qualifiedByName = "utilisateurLoginSet")
    SalonDiscussionDTO toDto(SalonDiscussion s);

    @Mapping(target = "removeParticipants", ignore = true)
    SalonDiscussion toEntity(SalonDiscussionDTO salonDiscussionDTO);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);

    @Named("utilisateurLoginSet")
    default Set<UtilisateurDTO> toDtoUtilisateurLoginSet(Set<Utilisateur> utilisateur) {
        return utilisateur.stream().map(this::toDtoUtilisateurLogin).collect(Collectors.toSet());
    }
}
