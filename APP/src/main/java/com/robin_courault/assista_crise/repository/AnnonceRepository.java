package com.robin_courault.assista_crise.repository;

import com.robin_courault.assista_crise.domain.Annonce;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Annonce entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long> {}
