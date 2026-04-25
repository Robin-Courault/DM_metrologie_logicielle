<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="assistaCriseApp.salonDiscussion.home.createOrEditLabel" data-cy="SalonDiscussionCreateUpdateHeading">
          {{ t$('assistaCriseApp.salonDiscussion.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="salonDiscussion.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="salonDiscussion.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="salon-discussion">{{ t$('assistaCriseApp.salonDiscussion.dateOuverture') }}</label>
            <div class="d-flex">
              <input
                id="salon-discussion-dateOuverture"
                data-cy="dateOuverture"
                type="datetime-local"
                class="form-control"
                name="dateOuverture"
                :class="{ valid: !v$.dateOuverture.$invalid, invalid: v$.dateOuverture.$invalid }"
                :value="convertDateTimeFromServer(v$.dateOuverture.$model)"
                @change="updateInstantField('dateOuverture', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="salon-discussion">{{ t$('assistaCriseApp.salonDiscussion.ouvert') }}</label>
            <input
              type="checkbox"
              class="form-check"
              name="ouvert"
              id="salon-discussion-ouvert"
              data-cy="ouvert"
              :class="{ valid: !v$.ouvert.$invalid, invalid: v$.ouvert.$invalid }"
              v-model="v$.ouvert.$model"
            />
          </div>
          <div class="mb-3">
            <label for="salon-discussion">{{ t$('assistaCriseApp.salonDiscussion.participants') }}</label>
            <select
              class="form-control"
              id="salon-discussion-participantses"
              data-cy="participants"
              multiple
              name="participants"
              v-if="salonDiscussion.participantses !== undefined"
              v-model="salonDiscussion.participantses"
            >
              <option
                :value="getSelected(salonDiscussion.participantses, utilisateurOption, 'id')"
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
<script lang="ts" src="./salon-discussion-update.component.ts"></script>
