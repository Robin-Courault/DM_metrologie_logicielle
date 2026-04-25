<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="crise">
        <h2 class="jh-entity-heading" data-cy="criseDetailsHeading">
          <span>{{ t$('assistaCriseApp.crise.detail.title') }}Crise</span> {{ crise.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.crise.titre') }}</span>
          </dt>
          <dd>
            <span>{{ crise.titre }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.crise.description') }}</span>
          </dt>
          <dd>
            <span>{{ crise.description }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.crise.type') }}</span>
          </dt>
          <dd>
            <span>{{ t$('assistaCriseApp.TypeCrise.' + crise.type) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.crise.dateDebut') }}</span>
          </dt>
          <dd>
            <span v-if="crise.dateDebut">{{ formatDateLong(crise.dateDebut) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.crise.dateFin') }}</span>
          </dt>
          <dd>
            <span v-if="crise.dateFin">{{ formatDateLong(crise.dateFin) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.crise.zoneGeographique') }}</span>
          </dt>
          <dd>
            <span>{{ crise.zoneGeographique }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.crise.cloturee') }}</span>
          </dt>
          <dd>
            <span>{{ crise.cloturee }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.crise.autorite') }}</span>
          </dt>
          <dd>
            <div v-if="crise.autorite">
              <router-link :to="{ name: 'AutoriteView', params: { autoriteId: crise.autorite.id } }">{{ crise.autorite.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="crise.id" :to="{ name: 'CriseEdit', params: { criseId: crise.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./crise-details.component.ts"></script>
