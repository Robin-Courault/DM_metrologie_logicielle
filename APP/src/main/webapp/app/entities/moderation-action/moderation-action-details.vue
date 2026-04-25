<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="moderationAction">
        <h2 class="jh-entity-heading" data-cy="moderationActionDetailsHeading">
          <span>{{ t$('assistaCriseApp.moderationAction.detail.title') }}ModerationAction</span> {{ moderationAction.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.moderationAction.dateAction') }}</span>
          </dt>
          <dd>
            <span v-if="moderationAction.dateAction">{{ formatDateLong(moderationAction.dateAction) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.moderationAction.motif') }}</span>
          </dt>
          <dd>
            <span>{{ moderationAction.motif }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.moderationAction.type') }}</span>
          </dt>
          <dd>
            <span>{{ t$('assistaCriseApp.TypeModeration.' + moderationAction.type) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.moderationAction.administrateur') }}</span>
          </dt>
          <dd>
            <div v-if="moderationAction.administrateur">
              <router-link :to="{ name: 'AdministrateurView', params: { administrateurId: moderationAction.administrateur.id } }">{{
                moderationAction.administrateur.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.moderationAction.annonce') }}</span>
          </dt>
          <dd>
            <div v-if="moderationAction.annonce">
              <router-link :to="{ name: 'AnnonceView', params: { annonceId: moderationAction.annonce.id } }">{{
                moderationAction.annonce.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.moderationAction.utilisateurCible') }}</span>
          </dt>
          <dd>
            <div v-if="moderationAction.utilisateurCible">
              <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: moderationAction.utilisateurCible.id } }">{{
                moderationAction.utilisateurCible.login
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="moderationAction.id"
          :to="{ name: 'ModerationActionEdit', params: { moderationActionId: moderationAction.id } }"
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

<script lang="ts" src="./moderation-action-details.component.ts"></script>
