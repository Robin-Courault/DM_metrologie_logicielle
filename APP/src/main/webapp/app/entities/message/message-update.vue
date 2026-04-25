<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="assistaCriseApp.message.home.createOrEditLabel" data-cy="MessageCreateUpdateHeading">
          {{ t$('assistaCriseApp.message.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="message.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="message.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="message">{{ t$('assistaCriseApp.message.contenu') }}</label>
            <input
              type="text"
              class="form-control"
              name="contenu"
              id="message-contenu"
              data-cy="contenu"
              :class="{ valid: !v$.contenu.$invalid, invalid: v$.contenu.$invalid }"
              v-model="v$.contenu.$model"
              required
            />
            <div v-if="v$.contenu.$anyDirty && v$.contenu.$invalid">
              <small class="form-text text-danger" v-for="error of v$.contenu.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="message">{{ t$('assistaCriseApp.message.dateEnvoi') }}</label>
            <div class="d-flex">
              <input
                id="message-dateEnvoi"
                data-cy="dateEnvoi"
                type="datetime-local"
                class="form-control"
                name="dateEnvoi"
                :class="{ valid: !v$.dateEnvoi.$invalid, invalid: v$.dateEnvoi.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.dateEnvoi.$model)"
                @change="updateInstantField('dateEnvoi', $event)"
              />
            </div>
            <div v-if="v$.dateEnvoi.$anyDirty && v$.dateEnvoi.$invalid">
              <small class="form-text text-danger" v-for="error of v$.dateEnvoi.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="message">{{ t$('assistaCriseApp.message.utilisateur') }}</label>
            <select
              class="form-control"
              id="message-utilisateur"
              data-cy="utilisateur"
              name="utilisateur"
              v-model="message.utilisateur"
              required
            >
              <option v-if="!message.utilisateur" :value="null" selected></option>
              <option
                :value="message.utilisateur && utilisateurOption.id === message.utilisateur.id ? message.utilisateur : utilisateurOption"
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
          <div class="mb-3">
            <label class="form-control-label" for="message">{{ t$('assistaCriseApp.message.salonDiscussion') }}</label>
            <select
              class="form-control"
              id="message-salonDiscussion"
              data-cy="salonDiscussion"
              name="salonDiscussion"
              v-model="message.salonDiscussion"
              required
            >
              <option v-if="!message.salonDiscussion" :value="null" selected></option>
              <option
                :value="
                  message.salonDiscussion && salonDiscussionOption.id === message.salonDiscussion.id
                    ? message.salonDiscussion
                    : salonDiscussionOption
                "
                v-for="salonDiscussionOption in salonDiscussions"
                :key="salonDiscussionOption.id"
              >
                {{ salonDiscussionOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.salonDiscussion.$anyDirty && v$.salonDiscussion.$invalid">
            <small class="form-text text-danger" v-for="error of v$.salonDiscussion.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./message-update.component.ts"></script>
