package com.robin_courault.assista_crise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * A Agent.
 */
@Entity
@Table(name = "agent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Agent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fonction")
    private String fonction;

    @Column(name = "service")
    private String service;

    @JsonIgnoreProperties(value = { "salonses", "sinistre", "citoyen", "agent", "administrateur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Utilisateur utilisateur;

    /**
     * Un agent est rattaché à une autorité
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "criseses" }, allowSetters = true)
    private Autorite autorite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Agent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFonction() {
        return this.fonction;
    }

    public Agent fonction(String fonction) {
        this.setFonction(fonction);
        return this;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getService() {
        return this.service;
    }

    public Agent service(String service) {
        this.setService(service);
        return this;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Agent utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public Autorite getAutorite() {
        return this.autorite;
    }

    public void setAutorite(Autorite autorite) {
        this.autorite = autorite;
    }

    public Agent autorite(Autorite autorite) {
        this.setAutorite(autorite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agent)) {
            return false;
        }
        return getId() != null && getId().equals(((Agent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agent{" +
            "id=" + getId() +
            ", fonction='" + getFonction() + "'" +
            ", service='" + getService() + "'" +
            "}";
    }
}
