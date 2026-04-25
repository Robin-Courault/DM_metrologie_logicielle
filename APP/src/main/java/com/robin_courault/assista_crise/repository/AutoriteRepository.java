package com.robin_courault.assista_crise.repository;

import com.robin_courault.assista_crise.domain.Autorite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Autorite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoriteRepository extends JpaRepository<Autorite, Long> {}
