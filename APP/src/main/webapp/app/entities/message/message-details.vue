<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="message">
        <h2 class="jh-entity-heading" data-cy="messageDetailsHeading">
          <span>{{ t$('assistaCriseApp.message.detail.title') }}Message</span> {{ message.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.message.contenu') }}</span>
          </dt>
          <dd>
            <span>{{ message.contenu }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.message.dateEnvoi') }}</span>
          </dt>
          <dd>
            <span v-if="message.dateEnvoi">{{ formatDateLong(message.dateEnvoi) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.message.utilisateur') }}</span>
          </dt>
          <dd>
            <div v-if="message.utilisateur">
              <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: message.utilisateur.id } }">{{
                message.utilisateur.login
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.message.salonDiscussion') }}</span>
          </dt>
          <dd>
            <div v-if="message.salonDiscussion">
              <router-link :to="{ name: 'SalonDiscussionView', params: { salonDiscussionId: message.salonDiscussion.id } }">{{
                message.salonDiscussion.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link v-if="message.id" :to="{ name: 'MessageEdit', params: { messageId: message.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.edit') }}</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./message-details.component.ts"></script>
