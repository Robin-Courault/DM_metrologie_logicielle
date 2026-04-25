<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="administrateur">
        <h2 class="jh-entity-heading" data-cy="administrateurDetailsHeading">
          <span>{{ t$('assistaCriseApp.administrateur.detail.title') }}Administrateur</span> {{ administrateur.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.administrateur.utilisateur') }}</span>
          </dt>
          <dd>
            <div v-if="administrateur.utilisateur">
              <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: administrateur.utilisateur.id } }">{{
                administrateur.utilisateur.login
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="administrateur.id"
          :to="{ name: 'AdministrateurEdit', params: { administrateurId: administrateur.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./administrateur-details.component.ts"></script>
