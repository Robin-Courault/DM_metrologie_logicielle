package com.robin_courault.assista_crise.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.robin_courault.assista_crise.domain.Sinistre} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SinistreDTO implements Serializable {

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
        if (!(o instanceof SinistreDTO)) {
            return false;
        }

        SinistreDTO sinistreDTO = (SinistreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sinistreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SinistreDTO{" +
            "id=" + getId() +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
