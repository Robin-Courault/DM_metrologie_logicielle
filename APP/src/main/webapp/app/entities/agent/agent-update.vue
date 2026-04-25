<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="assistaCriseApp.agent.home.createOrEditLabel" data-cy="AgentCreateUpdateHeading">
          {{ t$('assistaCriseApp.agent.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="agent.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="agent.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="agent">{{ t$('assistaCriseApp.agent.fonction') }}</label>
            <input
              type="text"
              class="form-control"
              name="fonction"
              id="agent-fonction"
              data-cy="fonction"
              :class="{ valid: !v$.fonction.$invalid, invalid: v$.fonction.$invalid }"
              v-model="v$.fonction.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="agent">{{ t$('assistaCriseApp.agent.service') }}</label>
            <input
              type="text"
              class="form-control"
              name="service"
              id="agent-service"
              data-cy="service"
              :class="{ valid: !v$.service.$invalid, invalid: v$.service.$invalid }"
              v-model="v$.service.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="agent">{{ t$('assistaCriseApp.agent.utilisateur') }}</label>
            <select
              class="form-control"
              id="agent-utilisateur"
              data-cy="utilisateur"
              name="utilisateur"
              v-model="agent.utilisateur"
              required
            >
              <option v-if="!agent.utilisateur" :value="null" selected></option>
              <option
                :value="agent.utilisateur && utilisateurOption.id === agent.utilisateur.id ? agent.utilisateur : utilisateurOption"
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
            <label class="form-control-label" for="agent">{{ t$('assistaCriseApp.agent.autorite') }}</label>
            <select class="form-control" id="agent-autorite" data-cy="autorite" name="autorite" v-model="agent.autorite" required>
              <option v-if="!agent.autorite" :value="null" selected></option>
              <option
                :value="agent.autorite && autoriteOption.id === agent.autorite.id ? agent.autorite : autoriteOption"
                v-for="autoriteOption in autorites"
                :key="autoriteOption.id"
              >
                {{ autoriteOption.nom }}
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
<script lang="ts" src="./agent-update.component.ts"></script>
