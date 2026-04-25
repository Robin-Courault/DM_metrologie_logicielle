<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="assistaCriseApp.offre.home.createOrEditLabel" data-cy="OffreCreateUpdateHeading">
          {{ t$('assistaCriseApp.offre.home.createOrEditLabel') }}
        </h2>
        <div>
          <div class="mb-3" v-if="offre.id">
            <label for="id">{{ t$('global.field.id') }}</label>
            <input type="text" class="form-control" id="id" name="id" v-model="offre.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="offre">{{ t$('assistaCriseApp.offre.disponibleDe') }}</label>
            <div class="d-flex">
              <input
                id="offre-disponibleDe"
                data-cy="disponibleDe"
                type="datetime-local"
                class="form-control"
                name="disponibleDe"
                :class="{ valid: !v$.disponibleDe.$invalid, invalid: v$.disponibleDe.$invalid }"
                :value="convertDateTimeFromServer(v$.disponibleDe.$model)"
                @change="updateInstantField('disponibleDe', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="offre">{{ t$('assistaCriseApp.offre.disponibleJusqua') }}</label>
            <div class="d-flex">
              <input
                id="offre-disponibleJusqua"
                data-cy="disponibleJusqua"
                type="datetime-local"
                class="form-control"
                name="disponibleJusqua"
                :class="{ valid: !v$.disponibleJusqua.$invalid, invalid: v$.disponibleJusqua.$invalid }"
                :value="convertDateTimeFromServer(v$.disponibleJusqua.$model)"
                @change="updateInstantField('disponibleJusqua', $event)"
              />
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="offre">{{ t$('assistaCriseApp.offre.quantite') }}</label>
            <input
              type="number"
              class="form-control"
              name="quantite"
              id="offre-quantite"
              data-cy="quantite"
              :class="{ valid: !v$.quantite.$invalid, invalid: v$.quantite.$invalid }"
              v-model.number="v$.quantite.$model"
            />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="offre">{{ t$('assistaCriseApp.offre.annonce') }}</label>
            <select class="form-control" id="offre-annonce" data-cy="annonce" name="annonce" v-model="offre.annonce" required>
              <option v-if="!offre.annonce" :value="null" selected></option>
              <option
                :value="offre.annonce && annonceOption.id === offre.annonce.id ? offre.annonce : annonceOption"
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
            <label class="form-control-label" for="offre">{{ t$('assistaCriseApp.offre.citoyen') }}</label>
            <select class="form-control" id="offre-citoyen" data-cy="citoyen" name="citoyen" v-model="offre.citoyen" required>
              <option v-if="!offre.citoyen" :value="null" selected></option>
              <option
                :value="offre.citoyen && citoyenOption.id === offre.citoyen.id ? offre.citoyen : citoyenOption"
                v-for="citoyenOption in citoyens"
                :key="citoyenOption.id"
              >
                {{ citoyenOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.citoyen.$anyDirty && v$.citoyen.$invalid">
            <small class="form-text text-danger" v-for="error of v$.citoyen.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="offre">{{ t$('assistaCriseApp.offre.crise') }}</label>
            <select class="form-control" id="offre-crise" data-cy="crise" name="crise" v-model="offre.crise" required>
              <option v-if="!offre.crise" :value="null" selected></option>
              <option
                :value="offre.crise && criseOption.id === offre.crise.id ? offre.crise : criseOption"
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
            <label for="offre">{{ t$('assistaCriseApp.offre.demandes') }}</label>
            <select
              class="form-control"
              id="offre-demandeses"
              data-cy="demandes"
              multiple
              name="demandes"
              v-if="offre.demandeses !== undefined"
              v-model="offre.demandeses"
            >
              <option :value="getSelected(offre.demandeses, demandeOption, 'id')" v-for="demandeOption in demandes" :key="demandeOption.id">
                {{ demandeOption.id }}
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
<script lang="ts" src="./offre-update.component.ts"></script>
