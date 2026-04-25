package com.robin_courault.assista_crise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "contenu", nullable = false)
    private String contenu;

    @NotNull
    @Column(name = "date_envoi", nullable = false)
    private Instant dateEnvoi;

    /**
     * Un message est écrit par un utilisateur
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "salonses", "sinistre", "citoyen", "agent", "administrateur" }, allowSetters = true)
    private Utilisateur utilisateur;

    /**
     * Un message appartient à un salon
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "participantses", "demande" }, allowSetters = true)
    private SalonDiscussion salonDiscussion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Message id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return this.contenu;
    }

    public Message contenu(String contenu) {
        this.setContenu(contenu);
        return this;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Instant getDateEnvoi() {
        return this.dateEnvoi;
    }

    public Message dateEnvoi(Instant dateEnvoi) {
        this.setDateEnvoi(dateEnvoi);
        return this;
    }

    public void setDateEnvoi(Instant dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Message utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public SalonDiscussion getSalonDiscussion() {
        return this.salonDiscussion;
    }

    public void setSalonDiscussion(SalonDiscussion salonDiscussion) {
        this.salonDiscussion = salonDiscussion;
    }

    public Message salonDiscussion(SalonDiscussion salonDiscussion) {
        this.setSalonDiscussion(salonDiscussion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return getId() != null && getId().equals(((Message) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", contenu='" + getContenu() + "'" +
            ", dateEnvoi='" + getDateEnvoi() + "'" +
            "}";
    }
}
