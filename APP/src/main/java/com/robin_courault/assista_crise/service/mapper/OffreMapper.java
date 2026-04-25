package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.Annonce;
import com.robin_courault.assista_crise.domain.Citoyen;
import com.robin_courault.assista_crise.domain.Crise;
import com.robin_courault.assista_crise.domain.Demande;
import com.robin_courault.assista_crise.domain.Offre;
import com.robin_courault.assista_crise.service.dto.AnnonceDTO;
import com.robin_courault.assista_crise.service.dto.CitoyenDTO;
import com.robin_courault.assista_crise.service.dto.CriseDTO;
import com.robin_courault.assista_crise.service.dto.DemandeDTO;
import com.robin_courault.assista_crise.service.dto.OffreDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Offre} and its DTO {@link OffreDTO}.
 */
@Mapper(componentModel = "spring")
public interface OffreMapper extends EntityMapper<OffreDTO, Offre> {
    @Mapping(target = "annonce", source = "annonce", qualifiedByName = "annonceTitre")
    @Mapping(target = "citoyen", source = "citoyen", qualifiedByName = "citoyenId")
    @Mapping(target = "crise", source = "crise", qualifiedByName = "criseId")
    @Mapping(target = "demandeses", source = "demandeses", qualifiedByName = "demandeIdSet")
    OffreDTO toDto(Offre s);

    @Mapping(target = "demandeses", ignore = true)
    @Mapping(target = "removeDemandes", ignore = true)
    Offre toEntity(OffreDTO offreDTO);

    @Named("annonceTitre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titre", source = "titre")
    AnnonceDTO toDtoAnnonceTitre(Annonce annonce);

    @Named("citoyenId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CitoyenDTO toDtoCitoyenId(Citoyen citoyen);

    @Named("criseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CriseDTO toDtoCriseId(Crise crise);

    @Named("demandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DemandeDTO toDtoDemandeId(Demande demande);

    @Named("demandeIdSet")
    default Set<DemandeDTO> toDtoDemandeIdSet(Set<Demande> demande) {
        return demande.stream().map(this::toDtoDemandeId).collect(Collectors.toSet());
    }
}
