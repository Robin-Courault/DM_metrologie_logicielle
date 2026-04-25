<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="annonce">
        <h2 class="jh-entity-heading" data-cy="annonceDetailsHeading">
          <span>{{ t$('assistaCriseApp.annonce.detail.title') }}Annonce</span> {{ annonce.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.annonce.titre') }}</span>
          </dt>
          <dd>
            <span>{{ annonce.titre }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.annonce.description') }}</span>
          </dt>
          <dd>
            <span>{{ annonce.description }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.annonce.categorie') }}</span>
          </dt>
          <dd>
            <span>{{ t$('assistaCriseApp.CategorieBesoin.' + annonce.categorie) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.annonce.latitude') }}</span>
          </dt>
          <dd>
            <span>{{ annonce.latitude }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.annonce.longitude') }}</span>
          </dt>
          <dd>
            <span>{{ annonce.longitude }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.annonce.adresse') }}</span>
          </dt>
          <dd>
            <span>{{ annonce.adresse }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.annonce.dateCreation') }}</span>
          </dt>
          <dd>
            <span v-if="annonce.dateCreation">{{ formatDateLong(annonce.dateCreation) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.annonce.dateMaJ') }}</span>
          </dt>
          <dd>
            <span v-if="annonce.dateMaJ">{{ formatDateLong(annonce.dateMaJ) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.annonce.etat') }}</span>
          </dt>
          <dd>
            <span>{{ t$('assistaCriseApp.EtatAnnonce.' + annonce.etat) }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="annonce.id" :to="{ name: 'AnnonceEdit', params: { annonceId: annonce.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./annonce-details.component.ts"></script>
