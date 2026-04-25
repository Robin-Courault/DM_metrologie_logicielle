<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="assistaCriseApp.annonce.home.createOrEditLabel" data-cy="AnnonceCreateUpdateHeading">
          {{ t$('assistaCriseApp.annonce.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="annonce.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="annonce.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="annonce">{{ t$('assistaCriseApp.annonce.titre') }}</label>
            <input
              type="text"
              class="form-control"
              name="titre"
              id="annonce-titre"
              data-cy="titre"
              :class="{ valid: !v$.titre.$invalid, invalid: v$.titre.$invalid }"
              v-model="v$.titre.$model"
              required
            />
            <div v-if="v$.titre.$anyDirty && v$.titre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.titre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="annonce">{{ t$('assistaCriseApp.annonce.description') }}</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="annonce-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="annonce">{{ t$('assistaCriseApp.annonce.categorie') }}</label>
            <select
              class="form-control"
              name="categorie"
              :class="{ valid: !v$.categorie.$invalid, invalid: v$.categorie.$invalid }"
              v-model="v$.categorie.$model"
              id="annonce-categorie"
              data-cy="categorie"
              required
            >
              <option
                v-for="categorieBesoin in categorieBesoinValues"
                :key="categorieBesoin"
                :value="categorieBesoin"
                :label="t$('assistaCriseApp.CategorieBesoin.' + categorieBesoin)"
              >
                {{ categorieBesoin }}
              </option>
            </select>
            <div v-if="v$.categorie.$anyDirty && v$.categorie.$invalid">
              <small class="form-text text-danger" v-for="error of v$.categorie.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="annonce">{{ t$('assistaCriseApp.annonce.latitude') }}</label>
            <input
              type="number"
              class="form-control"
              name="latitude"
              id="annonce-latitude"
              data-cy="latitude"
              :class="{ valid: !v$.latitude.$invalid, invalid: v$.latitude.$invalid }"
              v-model.number="v$.latitude.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="annonce">{{ t$('assistaCriseApp.annonce.longitude') }}</label>
            <input
              type="number"
              class="form-control"
              name="longitude"
              id="annonce-longitude"
              data-cy="longitude"
              :class="{ valid: !v$.longitude.$invalid, invalid: v$.longitude.$invalid }"
              v-model.number="v$.longitude.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="annonce">{{ t$('assistaCriseApp.annonce.adresse') }}</label>
            <input
              type="text"
              class="form-control"
              name="adresse"
              id="annonce-adresse"
              data-cy="adresse"
              :class="{ valid: !v$.adresse.$invalid, invalid: v$.adresse.$invalid }"
              v-model="v$.adresse.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="annonce">{{ t$('assistaCriseApp.annonce.dateCreation') }}</label>
            <div class="d-flex">
              <input
                id="annonce-dateCreation"
                data-cy="dateCreation"
                type="datetime-local"
                class="form-control"
                name="dateCreation"
                :class="{ valid: !v$.dateCreation.$invalid, invalid: v$.dateCreation.$invalid }"
                :value="convertDateTimeFromServer(v$.dateCreation.$model)"
                @change="updateInstantField('dateCreation', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="annonce">{{ t$('assistaCriseApp.annonce.dateMaJ') }}</label>
            <div class="d-flex">
              <input
                id="annonce-dateMaJ"
                data-cy="dateMaJ"
                type="datetime-local"
                class="form-control"
                name="dateMaJ"
                :class="{ valid: !v$.dateMaJ.$invalid, invalid: v$.dateMaJ.$invalid }"
                :value="convertDateTimeFromServer(v$.dateMaJ.$model)"
                @change="updateInstantField('dateMaJ', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="annonce">{{ t$('assistaCriseApp.annonce.etat') }}</label>
            <select
              class="form-control"
              name="etat"
              :class="{ valid: !v$.etat.$invalid, invalid: v$.etat.$invalid }"
              v-model="v$.etat.$model"
              id="annonce-etat"
              data-cy="etat"
            >
              <option
                v-for="etatAnnonce in etatAnnonceValues"
                :key="etatAnnonce"
                :value="etatAnnonce"
                :label="t$('assistaCriseApp.EtatAnnonce.' + etatAnnonce)"
              >
                {{ etatAnnonce }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.cancel') }}</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>{{ t$('entity.action.save') }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./annonce-update.component.ts"></script>
