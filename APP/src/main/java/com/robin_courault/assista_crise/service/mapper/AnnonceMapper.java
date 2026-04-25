package com.robin_courault.assista_crise.service.mapper;

import com.robin_courault.assista_crise.domain.Annonce;
import com.robin_courault.assista_crise.service.dto.AnnonceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Annonce} and its DTO {@link AnnonceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnnonceMapper extends EntityMapper<AnnonceDTO, Annonce> {}
