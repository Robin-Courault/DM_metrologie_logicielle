<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="sinistre">
        <h2 class="jh-entity-heading" data-cy="sinistreDetailsHeading">
          <span>{{ t$('assistaCriseApp.sinistre.detail.title') }}Sinistre</span> {{ sinistre.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.sinistre.utilisateur') }}</span>
          </dt>
          <dd>
            <div v-if="sinistre.utilisateur">
              <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: sinistre.utilisateur.id } }">{{
                sinistre.utilisateur.login
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="sinistre.id" :to="{ name: 'SinistreEdit', params: { sinistreId: sinistre.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./sinistre-details.component.ts"></script>
