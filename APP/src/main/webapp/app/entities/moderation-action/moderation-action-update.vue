<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="assistaCriseApp.moderationAction.home.createOrEditLabel" data-cy="ModerationActionCreateUpdateHeading">
          {{ t$('assistaCriseApp.moderationAction.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="moderationAction.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="moderationAction.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="moderation-action">{{ t$('assistaCriseApp.moderationAction.dateAction') }}</label>
            <div class="d-flex">
              <input
                id="moderation-action-dateAction"
                data-cy="dateAction"
                type="datetime-local"
                class="form-control"
                name="dateAction"
                :class="{ valid: !v$.dateAction.$invalid, invalid: v$.dateAction.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.dateAction.$model)"
                @change="updateInstantField('dateAction', $event)"
              />
            </div>
            <div v-if="v$.dateAction.$anyDirty && v$.dateAction.$invalid">
              <small class="form-text text-danger" v-for="error of v$.dateAction.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="moderation-action">{{ t$('assistaCriseApp.moderationAction.motif') }}</label>
            <input
              type="text"
              class="form-control"
              name="motif"
              id="moderation-action-motif"
              data-cy="motif"
              :class="{ valid: !v$.motif.$invalid, invalid: v$.motif.$invalid }"
              v-model="v$.motif.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="moderation-action">{{ t$('assistaCriseApp.moderationAction.type') }}</label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !v$.type.$invalid, invalid: v$.type.$invalid }"
              v-model="v$.type.$model"
              id="moderation-action-type"
              data-cy="type"
              required
            >
              <option
                v-for="typeModeration in typeModerationValues"
                :key="typeModeration"
                :value="typeModeration"
                :label="t$('assistaCriseApp.TypeModeration.' + typeModeration)"
              >
                {{ typeModeration }}
              </option>
            </select>
            <div v-if="v$.type.$anyDirty && v$.type.$invalid">
              <small class="form-text text-danger" v-for="error of v$.type.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="moderation-action">{{ t$('assistaCriseApp.moderationAction.administrateur') }}</label>
            <select
              class="form-control"
              id="moderation-action-administrateur"
              data-cy="administrateur"
              name="administrateur"
              v-model="moderationAction.administrateur"
              required
            >
              <option v-if="!moderationAction.administrateur" :value="null" selected></option>
              <option
                :value="
                  moderationAction.administrateur && administrateurOption.id === moderationAction.administrateur.id
                    ? moderationAction.administrateur
                    : administrateurOption
                "
                v-for="administrateurOption in administrateurs"
                :key="administrateurOption.id"
              >
                {{ administrateurOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.administrateur.$anyDirty && v$.administrateur.$invalid">
            <small class="form-text text-danger" v-for="error of v$.administrateur.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="moderation-action">{{ t$('assistaCriseApp.moderationAction.annonce') }}</label>
            <select class="form-control" id="moderation-action-annonce" data-cy="annonce" name="annonce" v-model="moderationAction.annonce">
              <option :value="null"></option>
              <option
                :value="
                  moderationAction.annonce && annonceOption.id === moderationAction.annonce.id ? moderationAction.annonce : annonceOption
                "
                v-for="annonceOption in annonces"
                :key="annonceOption.id"
              >
                {{ annonceOption.id }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="moderation-action">{{ t$('assistaCriseApp.moderationAction.utilisateurCible') }}</label>
            <select
              class="form-control"
              id="moderation-action-utilisateurCible"
              data-cy="utilisateurCible"
              name="utilisateurCible"
              v-model="moderationAction.utilisateurCible"
            >
              <option :value="null"></option>
              <option
                :value="
                  moderationAction.utilisateurCible && utilisateurOption.id === moderationAction.utilisateurCible.id
                    ? moderationAction.utilisateurCible
                    : utilisateurOption
                "
                v-for="utilisateurOption in utilisateurs"
                :key="utilisateurOption.id"
              >
                {{ utilisateurOption.login }}
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
<script lang="ts" src="./moderation-action-update.component.ts"></script>
