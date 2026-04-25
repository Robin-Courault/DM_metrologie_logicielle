<template>
  <div>
    <h2 id="page-heading" data-cy="AnnonceHeading">
      <span id="annonce">{{ t$('assistaCriseApp.annonce.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.annonce.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'AnnonceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-annonce"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.annonce.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && annonces?.length === 0">
      <span>{{ t$('assistaCriseApp.annonce.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="annonces?.length > 0">
      <table class="table table-striped" aria-describedby="annonces">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('titre')">
              <span>{{ t$('assistaCriseApp.annonce.titre') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'titre'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('description')">
              <span>{{ t$('assistaCriseApp.annonce.description') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('categorie')">
              <span>{{ t$('assistaCriseApp.annonce.categorie') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'categorie'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('latitude')">
              <span>{{ t$('assistaCriseApp.annonce.latitude') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'latitude'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('longitude')">
              <span>{{ t$('assistaCriseApp.annonce.longitude') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'longitude'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('adresse')">
              <span>{{ t$('assistaCriseApp.annonce.adresse') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'adresse'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('dateCreation')">
              <span>{{ t$('assistaCriseApp.annonce.dateCreation') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dateCreation'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('dateMaJ')">
              <span>{{ t$('assistaCriseApp.annonce.dateMaJ') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dateMaJ'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('etat')">
              <span>{{ t$('assistaCriseApp.annonce.etat') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'etat'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="annonce in annonces" :key="annonce.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AnnonceView', params: { annonceId: annonce.id } }">{{ annonce.id }}</router-link>
            </td>
            <td>{{ annonce.titre }}</td>
            <td>{{ annonce.description }}</td>
            <td>{{ t$('assistaCriseApp.CategorieBesoin.' + annonce.categorie) }}</td>
            <td>{{ annonce.latitude }}</td>
            <td>{{ annonce.longitude }}</td>
            <td>{{ annonce.adresse }}</td>
            <td>{{ formatDateShort(annonce.dateCreation) || '' }}</td>
            <td>{{ formatDateShort(annonce.dateMaJ) || '' }}</td>
            <td>{{ t$('assistaCriseApp.EtatAnnonce.' + annonce.etat) }}</td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'AnnonceView', params: { annonceId: annonce.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AnnonceEdit', params: { annonceId: annonce.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(annonce)"
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
        <span id="assistaCriseApp.annonce.delete.question" data-cy="annonceDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-annonce-heading">{{ t$('assistaCriseApp.annonce.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-annonce"
            data-cy="entityConfirmDeleteButton"
            @click="removeAnnonce"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="annonces?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./annonce.component.ts"></script>
