package com.robin_courault.assista_crise.repository;

import com.robin_courault.assista_crise.domain.ModerationAction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ModerationAction entity.
 */
@Repository
public interface ModerationActionRepository extends JpaRepository<ModerationAction, Long> {
    default Optional<ModerationAction> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ModerationAction> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ModerationAction> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select moderationAction from ModerationAction moderationAction left join fetch moderationAction.utilisateurCible",
        countQuery = "select count(moderationAction) from ModerationAction moderationAction"
    )
    Page<ModerationAction> findAllWithToOneRelationships(Pageable pageable);

    @Query("select moderationAction from ModerationAction moderationAction left join fetch moderationAction.utilisateurCible")
    List<ModerationAction> findAllWithToOneRelationships();

    @Query(
        "select moderationAction from ModerationAction moderationAction left join fetch moderationAction.utilisateurCible where moderationAction.id =:id"
    )
    Optional<ModerationAction> findOneWithToOneRelationships(@Param("id") Long id);
}
