package com.robin_courault.assista_crise.service.dto;

import com.robin_courault.assista_crise.domain.enumeration.TypeAutorite;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.robin_courault.assista_crise.domain.Autorite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AutoriteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    @NotNull
    private TypeAutorite type;

    private String territoire;

    private String contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TypeAutorite getType() {
        return type;
    }

    public void setType(TypeAutorite type) {
        this.type = type;
    }

    public String getTerritoire() {
        return territoire;
    }

    public void setTerritoire(String territoire) {
        this.territoire = territoire;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutoriteDTO)) {
            return false;
        }

        AutoriteDTO autoriteDTO = (AutoriteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, autoriteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AutoriteDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", territoire='" + getTerritoire() + "'" +
            ", contact='" + getContact() + "'" +
            "}";
    }
}
