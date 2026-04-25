<template>
  <div>
    <h2 id="page-heading" data-cy="ModerationActionHeading">
      <span id="moderation-action">{{ t$('assistaCriseApp.moderationAction.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.moderationAction.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'ModerationActionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-moderation-action"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.moderationAction.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && moderationActions?.length === 0">
      <span>{{ t$('assistaCriseApp.moderationAction.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="moderationActions?.length > 0">
      <table class="table table-striped" aria-describedby="moderationActions">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('dateAction')">
              <span>{{ t$('assistaCriseApp.moderationAction.dateAction') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dateAction'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('motif')">
              <span>{{ t$('assistaCriseApp.moderationAction.motif') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'motif'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('type')">
              <span>{{ t$('assistaCriseApp.moderationAction.type') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'type'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('administrateur.id')">
              <span>{{ t$('assistaCriseApp.moderationAction.administrateur') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'administrateur.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('annonce.id')">
              <span>{{ t$('assistaCriseApp.moderationAction.annonce') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'annonce.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('utilisateurCible.login')">
              <span>{{ t$('assistaCriseApp.moderationAction.utilisateurCible') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'utilisateurCible.login'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="moderationAction in moderationActions" :key="moderationAction.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ModerationActionView', params: { moderationActionId: moderationAction.id } }">{{
                moderationAction.id
              }}</router-link>
            </td>
            <td>{{ formatDateShort(moderationAction.dateAction) || '' }}</td>
            <td>{{ moderationAction.motif }}</td>
            <td>{{ t$('assistaCriseApp.TypeModeration.' + moderationAction.type) }}</td>
            <td>
              <div v-if="moderationAction.administrateur">
                <router-link :to="{ name: 'AdministrateurView', params: { administrateurId: moderationAction.administrateur.id } }">{{
                  moderationAction.administrateur.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="moderationAction.annonce">
                <router-link :to="{ name: 'AnnonceView', params: { annonceId: moderationAction.annonce.id } }">{{
                  moderationAction.annonce.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="moderationAction.utilisateurCible">
                <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: moderationAction.utilisateurCible.id } }">{{
                  moderationAction.utilisateurCible.login
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ModerationActionView', params: { moderationActionId: moderationAction.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ModerationActionEdit', params: { moderationActionId: moderationAction.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(moderationAction)"
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
        <span id="assistaCriseApp.moderationAction.delete.question" data-cy="moderationActionDeleteDialogHeading">{{
          t$('entity.delete.title')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-moderationAction-heading">{{ t$('assistaCriseApp.moderationAction.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-moderationAction"
            data-cy="entityConfirmDeleteButton"
            @click="removeModerationAction"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="moderationActions?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./moderation-action.component.ts"></script>
