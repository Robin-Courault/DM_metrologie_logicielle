<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="utilisateur">
        <h2 class="jh-entity-heading" data-cy="utilisateurDetailsHeading">
          <span>{{ t$('assistaCriseApp.utilisateur.detail.title') }}Utilisateur</span> {{ utilisateur.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.utilisateur.login') }}</span>
          </dt>
          <dd>
            <span>{{ utilisateur.login }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.utilisateur.nom') }}</span>
          </dt>
          <dd>
            <span>{{ utilisateur.nom }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.utilisateur.prenom') }}</span>
          </dt>
          <dd>
            <span>{{ utilisateur.prenom }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.utilisateur.email') }}</span>
          </dt>
          <dd>
            <span>{{ utilisateur.email }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.utilisateur.telephone') }}</span>
          </dt>
          <dd>
            <span>{{ utilisateur.telephone }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.utilisateur.motDePasse') }}</span>
          </dt>
          <dd>
            <span>{{ utilisateur.motDePasse }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.utilisateur.dateInscription') }}</span>
          </dt>
          <dd>
            <span v-if="utilisateur.dateInscription">{{ formatDateLong(utilisateur.dateInscription) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.utilisateur.actif') }}</span>
          </dt>
          <dd>
            <span>{{ utilisateur.actif }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.utilisateur.banni') }}</span>
          </dt>
          <dd>
            <span>{{ utilisateur.banni }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.utilisateur.salons') }}</span>
          </dt>
          <dd>
            <span v-for="(salons, i) in utilisateur.salonses" :key="salons.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'SalonDiscussionView', params: { salonDiscussionId: salons.id } }">{{ salons.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="utilisateur.id"
          :to="{ name: 'UtilisateurEdit', params: { utilisateurId: utilisateur.id } }"
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

<script lang="ts" src="./utilisateur-details.component.ts"></script>
