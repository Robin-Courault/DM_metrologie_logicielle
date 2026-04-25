<template>
  <div>
    <h2 id="page-heading" data-cy="SalonDiscussionHeading">
      <span id="salon-discussion">{{ t$('assistaCriseApp.salonDiscussion.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.salonDiscussion.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'SalonDiscussionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-salon-discussion"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.salonDiscussion.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && salonDiscussions?.length === 0">
      <span>{{ t$('assistaCriseApp.salonDiscussion.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="salonDiscussions?.length > 0">
      <table class="table table-striped" aria-describedby="salonDiscussions">
        <thead>
          <tr>
            <th scope="col">
              <span>{{ t$('global.field.id') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.salonDiscussion.dateOuverture') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.salonDiscussion.ouvert') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.salonDiscussion.participants') }}</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="salonDiscussion in salonDiscussions" :key="salonDiscussion.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SalonDiscussionView', params: { salonDiscussionId: salonDiscussion.id } }">{{
                salonDiscussion.id
              }}</router-link>
            </td>
            <td>{{ formatDateShort(salonDiscussion.dateOuverture) || '' }}</td>
            <td>{{ salonDiscussion.ouvert }}</td>
            <td>
              <span v-for="(participants, i) in salonDiscussion.participantses" :key="participants.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'UtilisateurView', params: { utilisateurId: participants.id } }">{{
                  participants.login
                }}</router-link>
              </span>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SalonDiscussionView', params: { salonDiscussionId: salonDiscussion.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SalonDiscussionEdit', params: { salonDiscussionId: salonDiscussion.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(salonDiscussion)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">{{ t$('entity.action.delete') }}</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #title>
        <span id="assistaCriseApp.salonDiscussion.delete.question" data-cy="salonDiscussionDeleteDialogHeading">{{
          t$('entity.delete.title')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-salonDiscussion-heading">{{ t$('assistaCriseApp.salonDiscussion.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-salonDiscussion"
            data-cy="entityConfirmDeleteButton"
            @click="removeSalonDiscussion"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./salon-discussion.component.ts"></script>
