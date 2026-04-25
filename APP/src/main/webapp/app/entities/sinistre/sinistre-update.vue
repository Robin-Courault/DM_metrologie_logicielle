<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="assistaCriseApp.sinistre.home.createOrEditLabel" data-cy="SinistreCreateUpdateHeading">
          {{ t$('assistaCriseApp.sinistre.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="sinistre.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="sinistre.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="sinistre">{{ t$('assistaCriseApp.sinistre.utilisateur') }}</label>
            <select
              class="form-control"
              id="sinistre-utilisateur"
              data-cy="utilisateur"
              name="utilisateur"
              v-model="sinistre.utilisateur"
              required
            >
              <option v-if="!sinistre.utilisateur" :value="null" selected></option>
              <option
                :value="sinistre.utilisateur && utilisateurOption.id === sinistre.utilisateur.id ? sinistre.utilisateur : utilisateurOption"
                v-for="utilisateurOption in utilisateurs"
                :key="utilisateurOption.id"
              >
                {{ utilisateurOption.login }}
              </option>
            </select>
          </div>
          <div v-if="v$.utilisateur.$anyDirty && v$.utilisateur.$invalid">
            <small class="form-text text-danger" v-for="error of v$.utilisateur.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./sinistre-update.component.ts"></script>
