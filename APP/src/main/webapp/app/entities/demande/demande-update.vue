<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="assistaCriseApp.demande.home.createOrEditLabel" data-cy="DemandeCreateUpdateHeading">
          {{ t$('assistaCriseApp.demande.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="demande.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="demande.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="demande">{{ t$('assistaCriseApp.demande.etatDemande') }}</label>
            <select
              class="form-control"
              name="etatDemande"
              :class="{ valid: !v$.etatDemande.$invalid, invalid: v$.etatDemande.$invalid }"
              v-model="v$.etatDemande.$model"
              id="demande-etatDemande"
              data-cy="etatDemande"
            >
              <option
                v-for="etatDemande in etatDemandeValues"
                :key="etatDemande"
                :value="etatDemande"
                :label="t$('assistaCriseApp.EtatDemande.' + etatDemande)"
              >
                {{ etatDemande }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="demande">{{ t$('assistaCriseApp.demande.dateFermeture') }}</label>
            <div class="d-flex">
              <input
                id="demande-dateFermeture"
                data-cy="dateFermeture"
                type="datetime-local"
                class="form-control"
                name="dateFermeture"
                :class="{ valid: !v$.dateFermeture.$invalid, invalid: v$.dateFermeture.$invalid }"
                :value="convertDateTimeFromServer(v$.dateFermeture.$model)"
                @change="updateInstantField('dateFermeture', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="demande">{{ t$('assistaCriseApp.demande.quantite') }}</label>
            <input
              type="number"
              class="form-control"
              name="quantite"
              id="demande-quantite"
              data-cy="quantite"
              :class="{ valid: !v$.quantite.$invalid, invalid: v$.quantite.$invalid }"
              v-model.number="v$.quantite.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="demande">{{ t$('assistaCriseApp.demande.annonce') }}</label>
            <select class="form-control" id="demande-annonce" data-cy="annonce" name="annonce" v-model="demande.annonce" required>
              <option v-if="!demande.annonce" :value="null" selected></option>
              <option
                :value="demande.annonce && annonceOption.id === demande.annonce.id ? demande.annonce : annonceOption"
                v-for="annonceOption in annonces"
                :key="annonceOption.id"
              >
                {{ annonceOption.titre }}
              </option>
            </select>
          </div>
          <div v-if="v$.annonce.$anyDirty && v$.annonce.$invalid">
            <small class="form-text text-danger" v-for="error of v$.annonce.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="demande">{{ t$('assistaCriseApp.demande.salonDiscussion') }}</label>
            <select
              class="form-control"
              id="demande-salonDiscussion"
              data-cy="salonDiscussion"
              name="salonDiscussion"
              v-model="demande.salonDiscussion"
            >
              <option :value="null"></option>
              <option
                :value="
                  demande.salonDiscussion && salonDiscussionOption.id === demande.salonDiscussion.id
                    ? demande.salonDiscussion
                    : salonDiscussionOption
                "
                v-for="salonDiscussionOption in salonDiscussions"
                :key="salonDiscussionOption.id"
              >
                {{ salonDiscussionOption.id }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="demande">{{ t$('assistaCriseApp.demande.sinistre') }}</label>
            <select class="form-control" id="demande-sinistre" data-cy="sinistre" name="sinistre" v-model="demande.sinistre" required>
              <option v-if="!demande.sinistre" :value="null" selected></option>
              <option
                :value="demande.sinistre && sinistreOption.id === demande.sinistre.id ? demande.sinistre : sinistreOption"
                v-for="sinistreOption in sinistres"
                :key="sinistreOption.id"
              >
                {{ sinistreOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.sinistre.$anyDirty && v$.sinistre.$invalid">
            <small class="form-text text-danger" v-for="error of v$.sinistre.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="demande">{{ t$('assistaCriseApp.demande.crise') }}</label>
            <select class="form-control" id="demande-crise" data-cy="crise" name="crise" v-model="demande.crise" required>
              <option v-if="!demande.crise" :value="null" selected></option>
              <option
                :value="demande.crise && criseOption.id === demande.crise.id ? demande.crise : criseOption"
                v-for="criseOption in crises"
                :key="criseOption.id"
              >
                {{ criseOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.crise.$anyDirty && v$.crise.$invalid">
            <small class="form-text text-danger" v-for="error of v$.crise.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label for="demande">{{ t$('assistaCriseApp.demande.offres') }}</label>
            <select
              class="form-control"
              id="demande-offreses"
              data-cy="offres"
              multiple
              name="offres"
              v-if="demande.offreses !== undefined"
              v-model="demande.offreses"
            >
              <option :value="getSelected(demande.offreses, offreOption, 'id')" v-for="offreOption in offres" :key="offreOption.id">
                {{ offreOption.id }}
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
<script lang="ts" src="./demande-update.component.ts"></script>
