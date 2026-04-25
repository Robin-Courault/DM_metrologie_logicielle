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
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @NotNull
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(name = "date_inscription")
    private Instant dateInscription;

    @Column(name = "actif")
    private Boolean actif;

    @Column(name = "banni")
    private Boolean banni;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "participantses")
    @JsonIgnoreProperties(value = { "participantses", "demande" }, allowSetters = true)
    private Set<SalonDiscussion> salonses = new HashSet<>();

    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    private Sinistre sinistre;

    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    private Citoyen citoyen;

    @JsonIgnoreProperties(value = { "utilisateur", "autorite" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    private Agent agent;

    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    private Administrateur administrateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public Utilisateur login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNom() {
        return this.nom;
    }

    public Utilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Utilisateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public Utilisateur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Utilisateur telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }

    public Utilisateur motDePasse(String motDePasse) {
        this.setMotDePasse(motDePasse);
        return this;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Instant getDateInscription() {
        return this.dateInscription;
    }

    public Utilisateur dateInscription(Instant dateInscription) {
        this.setDateInscription(dateInscription);
        return this;
    }

    public void setDateInscription(Instant dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Utilisateur actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getBanni() {
        return this.banni;
    }

    public Utilisateur banni(Boolean banni) {
        this.setBanni(banni);
        return this;
    }

    public void setBanni(Boolean banni) {
        this.banni = banni;
    }

    public Set<SalonDiscussion> getSalonses() {
        return this.salonses;
    }

    public void setSalonses(Set<SalonDiscussion> salonDiscussions) {
        if (this.salonses != null) {
            this.salonses.forEach(i -> i.removeParticipants(this));
        }
        if (salonDiscussions != null) {
            salonDiscussions.forEach(i -> i.addParticipants(this));
        }
        this.salonses = salonDiscussions;
    }

    public Utilisateur salonses(Set<SalonDiscussion> salonDiscussions) {
        this.setSalonses(salonDiscussions);
        return this;
    }

    public Utilisateur addSalons(SalonDiscussion salonDiscussion) {
        this.salonses.add(salonDiscussion);
        salonDiscussion.getParticipantses().add(this);
        return this;
    }

    public Utilisateur removeSalons(SalonDiscussion salonDiscussion) {
        this.salonses.remove(salonDiscussion);
        salonDiscussion.getParticipantses().remove(this);
        return this;
    }

    public Sinistre getSinistre() {
        return this.sinistre;
    }

    public void setSinistre(Sinistre sinistre) {
        if (this.sinistre != null) {
            this.sinistre.setUtilisateur(null);
        }
        if (sinistre != null) {
            sinistre.setUtilisateur(this);
        }
        this.sinistre = sinistre;
    }

    public Utilisateur sinistre(Sinistre sinistre) {
        this.setSinistre(sinistre);
        return this;
    }

    public Citoyen getCitoyen() {
        return this.citoyen;
    }

    public void setCitoyen(Citoyen citoyen) {
        if (this.citoyen != null) {
            this.citoyen.setUtilisateur(null);
        }
        if (citoyen != null) {
            citoyen.setUtilisateur(this);
        }
        this.citoyen = citoyen;
    }

    public Utilisateur citoyen(Citoyen citoyen) {
        this.setCitoyen(citoyen);
        return this;
    }

    public Agent getAgent() {
        return this.agent;
    }

    public void setAgent(Agent agent) {
        if (this.agent != null) {
            this.agent.setUtilisateur(null);
        }
        if (agent != null) {
            agent.setUtilisateur(this);
        }
        this.agent = agent;
    }

    public Utilisateur agent(Agent agent) {
        this.setAgent(agent);
        return this;
    }

    public Administrateur getAdministrateur() {
        return this.administrateur;
    }

    public void setAdministrateur(Administrateur administrateur) {
        if (this.administrateur != null) {
            this.administrateur.setUtilisateur(null);
        }
        if (administrateur != null) {
            administrateur.setUtilisateur(this);
        }
        this.administrateur = administrateur;
    }

    public Utilisateur administrateur(Administrateur administrateur) {
        this.setAdministrateur(administrateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return getId() != null && getId().equals(((Utilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", motDePasse='" + getMotDePasse() + "'" +
            ", dateInscription='" + getDateInscription() + "'" +
            ", actif='" + getActif() + "'" +
            ", banni='" + getBanni() + "'" +
            "}";
    }
}
