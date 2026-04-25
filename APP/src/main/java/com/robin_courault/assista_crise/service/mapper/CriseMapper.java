package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.Autorite;
import com.robin_courault.assista_crise.domain.Crise;
import com.robin_courault.assista_crise.service.dto.AutoriteDTO;
import com.robin_courault.assista_crise.service.dto.CriseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Crise} and its DTO {@link CriseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CriseMapper extends EntityMapper<CriseDTO, Crise> {
    @Mapping(target = "autorite", source = "autorite", qualifiedByName = "autoriteId")
    CriseDTO toDto(Crise s);

    @Named("autoriteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AutoriteDTO toDtoAutoriteId(Autorite autorite);
}
