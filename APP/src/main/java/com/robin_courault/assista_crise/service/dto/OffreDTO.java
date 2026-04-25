package com.robin_courault.assista_crise.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.robin_courault.assista_crise.domain.Offre} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OffreDTO implements Serializable {

    private Long id;

    private Instant disponibleDe;

    private Instant disponibleJusqua;

    private Integer quantite;

    @NotNull
    private AnnonceDTO annonce;

    @NotNull
    @Schema(description = "Une offre est proposée par un citoyen")
    private CitoyenDTO citoyen;

    @NotNull
    @Schema(description = "Une offre concerne une crise")
    private CriseDTO crise;

    private Set<DemandeDTO> demandeses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDisponibleDe() {
        return disponibleDe;
    }

    public void setDisponibleDe(Instant disponibleDe) {
        this.disponibleDe = disponibleDe;
    }

    public Instant getDisponibleJusqua() {
        return disponibleJusqua;
    }

    public void setDisponibleJusqua(Instant disponibleJusqua) {
        this.disponibleJusqua = disponibleJusqua;
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

    public CitoyenDTO getCitoyen() {
        return citoyen;
    }

    public void setCitoyen(CitoyenDTO citoyen) {
        this.citoyen = citoyen;
    }

    public CriseDTO getCrise() {
        return crise;
    }

    public void setCrise(CriseDTO crise) {
        this.crise = crise;
    }

    public Set<DemandeDTO> getDemandeses() {
        return demandeses;
    }

    public void setDemandeses(Set<DemandeDTO> demandeses) {
        this.demandeses = demandeses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OffreDTO)) {
            return false;
        }

        OffreDTO offreDTO = (OffreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, offreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OffreDTO{" +
            "id=" + getId() +
            ", disponibleDe='" + getDisponibleDe() + "'" +
            ", disponibleJusqua='" + getDisponibleJusqua() + "'" +
            ", quantite=" + getQuantite() +
            ", annonce=" + getAnnonce() +
            ", citoyen=" + getCitoyen() +
            ", crise=" + getCrise() +
            ", demandeses=" + getDemandeses() +
            "}";
    }
}
