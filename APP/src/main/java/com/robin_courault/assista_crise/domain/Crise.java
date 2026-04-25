package com.robin_courault.assista_crise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.robin_courault.assista_crise.domain.enumeration.TypeCrise;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Crise.
 */
@Entity
@Table(name = "crise")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Crise implements Serializable {

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
    @Column(name = "type", nullable = false)
    private TypeCrise type;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @Column(name = "date_fin")
    private Instant dateFin;

    @Column(name = "zone_geographique")
    private String zoneGeographique;

    @Column(name = "cloturee")
    private Boolean cloturee;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "criseses" }, allowSetters = true)
    private Autorite autorite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Crise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Crise titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public Crise description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeCrise getType() {
        return this.type;
    }

    public Crise type(TypeCrise type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeCrise type) {
        this.type = type;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public Crise dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return this.dateFin;
    }

    public Crise dateFin(Instant dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public String getZoneGeographique() {
        return this.zoneGeographique;
    }

    public Crise zoneGeographique(String zoneGeographique) {
        this.setZoneGeographique(zoneGeographique);
        return this;
    }

    public void setZoneGeographique(String zoneGeographique) {
        this.zoneGeographique = zoneGeographique;
    }

    public Boolean getCloturee() {
        return this.cloturee;
    }

    public Crise cloturee(Boolean cloturee) {
        this.setCloturee(cloturee);
        return this;
    }

    public void setCloturee(Boolean cloturee) {
        this.cloturee = cloturee;
    }

    public Autorite getAutorite() {
        return this.autorite;
    }

    public void setAutorite(Autorite autorite) {
        this.autorite = autorite;
    }

    public Crise autorite(Autorite autorite) {
        this.setAutorite(autorite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Crise)) {
            return false;
        }
        return getId() != null && getId().equals(((Crise) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Crise{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", zoneGeographique='" + getZoneGeographique() + "'" +
            ", cloturee='" + getCloturee() + "'" +
            "}";
    }
}
