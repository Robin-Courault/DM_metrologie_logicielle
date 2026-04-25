package com.robin_courault.assista_crise.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.robin_courault.assista_crise.domain.SalonDiscussion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalonDiscussionDTO implements Serializable {

    private Long id;

    private Instant dateOuverture;

    private Boolean ouvert;

    @Schema(description = "Un salon de discussion a plusieurs participants")
    private Set<UtilisateurDTO> participantses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Instant dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Boolean getOuvert() {
        return ouvert;
    }

    public void setOuvert(Boolean ouvert) {
        this.ouvert = ouvert;
    }

    public Set<UtilisateurDTO> getParticipantses() {
        return participantses;
    }

    public void setParticipantses(Set<UtilisateurDTO> participantses) {
        this.participantses = participantses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalonDiscussionDTO)) {
            return false;
        }

        SalonDiscussionDTO salonDiscussionDTO = (SalonDiscussionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, salonDiscussionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalonDiscussionDTO{" +
            "id=" + getId() +
            ", dateOuverture='" + getDateOuverture() + "'" +
            ", ouvert='" + getOuvert() + "'" +
            ", participantses=" + getParticipantses() +
            "}";
    }
}
