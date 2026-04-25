<template>
  <div>
    <h2 id="page-heading" data-cy="UtilisateurHeading">
      <span id="utilisateur">{{ t$('assistaCriseApp.utilisateur.home.title') }}</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info me-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span>{{ t$('assistaCriseApp.utilisateur.home.refreshListLabel') }}</span>
        </button>
        <router-link :to="{ name: 'UtilisateurCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-utilisateur"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>{{ t$('assistaCriseApp.utilisateur.home.createLabel') }}</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && utilisateurs?.length === 0">
      <span>{{ t$('assistaCriseApp.utilisateur.home.notFound') }}</span>
    </div>
    <div class="table-responsive" v-if="utilisateurs?.length > 0">
      <table class="table table-striped" aria-describedby="utilisateurs">
        <thead>
          <tr>
            <th scope="col">
              <span>{{ t$('global.field.id') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.utilisateur.login') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.utilisateur.nom') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.utilisateur.prenom') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.utilisateur.email') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.utilisateur.telephone') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.utilisateur.motDePasse') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.utilisateur.dateInscription') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.utilisateur.actif') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.utilisateur.banni') }}</span>
            </th>
            <th scope="col">
              <span>{{ t$('assistaCriseApp.utilisateur.salons') }}</span>
            </th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="utilisateur in utilisateurs" :key="utilisateur.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: utilisateur.id } }">{{ utilisateur.id }}</router-link>
            </td>
            <td>{{ utilisateur.login }}</td>
            <td>{{ utilisateur.nom }}</td>
            <td>{{ utilisateur.prenom }}</td>
            <td>{{ utilisateur.email }}</td>
            <td>{{ utilisateur.telephone }}</td>
            <td>{{ utilisateur.motDePasse }}</td>
            <td>{{ formatDateShort(utilisateur.dateInscription) || '' }}</td>
            <td>{{ utilisateur.actif }}</td>
            <td>{{ utilisateur.banni }}</td>
            <td>
              <span v-for="(salons, i) in utilisateur.salonses" :key="salons.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'SalonDiscussionView', params: { salonDiscussionId: salons.id } }">{{
                  salons.id
                }}</router-link>
              </span>
            </td>
            <td class="text-end">
              <div class="btn-group">
                <router-link :to="{ name: 'UtilisateurView', params: { utilisateurId: utilisateur.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.view') }}</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'UtilisateurEdit', params: { utilisateurId: utilisateur.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">{{ t$('entity.action.edit') }}</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(utilisateur)"
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
        <span id="assistaCriseApp.utilisateur.delete.question" data-cy="utilisateurDeleteDialogHeading">{{
          t$('entity.delete.title')
        }}</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-utilisateur-heading">{{ t$('assistaCriseApp.utilisateur.delete.question', { id: removeId }) }}</p>
      </div>
      <template #footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">{{ t$('entity.action.cancel') }}</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-utilisateur"
            data-cy="entityConfirmDeleteButton"
            @click="removeUtilisateur"
          >
            {{ t$('entity.action.delete') }}
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./utilisateur.component.ts"></script>
