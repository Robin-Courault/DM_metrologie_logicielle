package com.robin_courault.assista_crise.repository;

import com.robin_courault.assista_crise.domain.Citoyen;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Citoyen entity.
 */
@Repository
public interface CitoyenRepository extends JpaRepository<Citoyen, Long> {
    default Optional<Citoyen> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Citoyen> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Citoyen> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select citoyen from Citoyen citoyen left join fetch citoyen.utilisateur",
        countQuery = "select count(citoyen) from Citoyen citoyen"
    )
    Page<Citoyen> findAllWithToOneRelationships(Pageable pageable);

    @Query("select citoyen from Citoyen citoyen left join fetch citoyen.utilisateur")
    List<Citoyen> findAllWithToOneRelationships();

    @Query("select citoyen from Citoyen citoyen left join fetch citoyen.utilisateur where citoyen.id =:id")
    Optional<Citoyen> findOneWithToOneRelationships(@Param("id") Long id);
}
