<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="assistaCriseApp.crise.home.createOrEditLabel" data-cy="CriseCreateUpdateHeading">
          {{ t$('assistaCriseApp.crise.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="crise.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="crise.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="crise">{{ t$('assistaCriseApp.crise.titre') }}</label>
            <input
              type="text"
              class="form-control"
              name="titre"
              id="crise-titre"
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
            <label class="form-control-label" for="crise">{{ t$('assistaCriseApp.crise.description') }}</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="crise-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="crise">{{ t$('assistaCriseApp.crise.type') }}</label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !v$.type.$invalid, invalid: v$.type.$invalid }"
              v-model="v$.type.$model"
              id="crise-type"
              data-cy="type"
              required
            >
              <option
                v-for="typeCrise in typeCriseValues"
                :key="typeCrise"
                :value="typeCrise"
                :label="t$('assistaCriseApp.TypeCrise.' + typeCrise)"
              >
                {{ typeCrise }}
              </option>
            </select>
            <div v-if="v$.type.$anyDirty && v$.type.$invalid">
              <small class="form-text text-danger" v-for="error of v$.type.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="crise">{{ t$('assistaCriseApp.crise.dateDebut') }}</label>
            <div class="d-flex">
              <input
                id="crise-dateDebut"
                data-cy="dateDebut"
                type="datetime-local"
                class="form-control"
                name="dateDebut"
                :class="{ valid: !v$.dateDebut.$invalid, invalid: v$.dateDebut.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.dateDebut.$model)"
                @change="updateInstantField('dateDebut', $event)"
              />
            </div>
            <div v-if="v$.dateDebut.$anyDirty && v$.dateDebut.$invalid">
              <small class="form-text text-danger" v-for="error of v$.dateDebut.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="crise">{{ t$('assistaCriseApp.crise.dateFin') }}</label>
            <div class="d-flex">
              <input
                id="crise-dateFin"
                data-cy="dateFin"
                type="datetime-local"
                class="form-control"
                name="dateFin"
                :class="{ valid: !v$.dateFin.$invalid, invalid: v$.dateFin.$invalid }"
                :value="convertDateTimeFromServer(v$.dateFin.$model)"
                @change="updateInstantField('dateFin', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="crise">{{ t$('assistaCriseApp.crise.zoneGeographique') }}</label>
            <input
              type="text"
              class="form-control"
              name="zoneGeographique"
              id="crise-zoneGeographique"
              data-cy="zoneGeographique"
              :class="{ valid: !v$.zoneGeographique.$invalid, invalid: v$.zoneGeographique.$invalid }"
              v-model="v$.zoneGeographique.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="crise">{{ t$('assistaCriseApp.crise.cloturee') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="cloturee"
              id="crise-cloturee"
              data-cy="cloturee"
              :class="{ valid: !v$.cloturee.$invalid, invalid: v$.cloturee.$invalid }"
              v-model="v$.cloturee.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="crise">{{ t$('assistaCriseApp.crise.autorite') }}</label>
            <select class="form-control" id="crise-autorite" data-cy="autorite" name="autorite" v-model="crise.autorite" required>
              <option v-if="!crise.autorite" :value="null" selected></option>
              <option
                :value="crise.autorite && autoriteOption.id === crise.autorite.id ? crise.autorite : autoriteOption"
                v-for="autoriteOption in autorites"
                :key="autoriteOption.id"
              >
                {{ autoriteOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.autorite.$anyDirty && v$.autorite.$invalid">
            <small class="form-text text-danger" v-for="error of v$.autorite.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./crise-update.component.ts"></script>
