<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="assistaCriseApp.autorite.home.createOrEditLabel" data-cy="AutoriteCreateUpdateHeading">
          {{ t$('assistaCriseApp.autorite.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="autorite.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="autorite.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="autorite">{{ t$('assistaCriseApp.autorite.nom') }}</label>
            <input
              type="text"
              class="form-control"
              name="nom"
              id="autorite-nom"
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
            <label class="form-control-label" for="autorite">{{ t$('assistaCriseApp.autorite.type') }}</label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !v$.type.$invalid, invalid: v$.type.$invalid }"
              v-model="v$.type.$model"
              id="autorite-type"
              data-cy="type"
              required
            >
              <option
                v-for="typeAutorite in typeAutoriteValues"
                :key="typeAutorite"
                :value="typeAutorite"
                :label="t$('assistaCriseApp.TypeAutorite.' + typeAutorite)"
              >
                {{ typeAutorite }}
              </option>
            </select>
            <div v-if="v$.type.$anyDirty && v$.type.$invalid">
              <small class="form-text text-danger" v-for="error of v$.type.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="autorite">{{ t$('assistaCriseApp.autorite.territoire') }}</label>
            <input
              type="text"
              class="form-control"
              name="territoire"
              id="autorite-territoire"
              data-cy="territoire"
              :class="{ valid: !v$.territoire.$invalid, invalid: v$.territoire.$invalid }"
              v-model="v$.territoire.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="autorite">{{ t$('assistaCriseApp.autorite.contact') }}</label>
            <input
              type="text"
              class="form-control"
              name="contact"
              id="autorite-contact"
              data-cy="contact"
              :class="{ valid: !v$.contact.$invalid, invalid: v$.contact.$invalid }"
              v-model="v$.contact.$model"
            />
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
<script lang="ts" src="./autorite-update.component.ts"></script>
