<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="autorite">
        <h2 class="jh-entity-heading" data-cy="autoriteDetailsHeading">
          <span>{{ t$('assistaCriseApp.autorite.detail.title') }}Autorite</span> {{ autorite.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.autorite.nom') }}</span>
          </dt>
          <dd>
            <span>{{ autorite.nom }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.autorite.type') }}</span>
          </dt>
          <dd>
            <span>{{ t$('assistaCriseApp.TypeAutorite.' + autorite.type) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.autorite.territoire') }}</span>
          </dt>
          <dd>
            <span>{{ autorite.territoire }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.autorite.contact') }}</span>
          </dt>
          <dd>
            <span>{{ autorite.contact }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="autorite.id" :to="{ name: 'AutoriteEdit', params: { autoriteId: autorite.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./autorite-details.component.ts"></script>
