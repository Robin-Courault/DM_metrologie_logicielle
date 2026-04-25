<template>
  <div>
    <h2 id="page-heading" data-cy="AdministrateurHeading">
      <span id="administrateur">{{ t$('assistaCriseApp.administrateur.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.administrateur.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'AdministrateurCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-administrateur"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.administrateur.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && administrateurs?.length === 0">
      <span>{{ t$('assistaCriseApp.administrateur.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="administrateurs?.length > 0">
      <table class="table table-striped" aria-describedby="administrateurs">
        <thead>
          <tr>
            <th scope="col">
              <span>{{ t$('global.field.id') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.administrateur.utilisateur') }}</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="administrateur in administrateurs" :key="administrateur.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AdministrateurView', params: { administrateurId: administrateur.id } }">{{
                administrateur.id
              }}</router-link>
            </td>
            <td>
              <div v-if="administrateur.utilisateur">
                <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: administrateur.utilisateur.id } }">{{
                  administrateur.utilisateur.login
                }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'AdministrateurView', params: { administrateurId: administrateur.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'AdministrateurEdit', params: { administrateurId: administrateur.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(administrateur)"
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
        <span id="assistaCriseApp.administrateur.delete.question" data-cy="administrateurDeleteDialogHeading">{{
          t$('entity.delete.title')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-administrateur-heading">{{ t$('assistaCriseApp.administrateur.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-administrateur"
            data-cy="entityConfirmDeleteButton"
            @click="removeAdministrateur"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./administrateur.component.ts"></script>
