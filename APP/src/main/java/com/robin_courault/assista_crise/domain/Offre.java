package com.robin_courault.assista_crise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Offre.
 */
@Entity
@Table(name = "offre")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Offre implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "disponible_de")
    private Instant disponibleDe;

    @Column(name = "disponible_jusqua")
    private Instant disponibleJusqua;

    @Column(name = "quantite")
    private Integer quantite;

    @JsonIgnoreProperties(value = { "demande", "offre" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Annonce annonce;

    /**
     * Une offre est proposée par un citoyen
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    private Citoyen citoyen;

    /**
     * Une offre concerne une crise
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "autorite" }, allowSetters = true)
    private Crise crise;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "offreses")
    @JsonIgnoreProperties(value = { "annonce", "salonDiscussion", "sinistre", "crise", "offreses" }, allowSetters = true)
    private Set<Demande> demandeses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Offre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDisponibleDe() {
        return this.disponibleDe;
    }

    public Offre disponibleDe(Instant disponibleDe) {
        this.setDisponibleDe(disponibleDe);
        return this;
    }

    public void setDisponibleDe(Instant disponibleDe) {
        this.disponibleDe = disponibleDe;
    }

    public Instant getDisponibleJusqua() {
        return this.disponibleJusqua;
    }

    public Offre disponibleJusqua(Instant disponibleJusqua) {
        this.setDisponibleJusqua(disponibleJusqua);
        return this;
    }

    public void setDisponibleJusqua(Instant disponibleJusqua) {
        this.disponibleJusqua = disponibleJusqua;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public Offre quantite(Integer quantite) {
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

    public Offre annonce(Annonce annonce) {
        this.setAnnonce(annonce);
        return this;
    }

    public Citoyen getCitoyen() {
        return this.citoyen;
    }

    public void setCitoyen(Citoyen citoyen) {
        this.citoyen = citoyen;
    }

    public Offre citoyen(Citoyen citoyen) {
        this.setCitoyen(citoyen);
        return this;
    }

    public Crise getCrise() {
        return this.crise;
    }

    public void setCrise(Crise crise) {
        this.crise = crise;
    }

    public Offre crise(Crise crise) {
        this.setCrise(crise);
        return this;
    }

    public Set<Demande> getDemandeses() {
        return this.demandeses;
    }

    public void setDemandeses(Set<Demande> demandes) {
        if (this.demandeses != null) {
            this.demandeses.forEach(i -> i.removeOffres(this));
        }
        if (demandes != null) {
            demandes.forEach(i -> i.addOffres(this));
        }
        this.demandeses = demandes;
    }

    public Offre demandeses(Set<Demande> demandes) {
        this.setDemandeses(demandes);
        return this;
    }

    public Offre addDemandes(Demande demande) {
        this.demandeses.add(demande);
        demande.getOffreses().add(this);
        return this;
    }

    public Offre removeDemandes(Demande demande) {
        this.demandeses.remove(demande);
        demande.getOffreses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Offre)) {
            return false;
        }
        return getId() != null && getId().equals(((Offre) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Offre{" +
            "id=" + getId() +
            ", disponibleDe='" + getDisponibleDe() + "'" +
            ", disponibleJusqua='" + getDisponibleJusqua() + "'" +
            ", quantite=" + getQuantite() +
            "}";
    }
}
