package com.robin_courault.assista_crise.service.dto;

import com.robin_courault.assista_crise.domain.enumeration.TypeModeration;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.robin_courault.assista_crise.domain.ModerationAction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModerationActionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateAction;

    private String motif;

    @NotNull
    private TypeModeration type;

    @NotNull
    @Schema(description = "Une modération est effectuée par un administrateur")
    private AdministrateurDTO administrateur;

    @Schema(description = "Une action de modération peut cibler une annonce")
    private AnnonceDTO annonce;

    @Schema(description = "Une action de modération peut cibler un utilisateur")
    private UtilisateurDTO utilisateurCible;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateAction() {
        return dateAction;
    }

    public void setDateAction(Instant dateAction) {
        this.dateAction = dateAction;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public TypeModeration getType() {
        return type;
    }

    public void setType(TypeModeration type) {
        this.type = type;
    }

    public AdministrateurDTO getAdministrateur() {
        return administrateur;
    }

    public void setAdministrateur(AdministrateurDTO administrateur) {
        this.administrateur = administrateur;
    }

    public AnnonceDTO getAnnonce() {
        return annonce;
    }

    public void setAnnonce(AnnonceDTO annonce) {
        this.annonce = annonce;
    }

    public UtilisateurDTO getUtilisateurCible() {
        return utilisateurCible;
    }

    public void setUtilisateurCible(UtilisateurDTO utilisateurCible) {
        this.utilisateurCible = utilisateurCible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModerationActionDTO)) {
            return false;
        }

        ModerationActionDTO moderationActionDTO = (ModerationActionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moderationActionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModerationActionDTO{" +
            "id=" + getId() +
            ", dateAction='" + getDateAction() + "'" +
            ", motif='" + getMotif() + "'" +
            ", type='" + getType() + "'" +
            ", administrateur=" + getAdministrateur() +
            ", annonce=" + getAnnonce() +
            ", utilisateurCible=" + getUtilisateurCible() +
            "}";
    }
}
