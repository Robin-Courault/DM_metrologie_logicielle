<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <div v-if="salonDiscussion">
        <h2 class="jh-entity-heading" data-cy="salonDiscussionDetailsHeading">
          <span>{{ t$('assistaCriseApp.salonDiscussion.detail.title') }}SalonDiscussion</span> {{ salonDiscussion.id }}
        </h2>
        <dl class="row-md jh-entity-details">
          <dt>
            <span>{{ t$('assistaCriseApp.salonDiscussion.dateOuverture') }}</span>
          </dt>
          <dd>
            <span v-if="salonDiscussion.dateOuverture">{{ formatDateLong(salonDiscussion.dateOuverture) }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.salonDiscussion.ouvert') }}</span>
          </dt>
          <dd>
            <span>{{ salonDiscussion.ouvert }}</span>
          </dd>
          <dt>
            <span>{{ t$('assistaCriseApp.salonDiscussion.participants') }}</span>
          </dt>
          <dd>
            <span v-for="(participants, i) in salonDiscussion.participantses" :key="participants.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: participants.id } }">{{
                participants.login
              }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.back') }}</span>
        </button>
        <router-link
          v-if="salonDiscussion.id"
          :to="{ name: 'SalonDiscussionEdit', params: { salonDiscussionId: salonDiscussion.id } }"
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

<script lang="ts" src="./salon-discussion-details.component.ts"></script>
