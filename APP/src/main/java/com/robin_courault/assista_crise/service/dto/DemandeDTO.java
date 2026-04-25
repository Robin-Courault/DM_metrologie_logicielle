package com.robin_courault.assista_crise.service.dto;

import com.robin_courault.assista_crise.domain.enumeration.EtatDemande;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.robin_courault.assista_crise.domain.Demande} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DemandeDTO implements Serializable {

    private Long id;

    private EtatDemande etatDemande;

    private Instant dateFermeture;

    private Integer quantite;

    @NotNull
    private AnnonceDTO annonce;

    @Schema(description = "Une demande discute via un salon de discussion")
    private SalonDiscussionDTO salonDiscussion;

    @NotNull
    @Schema(description = "Une demande est publiée par un sinistré")
    private SinistreDTO sinistre;

    @NotNull
    @Schema(description = "Une demande concerne une crise")
    private CriseDTO crise;

    @Schema(description = "Une demande peut être couverte par plusieurs offres et vice-versa")
    private Set<OffreDTO> offreses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EtatDemande getEtatDemande() {
        return etatDemande;
    }

    public void setEtatDemande(EtatDemande etatDemande) {
        this.etatDemande = etatDemande;
    }

    public Instant getDateFermeture() {
        return dateFermeture;
    }

    public void setDateFermeture(Instant dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public AnnonceDTO getAnnonce() {
        return annonce;
    }

    public void setAnnonce(AnnonceDTO annonce) {
        this.annonce = annonce;
    }

    public SalonDiscussionDTO getSalonDiscussion() {
        return salonDiscussion;
    }

    public void setSalonDiscussion(SalonDiscussionDTO salonDiscussion) {
        this.salonDiscussion = salonDiscussion;
    }

    public SinistreDTO getSinistre() {
        return sinistre;
    }

    public void setSinistre(SinistreDTO sinistre) {
        this.sinistre = sinistre;
    }

    public CriseDTO getCrise() {
        return crise;
    }

    public void setCrise(CriseDTO crise) {
        this.crise = crise;
    }

    public Set<OffreDTO> getOffreses() {
        return offreses;
    }

    public void setOffreses(Set<OffreDTO> offreses) {
        this.offreses = offreses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeDTO)) {
            return false;
        }

        DemandeDTO demandeDTO = (DemandeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, demandeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeDTO{" +
            "id=" + getId() +
            ", etatDemande='" + getEtatDemande() + "'" +
            ", dateFermeture='" + getDateFermeture() + "'" +
            ", quantite=" + getQuantite() +
            ", annonce=" + getAnnonce() +
            ", salonDiscussion=" + getSalonDiscussion() +
            ", sinistre=" + getSinistre() +
            ", crise=" + getCrise() +
            ", offreses=" + getOffreses() +
            "}";
    }
}
