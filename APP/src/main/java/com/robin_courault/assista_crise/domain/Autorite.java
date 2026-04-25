package com.robin_courault.assista_crise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.robin_courault.assista_crise.domain.enumeration.TypeAutorite;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Autorite.
 */
@Entity
@Table(name = "autorite")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Autorite implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeAutorite type;

    @Column(name = "territoire")
    private String territoire;

    @Column(name = "contact")
    private String contact;

    /**
     * Une autorité déclare plusieurs crises
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "autorite")
    @JsonIgnoreProperties(value = { "autorite" }, allowSetters = true)
    private Set<Crise> criseses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Autorite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Autorite nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TypeAutorite getType() {
        return this.type;
    }

    public Autorite type(TypeAutorite type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeAutorite type) {
        this.type = type;
    }

    public String getTerritoire() {
        return this.territoire;
    }

    public Autorite territoire(String territoire) {
        this.setTerritoire(territoire);
        return this;
    }

    public void setTerritoire(String territoire) {
        this.territoire = territoire;
    }

    public String getContact() {
        return this.contact;
    }

    public Autorite contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Set<Crise> getCriseses() {
        return this.criseses;
    }

    public void setCriseses(Set<Crise> crises) {
        if (this.criseses != null) {
            this.criseses.forEach(i -> i.setAutorite(null));
        }
        if (crises != null) {
            crises.forEach(i -> i.setAutorite(this));
        }
        this.criseses = crises;
    }

    public Autorite criseses(Set<Crise> crises) {
        this.setCriseses(crises);
        return this;
    }

    public Autorite addCrises(Crise crise) {
        this.criseses.add(crise);
        crise.setAutorite(this);
        return this;
    }

    public Autorite removeCrises(Crise crise) {
        this.criseses.remove(crise);
        crise.setAutorite(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Autorite)) {
            return false;
        }
        return getId() != null && getId().equals(((Autorite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Autorite{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", type='" + getType() + "'" +
            ", territoire='" + getTerritoire() + "'" +
            ", contact='" + getContact() + "'" +
            "}";
    }
}
