package com.robin_courault.assista_crise.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.robin_courault.assista_crise.domain.Message} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageDTO implements Serializable {

    private Long id;

    @NotNull
    private String contenu;

    @NotNull
    private Instant dateEnvoi;

    @NotNull
    @Schema(description = "Un message est écrit par un utilisateur")
    private UtilisateurDTO utilisateur;

    @NotNull
    @Schema(description = "Un message appartient à un salon")
    private SalonDiscussionDTO salonDiscussion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Instant getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Instant dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    public SalonDiscussionDTO getSalonDiscussion() {
        return salonDiscussion;
    }

    public void setSalonDiscussion(SalonDiscussionDTO salonDiscussion) {
        this.salonDiscussion = salonDiscussion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageDTO)) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + getId() +
            ", contenu='" + getContenu() + "'" +
            ", dateEnvoi='" + getDateEnvoi() + "'" +
            ", utilisateur=" + getUtilisateur() +
            ", salonDiscussion=" + getSalonDiscussion() +
            "}";
    }
}
