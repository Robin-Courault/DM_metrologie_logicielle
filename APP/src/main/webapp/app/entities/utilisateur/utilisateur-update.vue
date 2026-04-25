<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="assistaCriseApp.utilisateur.home.createOrEditLabel" data-cy="UtilisateurCreateUpdateHeading">
          {{ t$('assistaCriseApp.utilisateur.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="utilisateur.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="utilisateur.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="utilisateur">{{ t$('assistaCriseApp.utilisateur.login') }}</label>
            <input
              type="text"
              class="form-control"
              name="login"
              id="utilisateur-login"
              data-cy="login"
              :class="{ valid: !v$.login.$invalid, invalid: v$.login.$invalid }"
              v-model="v$.login.$model"
              required
            />
            <div v-if="v$.login.$anyDirty && v$.login.$invalid">
              <small class="form-text text-danger" v-for="error of v$.login.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="utilisateur">{{ t$('assistaCriseApp.utilisateur.nom') }}</label>
            <input
              type="text"
              class="form-control"
              name="nom"
              id="utilisateur-nom"
              data-cy="nom"
              :class="{ valid: !v$.nom.$invalid, invalid: v$.nom.$invalid }"
              v-model="v$.nom.$model"
              required
            />
            <div v-if="v$.nom.$anyDirty && v$.nom.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nom.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="utilisateur">{{ t$('assistaCriseApp.utilisateur.prenom') }}</label>
            <input
              type="text"
              class="form-control"
              name="prenom"
              id="utilisateur-prenom"
              data-cy="prenom"
              :class="{ valid: !v$.prenom.$invalid, invalid: v$.prenom.$invalid }"
              v-model="v$.prenom.$model"
              required
            />
            <div v-if="v$.prenom.$anyDirty && v$.prenom.$invalid">
              <small class="form-text text-danger" v-for="error of v$.prenom.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="utilisateur">{{ t$('assistaCriseApp.utilisateur.email') }}</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="utilisateur-email"
              data-cy="email"
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
              required
            />
            <div v-if="v$.email.$anyDirty && v$.email.$invalid">
              <small class="form-text text-danger" v-for="error of v$.email.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="utilisateur">{{ t$('assistaCriseApp.utilisateur.telephone') }}</label>
            <input
              type="text"
              class="form-control"
              name="telephone"
              id="utilisateur-telephone"
              data-cy="telephone"
              :class="{ valid: !v$.telephone.$invalid, invalid: v$.telephone.$invalid }"
              v-model="v$.telephone.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="utilisateur">{{ t$('assistaCriseApp.utilisateur.motDePasse') }}</label>
            <input
              type="text"
              class="form-control"
              name="motDePasse"
              id="utilisateur-motDePasse"
              data-cy="motDePasse"
              :class="{ valid: !v$.motDePasse.$invalid, invalid: v$.motDePasse.$invalid }"
              v-model="v$.motDePasse.$model"
              required
            />
            <div v-if="v$.motDePasse.$anyDirty && v$.motDePasse.$invalid">
              <small class="form-text text-danger" v-for="error of v$.motDePasse.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="utilisateur">{{ t$('assistaCriseApp.utilisateur.dateInscription') }}</label>
            <div class="d-flex">
              <input
                id="utilisateur-dateInscription"
                data-cy="dateInscription"
                type="datetime-local"
                class="form-control"
                name="dateInscription"
                :class="{ valid: !v$.dateInscription.$invalid, invalid: v$.dateInscription.$invalid }"
                :value="convertDateTimeFromServer(v$.dateInscription.$model)"
                @change="updateInstantField('dateInscription', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="utilisateur">{{ t$('assistaCriseApp.utilisateur.actif') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="actif"
              id="utilisateur-actif"
              data-cy="actif"
              :class="{ valid: !v$.actif.$invalid, invalid: v$.actif.$invalid }"
              v-model="v$.actif.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="utilisateur">{{ t$('assistaCriseApp.utilisateur.banni') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="banni"
              id="utilisateur-banni"
              data-cy="banni"
              :class="{ valid: !v$.banni.$invalid, invalid: v$.banni.$invalid }"
              v-model="v$.banni.$model"
            />
          </div>
          <div class="mb-3">
            <label for="utilisateur">{{ t$('assistaCriseApp.utilisateur.salons') }}</label>
            <select
              class="form-control"
              id="utilisateur-salonses"
              data-cy="salons"
              multiple
              name="salons"
              v-if="utilisateur.salonses !== undefined"
              v-model="utilisateur.salonses"
            >
              <option
                :value="getSelected(utilisateur.salonses, salonDiscussionOption, 'id')"
                v-for="salonDiscussionOption in salonDiscussions"
                :key="salonDiscussionOption.id"
              >
                {{ salonDiscussionOption.id }}
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
<script lang="ts" src="./utilisateur-update.component.ts"></script>
