<template>
  <div>
    <h2 id="page-heading" data-cy="AutoriteHeading">
      <span id="autorite">{{ t$('assistaCriseApp.autorite.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.autorite.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'AutoriteCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-autorite"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.autorite.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && autorites?.length === 0">
      <span>{{ t$('assistaCriseApp.autorite.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="autorites?.length > 0">
      <table class="table table-striped" aria-describedby="autorites">
        <thead>
          <tr>
            <th scope="col">
              <span>{{ t$('global.field.id') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.autorite.nom') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.autorite.type') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.autorite.territoire') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.autorite.contact') }}</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="autorite in autorites" :key="autorite.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AutoriteView', params: { autoriteId: autorite.id } }">{{ autorite.id }}</router-link>
            </td>
            <td>{{ autorite.nom }}</td>
            <td>{{ t$('assistaCriseApp.TypeAutorite.' + autorite.type) }}</td>
            <td>{{ autorite.territoire }}</td>
            <td>{{ autorite.contact }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'AutoriteView', params: { autoriteId: autorite.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AutoriteEdit', params: { autoriteId: autorite.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(autorite)"
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
        <span id="assistaCriseApp.autorite.delete.question" data-cy="autoriteDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-autorite-heading">{{ t$('assistaCriseApp.autorite.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-autorite"
            data-cy="entityConfirmDeleteButton"
            @click="removeAutorite"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./autorite.component.ts"></script>
