package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.Message;
import com.robin_courault.assista_crise.domain.SalonDiscussion;
import com.robin_courault.assista_crise.domain.Utilisateur;
import com.robin_courault.assista_crise.service.dto.MessageDTO;
import com.robin_courault.assista_crise.service.dto.SalonDiscussionDTO;
import com.robin_courault.assista_crise.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurLogin")
    @Mapping(target = "salonDiscussion", source = "salonDiscussion", qualifiedByName = "salonDiscussionId")
    MessageDTO toDto(Message s);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);

    @Named("salonDiscussionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SalonDiscussionDTO toDtoSalonDiscussionId(SalonDiscussion salonDiscussion);
}
