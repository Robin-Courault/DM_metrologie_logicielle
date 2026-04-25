package com.robin_courault.assista_crise.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.robin_courault.assista_crise.domain.Agent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgentDTO implements Serializable {

    private Long id;

    private String fonction;

    private String service;

    @NotNull
    private UtilisateurDTO utilisateur;

    @NotNull
    @Schema(description = "Un agent est rattaché à une autorité")
    private AutoriteDTO autorite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    public AutoriteDTO getAutorite() {
        return autorite;
    }

    public void setAutorite(AutoriteDTO autorite) {
        this.autorite = autorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentDTO)) {
            return false;
        }

        AgentDTO agentDTO = (AgentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgentDTO{" +
            "id=" + getId() +
            ", fonction='" + getFonction() + "'" +
            ", service='" + getService() + "'" +
            ", utilisateur=" + getUtilisateur() +
            ", autorite=" + getAutorite() +
            "}";
    }
}
