package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.Annonce;
import com.robin_courault.assista_crise.domain.Crise;
import com.robin_courault.assista_crise.domain.Demande;
import com.robin_courault.assista_crise.domain.Offre;
import com.robin_courault.assista_crise.domain.SalonDiscussion;
import com.robin_courault.assista_crise.domain.Sinistre;
import com.robin_courault.assista_crise.service.dto.AnnonceDTO;
import com.robin_courault.assista_crise.service.dto.CriseDTO;
import com.robin_courault.assista_crise.service.dto.DemandeDTO;
import com.robin_courault.assista_crise.service.dto.OffreDTO;
import com.robin_courault.assista_crise.service.dto.SalonDiscussionDTO;
import com.robin_courault.assista_crise.service.dto.SinistreDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Demande} and its DTO {@link DemandeDTO}.
 */
@Mapper(componentModel = "spring")
public interface DemandeMapper extends EntityMapper<DemandeDTO, Demande> {
    @Mapping(target = "annonce", source = "annonce", qualifiedByName = "annonceTitre")
    @Mapping(target = "salonDiscussion", source = "salonDiscussion", qualifiedByName = "salonDiscussionId")
    @Mapping(target = "sinistre", source = "sinistre", qualifiedByName = "sinistreId")
    @Mapping(target = "crise", source = "crise", qualifiedByName = "criseId")
    @Mapping(target = "offreses", source = "offreses", qualifiedByName = "offreIdSet")
    DemandeDTO toDto(Demande s);

    @Mapping(target = "removeOffres", ignore = true)
    Demande toEntity(DemandeDTO demandeDTO);

    @Named("annonceTitre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titre", source = "titre")
    AnnonceDTO toDtoAnnonceTitre(Annonce annonce);

    @Named("salonDiscussionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SalonDiscussionDTO toDtoSalonDiscussionId(SalonDiscussion salonDiscussion);

    @Named("sinistreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SinistreDTO toDtoSinistreId(Sinistre sinistre);

    @Named("criseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CriseDTO toDtoCriseId(Crise crise);

    @Named("offreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OffreDTO toDtoOffreId(Offre offre);

    @Named("offreIdSet")
    default Set<OffreDTO> toDtoOffreIdSet(Set<Offre> offre) {
        return offre.stream().map(this::toDtoOffreId).collect(Collectors.toSet());
    }
}
