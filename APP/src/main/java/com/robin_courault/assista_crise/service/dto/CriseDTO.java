package com.robin_courault.assista_crise.service.dto;

import com.robin_courault.assista_crise.domain.enumeration.TypeCrise;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.robin_courault.assista_crise.domain.Crise} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CriseDTO implements Serializable {

    private Long id;

    @NotNull
    private String titre;

    private String description;

    @NotNull
    private TypeCrise type;

    @NotNull
    private Instant dateDebut;

    private Instant dateFin;

    private String zoneGeographique;

    private Boolean cloturee;

    @NotNull
    private AutoriteDTO autorite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeCrise getType() {
        return type;
    }

    public void setType(TypeCrise type) {
        this.type = type;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public String getZoneGeographique() {
        return zoneGeographique;
    }

    public void setZoneGeographique(String zoneGeographique) {
        this.zoneGeographique = zoneGeographique;
    }

    public Boolean getCloturee() {
        return cloturee;
    }

    public void setCloturee(Boolean cloturee) {
        this.cloturee = cloturee;
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
        if (!(o instanceof CriseDTO)) {
            return false;
        }

        CriseDTO criseDTO = (CriseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, criseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CriseDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", zoneGeographique='" + getZoneGeographique() + "'" +
            ", cloturee='" + getCloturee() + "'" +
            ", autorite=" + getAutorite() +
            "}";
    }
}
