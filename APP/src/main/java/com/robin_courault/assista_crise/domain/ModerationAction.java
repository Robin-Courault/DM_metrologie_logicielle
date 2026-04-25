package com.robin_courault.assista_crise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.robin_courault.assista_crise.domain.enumeration.TypeModeration;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ModerationAction.
 */
@Entity
@Table(name = "moderation_action")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModerationAction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_action", nullable = false)
    private Instant dateAction;

    @Column(name = "motif")
    private String motif;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeModeration type;

    /**
     * Une modération est effectuée par un administrateur
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    private Administrateur administrateur;

    /**
     * Une action de modération peut cibler une annonce
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "demande", "offre" }, allowSetters = true)
    private Annonce annonce;

    /**
     * Une action de modération peut cibler un utilisateur
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "salonses", "sinistre", "citoyen", "agent", "administrateur" }, allowSetters = true)
    private Utilisateur utilisateurCible;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ModerationAction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateAction() {
        return this.dateAction;
    }

    public ModerationAction dateAction(Instant dateAction) {
        this.setDateAction(dateAction);
        return this;
    }

    public void setDateAction(Instant dateAction) {
        this.dateAction = dateAction;
    }

    public String getMotif() {
        return this.motif;
    }

    public ModerationAction motif(String motif) {
        this.setMotif(motif);
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public TypeModeration getType() {
        return this.type;
    }

    public ModerationAction type(TypeModeration type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeModeration type) {
        this.type = type;
    }

    public Administrateur getAdministrateur() {
        return this.administrateur;
    }

    public void setAdministrateur(Administrateur administrateur) {
        this.administrateur = administrateur;
    }

    public ModerationAction administrateur(Administrateur administrateur) {
        this.setAdministrateur(administrateur);
        return this;
    }

    public Annonce getAnnonce() {
        return this.annonce;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public ModerationAction annonce(Annonce annonce) {
        this.setAnnonce(annonce);
        return this;
    }

    public Utilisateur getUtilisateurCible() {
        return this.utilisateurCible;
    }

    public void setUtilisateurCible(Utilisateur utilisateur) {
        this.utilisateurCible = utilisateur;
    }

    public ModerationAction utilisateurCible(Utilisateur utilisateur) {
        this.setUtilisateurCible(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModerationAction)) {
            return false;
        }
        return getId() != null && getId().equals(((ModerationAction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModerationAction{" +
            "id=" + getId() +
            ", dateAction='" + getDateAction() + "'" +
            ", motif='" + getMotif() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
