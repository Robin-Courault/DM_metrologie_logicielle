package com.robin_courault.assista_crise.service.dto;

import com.robin_courault.assista_crise.domain.enumeration.CategorieBesoin;
import com.robin_courault.assista_crise.domain.enumeration.EtatAnnonce;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.robin_courault.assista_crise.domain.Annonce} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnnonceDTO implements Serializable {

    private Long id;

    @NotNull
    private String titre;

    private String description;

    @NotNull
    private CategorieBesoin categorie;

    private Double latitude;

    private Double longitude;

    private String adresse;

    private Instant dateCreation;

    private Instant dateMaJ;

    private EtatAnnonce etat;

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

    public CategorieBesoin getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieBesoin categorie) {
        this.categorie = categorie;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateMaJ() {
        return dateMaJ;
    }

    public void setDateMaJ(Instant dateMaJ) {
        this.dateMaJ = dateMaJ;
    }

    public EtatAnnonce getEtat() {
        return etat;
    }

    public void setEtat(EtatAnnonce etat) {
        this.etat = etat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnonceDTO)) {
            return false;
        }

        AnnonceDTO annonceDTO = (AnnonceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, annonceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnonceDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", categorie='" + getCategorie() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", adresse='" + getAdresse() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateMaJ='" + getDateMaJ() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
