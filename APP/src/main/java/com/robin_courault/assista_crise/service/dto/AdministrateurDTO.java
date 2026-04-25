package com.robin_courault.assista_crise.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.robin_courault.assista_crise.domain.Administrateur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdministrateurDTO implements Serializable {

    private Long id;

    @NotNull
    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdministrateurDTO)) {
            return false;
        }

        AdministrateurDTO administrateurDTO = (AdministrateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, administrateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdministrateurDTO{" +
            "id=" + getId() +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
