package com.robin_courault.assista_crise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A SalonDiscussion.
 */
@Entity
@Table(name = "salon_discussion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalonDiscussion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_ouverture")
    private Instant dateOuverture;

    @Column(name = "ouvert")
    private Boolean ouvert;

    /**
     * Un salon de discussion a plusieurs participants
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_salon_discussion__participants",
        joinColumns = @JoinColumn(name = "salon_discussion_id"),
        inverseJoinColumns = @JoinColumn(name = "participants_id")
    )
    @JsonIgnoreProperties(value = { "salonses", "sinistre", "citoyen", "agent", "administrateur" }, allowSetters = true)
    private Set<Utilisateur> participantses = new HashSet<>();

    @JsonIgnoreProperties(value = { "annonce", "salonDiscussion", "sinistre", "crise", "offreses" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "salonDiscussion")
    private Demande demande;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SalonDiscussion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateOuverture() {
        return this.dateOuverture;
    }

    public SalonDiscussion dateOuverture(Instant dateOuverture) {
        this.setDateOuverture(dateOuverture);
        return this;
    }

    public void setDateOuverture(Instant dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Boolean getOuvert() {
        return this.ouvert;
    }

    public SalonDiscussion ouvert(Boolean ouvert) {
        this.setOuvert(ouvert);
        return this;
    }

    public void setOuvert(Boolean ouvert) {
        this.ouvert = ouvert;
    }

    public Set<Utilisateur> getParticipantses() {
        return this.participantses;
    }

    public void setParticipantses(Set<Utilisateur> utilisateurs) {
        this.participantses = utilisateurs;
    }

    public SalonDiscussion participantses(Set<Utilisateur> utilisateurs) {
        this.setParticipantses(utilisateurs);
        return this;
    }

    public SalonDiscussion addParticipants(Utilisateur utilisateur) {
        this.participantses.add(utilisateur);
        return this;
    }

    public SalonDiscussion removeParticipants(Utilisateur utilisateur) {
        this.participantses.remove(utilisateur);
        return this;
    }

    public Demande getDemande() {
        return this.demande;
    }

    public void setDemande(Demande demande) {
        if (this.demande != null) {
            this.demande.setSalonDiscussion(null);
        }
        if (demande != null) {
            demande.setSalonDiscussion(this);
        }
        this.demande = demande;
    }

    public SalonDiscussion demande(Demande demande) {
        this.setDemande(demande);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalonDiscussion)) {
            return false;
        }
        return getId() != null && getId().equals(((SalonDiscussion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalonDiscussion{" +
            "id=" + getId() +
            ", dateOuverture='" + getDateOuverture() + "'" +
            ", ouvert='" + getOuvert() + "'" +
            "}";
    }
}
