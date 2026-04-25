<template>
  <div>
    <h2 id="page-heading" data-cy="SinistreHeading">
      <span id="sinistre">{{ t$('assistaCriseApp.sinistre.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.sinistre.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'SinistreCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-sinistre"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.sinistre.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && sinistres?.length === 0">
      <span>{{ t$('assistaCriseApp.sinistre.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="sinistres?.length > 0">
      <table class="table table-striped" aria-describedby="sinistres">
        <thead>
          <tr>
            <th scope="col">
              <span>{{ t$('global.field.id') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.sinistre.utilisateur') }}</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="sinistre in sinistres" :key="sinistre.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SinistreView', params: { sinistreId: sinistre.id } }">{{ sinistre.id }}</router-link>
            </td>
            <td>
              <div v-if="sinistre.utilisateur">
                <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: sinistre.utilisateur.id } }">{{
                  sinistre.utilisateur.login
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'SinistreView', params: { sinistreId: sinistre.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'SinistreEdit', params: { sinistreId: sinistre.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(sinistre)"
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
        <span id="assistaCriseApp.sinistre.delete.question" data-cy="sinistreDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-sinistre-heading">{{ t$('assistaCriseApp.sinistre.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-sinistre"
            data-cy="entityConfirmDeleteButton"
            @click="removeSinistre"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./sinistre.component.ts"></script>
