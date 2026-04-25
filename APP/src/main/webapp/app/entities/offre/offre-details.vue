<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="offre">
        <h2 class="jh-entity-heading" data-cy="offreDetailsHeading">
          <span>{{ t$('assistaCriseApp.offre.detail.title') }}Offre</span> {{ offre.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.offre.disponibleDe') }}</span>
          </dt>
          <dd>
            <span v-if="offre.disponibleDe">{{ formatDateLong(offre.disponibleDe) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.offre.disponibleJusqua') }}</span>
          </dt>
          <dd>
            <span v-if="offre.disponibleJusqua">{{ formatDateLong(offre.disponibleJusqua) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.offre.quantite') }}</span>
          </dt>
          <dd>
            <span>{{ offre.quantite }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.offre.annonce') }}</span>
          </dt>
          <dd>
            <div v-if="offre.annonce">
              <router-link :to="{ name: 'AnnonceView', params: { annonceId: offre.annonce.id } }">{{ offre.annonce.titre }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.offre.citoyen') }}</span>
          </dt>
          <dd>
            <div v-if="offre.citoyen">
              <router-link :to="{ name: 'CitoyenView', params: { citoyenId: offre.citoyen.id } }">{{ offre.citoyen.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.offre.crise') }}</span>
          </dt>
          <dd>
            <div v-if="offre.crise">
              <router-link :to="{ name: 'CriseView', params: { criseId: offre.crise.id } }">{{ offre.crise.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.offre.demandes') }}</span>
          </dt>
          <dd>
            <span v-for="(demandes, i) in offre.demandeses" :key="demandes.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'DemandeView', params: { demandeId: demandes.id } }">{{ demandes.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="offre.id" :to="{ name: 'OffreEdit', params: { offreId: offre.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./offre-details.component.ts"></script>
