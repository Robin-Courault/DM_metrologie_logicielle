<template>
  <div>
    <h2 id="page-heading" data-cy="CitoyenHeading">
      <span id="citoyen">{{ t$('assistaCriseApp.citoyen.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.citoyen.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'CitoyenCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-citoyen"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.citoyen.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && citoyens?.length === 0">
      <span>{{ t$('assistaCriseApp.citoyen.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="citoyens?.length > 0">
      <table class="table table-striped" aria-describedby="citoyens">
        <thead>
          <tr>
            <th scope="col">
              <span>{{ t$('global.field.id') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.citoyen.utilisateur') }}</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="citoyen in citoyens" :key="citoyen.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CitoyenView', params: { citoyenId: citoyen.id } }">{{ citoyen.id }}</router-link>
            </td>
            <td>
              <div v-if="citoyen.utilisateur">
                <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: citoyen.utilisateur.id } }">{{
                  citoyen.utilisateur.login
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'CitoyenView', params: { citoyenId: citoyen.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CitoyenEdit', params: { citoyenId: citoyen.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(citoyen)"
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
        <span id="assistaCriseApp.citoyen.delete.question" data-cy="citoyenDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-citoyen-heading">{{ t$('assistaCriseApp.citoyen.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-citoyen"
            data-cy="entityConfirmDeleteButton"
            @click="removeCitoyen"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./citoyen.component.ts"></script>
