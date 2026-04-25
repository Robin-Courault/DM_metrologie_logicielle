<template>
  <div>
    <h2 id="page-heading" data-cy="DemandeHeading">
      <span id="demande">{{ t$('assistaCriseApp.demande.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.demande.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'DemandeCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-demande"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.demande.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && demandes?.length === 0">
      <span>{{ t$('assistaCriseApp.demande.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="demandes?.length > 0">
      <table class="table table-striped" aria-describedby="demandes">
        <thead>
          <tr>
            <th scope="col" @click="changeOrder('id')">
              <span>{{ t$('global.field.id') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('etatDemande')">
              <span>{{ t$('assistaCriseApp.demande.etatDemande') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'etatDemande'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('dateFermeture')">
              <span>{{ t$('assistaCriseApp.demande.dateFermeture') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dateFermeture'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('quantite')">
              <span>{{ t$('assistaCriseApp.demande.quantite') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'quantite'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('annonce.titre')">
              <span>{{ t$('assistaCriseApp.demande.annonce') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'annonce.titre'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('salonDiscussion.id')">
              <span>{{ t$('assistaCriseApp.demande.salonDiscussion') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'salonDiscussion.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('sinistre.id')">
              <span>{{ t$('assistaCriseApp.demande.sinistre') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sinistre.id'"></jhi-sort-indicator>
            </th>
            <th scope="col" @click="changeOrder('crise.id')">
              <span>{{ t$('assistaCriseApp.demande.crise') }}</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'crise.id'"></jhi-sort-indicator>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="demande in demandes" :key="demande.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'DemandeView', params: { demandeId: demande.id } }">{{ demande.id }}</router-link>
            </td>
            <td>{{ t$('assistaCriseApp.EtatDemande.' + demande.etatDemande) }}</td>
            <td>{{ formatDateShort(demande.dateFermeture) || '' }}</td>
            <td>{{ demande.quantite }}</td>
            <td>
              <div v-if="demande.annonce">
                <router-link :to="{ name: 'AnnonceView', params: { annonceId: demande.annonce.id } }">{{
                  demande.annonce.titre
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="demande.salonDiscussion">
                <router-link :to="{ name: 'SalonDiscussionView', params: { salonDiscussionId: demande.salonDiscussion.id } }">{{
                  demande.salonDiscussion.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="demande.sinistre">
                <router-link :to="{ name: 'SinistreView', params: { sinistreId: demande.sinistre.id } }">{{
                  demande.sinistre.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="demande.crise">
                <router-link :to="{ name: 'CriseView', params: { criseId: demande.crise.id } }">{{ demande.crise.id }}</router-link>
              </div>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'DemandeView', params: { demandeId: demande.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'DemandeEdit', params: { demandeId: demande.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(demande)"
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
        <span id="assistaCriseApp.demande.delete.question" data-cy="demandeDeleteDialogHeading">{{ t$('entity.delete.title') }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-demande-heading">{{ t$('assistaCriseApp.demande.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-demande"
            data-cy="entityConfirmDeleteButton"
            @click="removeDemande"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="demandes?.length > 0">
      <div class="d-flex justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :items-per-page="itemsPerPage"></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./demande.component.ts"></script>
