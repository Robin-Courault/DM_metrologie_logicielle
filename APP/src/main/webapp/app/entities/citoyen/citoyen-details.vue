<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="citoyen">
        <h2 class="jh-entity-heading" data-cy="citoyenDetailsHeading">
          <span>{{ t$('assistaCriseApp.citoyen.detail.title') }}Citoyen</span> {{ citoyen.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.citoyen.utilisateur') }}</span>
          </dt>
          <dd>
            <div v-if="citoyen.utilisateur">
              <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: citoyen.utilisateur.id } }">{{
                citoyen.utilisateur.login
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="citoyen.id" :to="{ name: 'CitoyenEdit', params: { citoyenId: citoyen.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./citoyen-details.component.ts"></script>
