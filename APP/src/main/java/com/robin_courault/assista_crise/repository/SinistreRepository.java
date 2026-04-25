package com.robin_courault.assista_crise.repository;

import com.robin_courault.assista_crise.domain.Sinistre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sinistre entity.
 */
@Repository
public interface SinistreRepository extends JpaRepository<Sinistre, Long> {
    default Optional<Sinistre> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Sinistre> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Sinistre> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select sinistre from Sinistre sinistre left join fetch sinistre.utilisateur",
        countQuery = "select count(sinistre) from Sinistre sinistre"
    )
    Page<Sinistre> findAllWithToOneRelationships(Pageable pageable);

    @Query("select sinistre from Sinistre sinistre left join fetch sinistre.utilisateur")
    List<Sinistre> findAllWithToOneRelationships();

    @Query("select sinistre from Sinistre sinistre left join fetch sinistre.utilisateur where sinistre.id =:id")
    Optional<Sinistre> findOneWithToOneRelationships(@Param("id") Long id);
}
