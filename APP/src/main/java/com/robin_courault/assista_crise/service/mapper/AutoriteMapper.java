package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.Autorite;
import com.robin_courault.assista_crise.service.dto.AutoriteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Autorite} and its DTO {@link AutoriteDTO}.
 */
@Mapper(componentModel = "spring")
public interface AutoriteMapper extends EntityMapper<AutoriteDTO, Autorite> {}
