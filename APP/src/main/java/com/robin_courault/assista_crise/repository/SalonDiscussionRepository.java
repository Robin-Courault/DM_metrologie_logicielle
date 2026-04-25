package com.robin_courault.assista_crise.repository;

import com.robin_courault.assista_crise.domain.SalonDiscussion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SalonDiscussion entity.
 *
 * When extending this class, extend SalonDiscussionRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SalonDiscussionRepository extends SalonDiscussionRepositoryWithBagRelationships, JpaRepository<SalonDiscussion, Long> {
    default Optional<SalonDiscussion> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<SalonDiscussion> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<SalonDiscussion> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
