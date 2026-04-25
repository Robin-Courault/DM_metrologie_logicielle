<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="demande">
        <h2 class="jh-entity-heading" data-cy="demandeDetailsHeading">
          <span>{{ t$('assistaCriseApp.demande.detail.title') }}Demande</span> {{ demande.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.demande.etatDemande') }}</span>
          </dt>
          <dd>
            <span>{{ t$('assistaCriseApp.EtatDemande.' + demande.etatDemande) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.demande.dateFermeture') }}</span>
          </dt>
          <dd>
            <span v-if="demande.dateFermeture">{{ formatDateLong(demande.dateFermeture) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.demande.quantite') }}</span>
          </dt>
          <dd>
            <span>{{ demande.quantite }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.demande.annonce') }}</span>
          </dt>
          <dd>
            <div v-if="demande.annonce">
              <router-link :to="{ name: 'AnnonceView', params: { annonceId: demande.annonce.id } }">{{
                demande.annonce.titre
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.demande.salonDiscussion') }}</span>
          </dt>
          <dd>
            <div v-if="demande.salonDiscussion">
              <router-link :to="{ name: 'SalonDiscussionView', params: { salonDiscussionId: demande.salonDiscussion.id } }">{{
                demande.salonDiscussion.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.demande.sinistre') }}</span>
          </dt>
          <dd>
            <div v-if="demande.sinistre">
              <router-link :to="{ name: 'SinistreView', params: { sinistreId: demande.sinistre.id } }">{{
                demande.sinistre.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.demande.crise') }}</span>
          </dt>
          <dd>
            <div v-if="demande.crise">
              <router-link :to="{ name: 'CriseView', params: { criseId: demande.crise.id } }">{{ demande.crise.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.demande.offres') }}</span>
          </dt>
          <dd>
            <span v-for="(offres, i) in demande.offreses" :key="offres.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'OffreView', params: { offreId: offres.id } }">{{ offres.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="demande.id" :to="{ name: 'DemandeEdit', params: { demandeId: demande.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./demande-details.component.ts"></script>
