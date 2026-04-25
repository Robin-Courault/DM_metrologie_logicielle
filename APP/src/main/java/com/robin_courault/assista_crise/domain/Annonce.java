package com.robin_courault.assista_crise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.robin_courault.assista_crise.domain.enumeration.CategorieBesoin;
import com.robin_courault.assista_crise.domain.enumeration.EtatAnnonce;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Annonce.
 */
@Entity
@Table(name = "annonce")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Annonce implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "categorie", nullable = false)
    private CategorieBesoin categorie;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @Column(name = "date_ma_j")
    private Instant dateMaJ;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatAnnonce etat;

    @JsonIgnoreProperties(value = { "annonce", "salonDiscussion", "sinistre", "crise", "offreses" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "annonce")
    private Demande demande;

    @JsonIgnoreProperties(value = { "annonce", "citoyen", "crise", "demandeses" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "annonce")
    private Offre offre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Annonce id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Annonce titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public Annonce description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategorieBesoin getCategorie() {
        return this.categorie;
    }

    public Annonce categorie(CategorieBesoin categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(CategorieBesoin categorie) {
        this.categorie = categorie;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Annonce latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Annonce longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Annonce adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Annonce dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateMaJ() {
        return this.dateMaJ;
    }

    public Annonce dateMaJ(Instant dateMaJ) {
        this.setDateMaJ(dateMaJ);
        return this;
    }

    public void setDateMaJ(Instant dateMaJ) {
        this.dateMaJ = dateMaJ;
    }

    public EtatAnnonce getEtat() {
        return this.etat;
    }

    public Annonce etat(EtatAnnonce etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(EtatAnnonce etat) {
        this.etat = etat;
    }

    public Demande getDemande() {
        return this.demande;
    }

    public void setDemande(Demande demande) {
        if (this.demande != null) {
            this.demande.setAnnonce(null);
        }
        if (demande != null) {
            demande.setAnnonce(this);
        }
        this.demande = demande;
    }

    public Annonce demande(Demande demande) {
        this.setDemande(demande);
        return this;
    }

    public Offre getOffre() {
        return this.offre;
    }

    public void setOffre(Offre offre) {
        if (this.offre != null) {
            this.offre.setAnnonce(null);
        }
        if (offre != null) {
            offre.setAnnonce(this);
        }
        this.offre = offre;
    }

    public Annonce offre(Offre offre) {
        this.setOffre(offre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Annonce)) {
            return false;
        }
        return getId() != null && getId().equals(((Annonce) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Annonce{" +
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
