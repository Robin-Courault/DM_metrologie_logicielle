<template>
  <div>
    <h2 id="page-heading" data-cy="CriseHeading">
      <span id="crise">{{ t$('assistaCriseApp.crise.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.crise.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'CriseCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-crise"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.crise.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && crises?.length === 0">
      <span>{{ t$('assistaCriseApp.crise.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="crises?.length > 0">
      <table class="table table-striped" aria-describedby="crises">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('titre')">
              <span>{{ t$('assistaCriseApp.crise.titre') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'titre'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('description')">
              <span>{{ t$('assistaCriseApp.crise.description') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('type')">
              <span>{{ t$('assistaCriseApp.crise.type') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'type'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('dateDebut')">
              <span>{{ t$('assistaCriseApp.crise.dateDebut') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dateDebut'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('dateFin')">
              <span>{{ t$('assistaCriseApp.crise.dateFin') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dateFin'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('zoneGeographique')">
              <span>{{ t$('assistaCriseApp.crise.zoneGeographique') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'zoneGeographique'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('cloturee')">
              <span>{{ t$('assistaCriseApp.crise.cloturee') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cloturee'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('autorite.id')">
              <span>{{ t$('assistaCriseApp.crise.autorite') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'autorite.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="crise in crises" :key="crise.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CriseView', params: { criseId: crise.id } }">{{ crise.id }}</router-link>
            </td>
            <td>{{ crise.titre }}</td>
            <td>{{ crise.description }}</td>
            <td>{{ t$('assistaCriseApp.TypeCrise.' + crise.type) }}</td>
            <td>{{ formatDateShort(crise.dateDebut) || '' }}</td>
            <td>{{ formatDateShort(crise.dateFin) || '' }}</td>
            <td>{{ crise.zoneGeographique }}</td>
            <td>{{ crise.cloturee }}</td>
            <td>
              <div v-if="crise.autorite">
                <router-link :to="{ name: 'AutoriteView', params: { autoriteId: crise.autorite.id } }">{{ crise.autorite.id }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'CriseView', params: { criseId: crise.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CriseEdit', params: { criseId: crise.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(crise)"
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
        <span id="assistaCriseApp.crise.delete.question" data-cy="criseDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-crise-heading">{{ t$('assistaCriseApp.crise.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-crise"
            data-cy="entityConfirmDeleteButton"
            @click="removeCrise"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="crises?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./crise.component.ts"></script>
