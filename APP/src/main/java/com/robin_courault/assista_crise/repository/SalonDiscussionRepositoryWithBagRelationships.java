package com.robin_courault.assista_crise.repository;

import com.robin_courault.assista_crise.domain.SalonDiscussion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SalonDiscussionRepositoryWithBagRelationships {
    Optional<SalonDiscussion> fetchBagRelationships(Optional<SalonDiscussion> salonDiscussion);

    List<SalonDiscussion> fetchBagRelationships(List<SalonDiscussion> salonDiscussions);

    Page<SalonDiscussion> fetchBagRelationships(Page<SalonDiscussion> salonDiscussions);
}
