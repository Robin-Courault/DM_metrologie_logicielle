package com.robin_courault.assista_crise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.robin_courault.assista_crise.domain.enumeration.EtatDemande;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Demande.
 */
@Entity
@Table(name = "demande")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Demande implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_demande")
    private EtatDemande etatDemande;

    @Column(name = "date_fermeture")
    private Instant dateFermeture;

    @Column(name = "quantite")
    private Integer quantite;

    @JsonIgnoreProperties(value = { "demande", "offre" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Annonce annonce;

    /**
     * Une demande discute via un salon de discussion
     */
    @JsonIgnoreProperties(value = { "participantses", "demande" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private SalonDiscussion salonDiscussion;

    /**
     * Une demande est publiée par un sinistré
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    private Sinistre sinistre;

    /**
     * Une demande concerne une crise
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "autorite" }, allowSetters = true)
    private Crise crise;

    /**
     * Une demande peut être couverte par plusieurs offres et vice-versa
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_demande__offres",
        joinColumns = @JoinColumn(name = "demande_id"),
        inverseJoinColumns = @JoinColumn(name = "offres_id")
    )
    @JsonIgnoreProperties(value = { "annonce", "citoyen", "crise", "demandeses" }, allowSetters = true)
    private Set<Offre> offreses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Demande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EtatDemande getEtatDemande() {
        return this.etatDemande;
    }

    public Demande etatDemande(EtatDemande etatDemande) {
        this.setEtatDemande(etatDemande);
        return this;
    }

    public void setEtatDemande(EtatDemande etatDemande) {
        this.etatDemande = etatDemande;
    }

    public Instant getDateFermeture() {
        return this.dateFermeture;
    }

    public Demande dateFermeture(Instant dateFermeture) {
        this.setDateFermeture(dateFermeture);
        return this;
    }

    public void setDateFermeture(Instant dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public Demande quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Annonce getAnnonce() {
        return this.annonce;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public Demande annonce(Annonce annonce) {
        this.setAnnonce(annonce);
        return this;
    }

    public SalonDiscussion getSalonDiscussion() {
        return this.salonDiscussion;
    }

    public void setSalonDiscussion(SalonDiscussion salonDiscussion) {
        this.salonDiscussion = salonDiscussion;
    }

    public Demande salonDiscussion(SalonDiscussion salonDiscussion) {
        this.setSalonDiscussion(salonDiscussion);
        return this;
    }

    public Sinistre getSinistre() {
        return this.sinistre;
    }

    public void setSinistre(Sinistre sinistre) {
        this.sinistre = sinistre;
    }

    public Demande sinistre(Sinistre sinistre) {
        this.setSinistre(sinistre);
        return this;
    }

    public Crise getCrise() {
        return this.crise;
    }

    public void setCrise(Crise crise) {
        this.crise = crise;
    }

    public Demande crise(Crise crise) {
        this.setCrise(crise);
        return this;
    }

    public Set<Offre> getOffreses() {
        return this.offreses;
    }

    public void setOffreses(Set<Offre> offres) {
        this.offreses = offres;
    }

    public Demande offreses(Set<Offre> offres) {
        this.setOffreses(offres);
        return this;
    }

    public Demande addOffres(Offre offre) {
        this.offreses.add(offre);
        return this;
    }

    public Demande removeOffres(Offre offre) {
        this.offreses.remove(offre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Demande)) {
            return false;
        }
        return getId() != null && getId().equals(((Demande) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Demande{" +
            "id=" + getId() +
            ", etatDemande='" + getEtatDemande() + "'" +
            ", dateFermeture='" + getDateFermeture() + "'" +
            ", quantite=" + getQuantite() +
            "}";
    }
}
