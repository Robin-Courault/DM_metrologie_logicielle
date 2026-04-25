package com.robin_courault.assista_crise.repository;

import com.robin_courault.assista_crise.domain.SalonDiscussion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SalonDiscussionRepositoryWithBagRelationshipsImpl implements SalonDiscussionRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SALONDISCUSSIONS_PARAMETER = "salonDiscussions";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SalonDiscussion> fetchBagRelationships(Optional<SalonDiscussion> salonDiscussion) {
        return salonDiscussion.map(this::fetchParticipantses);
    }

    @Override
    public Page<SalonDiscussion> fetchBagRelationships(Page<SalonDiscussion> salonDiscussions) {
        return new PageImpl<>(
            fetchBagRelationships(salonDiscussions.getContent()),
            salonDiscussions.getPageable(),
            salonDiscussions.getTotalElements()
        );
    }

    @Override
    public List<SalonDiscussion> fetchBagRelationships(List<SalonDiscussion> salonDiscussions) {
        return Optional.of(salonDiscussions).map(this::fetchParticipantses).orElse(Collections.emptyList());
    }

    SalonDiscussion fetchParticipantses(SalonDiscussion result) {
        return entityManager
            .createQuery(
                "select salonDiscussion from SalonDiscussion salonDiscussion left join fetch salonDiscussion.participantses where salonDiscussion.id = :id",
                SalonDiscussion.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<SalonDiscussion> fetchParticipantses(List<SalonDiscussion> salonDiscussions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, salonDiscussions.size()).forEach(index -> order.put(salonDiscussions.get(index).getId(), index));
        List<SalonDiscussion> result = entityManager
            .createQuery(
                "select salonDiscussion from SalonDiscussion salonDiscussion left join fetch salonDiscussion.participantses where salonDiscussion in :salonDiscussions",
                SalonDiscussion.class
            )
            .setParameter(SALONDISCUSSIONS_PARAMETER, salonDiscussions)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
