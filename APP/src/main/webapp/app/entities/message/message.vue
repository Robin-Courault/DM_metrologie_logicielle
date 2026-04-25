<template>
  <div>
    <h2 id="page-heading" data-cy="MessageHeading">
      <span id="message">{{ t$('assistaCriseApp.message.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.message.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'MessageCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-message"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.message.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && messages?.length === 0">
      <span>{{ t$('assistaCriseApp.message.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="messages?.length > 0">
      <table class="table table-striped" aria-describedby="messages">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('contenu')">
              <span>{{ t$('assistaCriseApp.message.contenu') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'contenu'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('dateEnvoi')">
              <span>{{ t$('assistaCriseApp.message.dateEnvoi') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dateEnvoi'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('utilisateur.login')">
              <span>{{ t$('assistaCriseApp.message.utilisateur') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'utilisateur.login'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('salonDiscussion.id')">
              <span>{{ t$('assistaCriseApp.message.salonDiscussion') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'salonDiscussion.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="message in messages" :key="message.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MessageView', params: { messageId: message.id } }">{{ message.id }}</router-link>
            </td>
            <td>{{ message.contenu }}</td>
            <td>{{ formatDateShort(message.dateEnvoi) || '' }}</td>
            <td>
              <div v-if="message.utilisateur">
                <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: message.utilisateur.id } }">{{
                  message.utilisateur.login
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="message.salonDiscussion">
                <router-link :to="{ name: 'SalonDiscussionView', params: { salonDiscussionId: message.salonDiscussion.id } }">{{
                  message.salonDiscussion.id
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'MessageView', params: { messageId: message.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MessageEdit', params: { messageId: message.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(message)"
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
        <span id="assistaCriseApp.message.delete.question" data-cy="messageDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-message-heading">{{ t$('assistaCriseApp.message.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-message"
            data-cy="entityConfirmDeleteButton"
            @click="removeMessage"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="messages?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./message.component.ts"></script>
