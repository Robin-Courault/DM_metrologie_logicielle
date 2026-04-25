<template>
  <div>
    <h2 id="page-heading" data-cy="OffreHeading">
      <span id="offre">{{ t$('assistaCriseApp.offre.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.offre.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'OffreCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-offre"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.offre.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && offres?.length === 0">
      <span>{{ t$('assistaCriseApp.offre.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="offres?.length > 0">
      <table class="table table-striped" aria-describedby="offres">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('disponibleDe')">
              <span>{{ t$('assistaCriseApp.offre.disponibleDe') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'disponibleDe'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('disponibleJusqua')">
              <span>{{ t$('assistaCriseApp.offre.disponibleJusqua') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'disponibleJusqua'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('quantite')">
              <span>{{ t$('assistaCriseApp.offre.quantite') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'quantite'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('annonce.titre')">
              <span>{{ t$('assistaCriseApp.offre.annonce') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'annonce.titre'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('citoyen.id')">
              <span>{{ t$('assistaCriseApp.offre.citoyen') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'citoyen.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('crise.id')">
              <span>{{ t$('assistaCriseApp.offre.crise') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'crise.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="offre in offres" :key="offre.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'OffreView', params: { offreId: offre.id } }">{{ offre.id }}</router-link>
            </td>
            <td>{{ formatDateShort(offre.disponibleDe) || '' }}</td>
            <td>{{ formatDateShort(offre.disponibleJusqua) || '' }}</td>
            <td>{{ offre.quantite }}</td>
            <td>
              <div v-if="offre.annonce">
                <router-link :to="{ name: 'AnnonceView', params: { annonceId: offre.annonce.id } }">{{ offre.annonce.titre }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="offre.citoyen">
                <router-link :to="{ name: 'CitoyenView', params: { citoyenId: offre.citoyen.id } }">{{ offre.citoyen.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="offre.crise">
                <router-link :to="{ name: 'CriseView', params: { criseId: offre.crise.id } }">{{ offre.crise.id }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'OffreView', params: { offreId: offre.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'OffreEdit', params: { offreId: offre.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(offre)"
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
        <span id="assistaCriseApp.offre.delete.question" data-cy="offreDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-offre-heading">{{ t$('assistaCriseApp.offre.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-offre"
            data-cy="entityConfirmDeleteButton"
            @click="removeOffre"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="offres?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./offre.component.ts"></script>
